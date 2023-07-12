package org.makka.greenfarm.controller;
import cn.dev33.satoken.stp.StpUtil;
import com.baidu.aip.face.AipFace;
import org.json.JSONException;
import org.json.JSONObject;
import org.makka.greenfarm.common.CommonResponse;
import org.makka.greenfarm.domain.User;
import org.makka.greenfarm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/accounts")
public class UserController {


    @Value("${file.path}")
    private String filePath;

    @Autowired
    private AipFace aipFace;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public CommonResponse<List<String>> login(@RequestParam String username, @RequestParam String password) {
        // 验证是否登录成功并返回token
        if (userService.validation(username, password)) {
            String uid = userService.getUidByUsername(username);
            StpUtil.login(uid);
            int isPremium = userService.getIsPremiumByUid(uid);
            List<String> tokenAndIsPremium = List.of(StpUtil.getTokenValue(), String.valueOf(isPremium));
            return CommonResponse.creatForSuccess(tokenAndIsPremium);
        } else {
            return CommonResponse.creatForError("用户名或密码错误");
        }
    }

    @PostMapping("/register")
    public CommonResponse<String> registerUser(@RequestBody User user) {
        // Return the token to the frontend
        return userService.register(user);
    }

    @PostMapping("/logout")
    public CommonResponse<String> logout() {
        if (!StpUtil.isLogin()) {
            return CommonResponse.creatForError("已经登出！");
        } else {
            String uid = StpUtil.getLoginIdAsString();
            userService.updateUserState(uid);
            // Return the token to the frontend
            StpUtil.logout();
            return CommonResponse.creatForSuccess("success");
        }
    }

    @PostMapping("/avatar")
    public CommonResponse<String> uploadAvatar(HttpServletRequest request, @RequestParam MultipartFile file) {
        if (StpUtil.isLogin()) {
            String uid = StpUtil.getLoginIdAsString();
            if (userService.updateAvatar(uid, request, file)) {
                return CommonResponse.creatForSuccess("success");
            } else {
                return CommonResponse.creatForError("fail");
            }
        } else {
            return CommonResponse.creatForError("请先登录！");
        }
    }

    @GetMapping("/details")
    public CommonResponse<User> getUserInfo() {
        if (StpUtil.isLogin()) {
            String uid = StpUtil.getLoginIdAsString();
            return CommonResponse.creatForSuccess(userService.getUserInfo(uid));
        } else {
            return CommonResponse.creatForError("请先登录！");
        }
    }

    @PostMapping("/details")
    public CommonResponse<User> updateUserInfo(@RequestBody User user) {
        if (StpUtil.isLogin()) {
            String uid = StpUtil.getLoginIdAsString();
            user.setUid(uid);
            if (userService.updateUserInfo(user)) {
                return CommonResponse.creatForSuccess(user);
            } else {
                return CommonResponse.creatForError("fail");
            }
        } else {
            return CommonResponse.creatForError("请先登录！");
        }
    }

    @GetMapping("/virtualization")
    public CommonResponse<String> virtualization() {
        if (StpUtil.isLogin()) {
            String uid = StpUtil.getLoginIdAsString();
            return CommonResponse.creatForSuccess(userService.getVirtualizationByUid(uid));
        } else {
            return CommonResponse.creatForError("请先登录！");
        }
    }

    @PostMapping("/face-register")
    public CommonResponse<String> register(@RequestParam String username,@RequestParam String faceBase) throws JSONException {
        if(!StringUtils.isEmpty(username) && !StringUtils.isEmpty(faceBase)) {
            // 文件上传的地址
            System.out.println(filePath);
            // 图片名称
            String fileName = username + System.currentTimeMillis() + ".png";
            // 最终图片名称
            System.out.println(filePath + "\\" + fileName);
            File file = new File(filePath + "\\" + fileName);

            // 往数据库里插入一条用户数据
            User user = null;
            String uid = String.valueOf(System.currentTimeMillis());
            //判断是否已有该用户
            User exitUser = userService.selectUserByName(username);
            if(exitUser != null) {
                return CommonResponse.creatForSuccess("数据库中已存在该用户!");
            }
            // 向百度云人脸库插入一张人脸，并将face_token返回
            String face_token = faceSetAddUser(aipFace,faceBase,username);
            user.setUsername(username);
            user.setUid(uid);
            user.setFaceid(face_token);
            //存入用户数据库
            userService.addUsersByFace(user);
            // 保存上传摄像头捕获的图片
            saveLocalImage(faceBase, file);
            return CommonResponse.creatForSuccess("人脸注册成功!");
        }
        return CommonResponse.creatForError("人脸注册失败!");
    }

    @PostMapping("/face-login")
    public CommonResponse<List<String>> login(@RequestParam String faceBase) throws JSONException {
        String faceData = faceBase;
        // 进行人像数据对比
        Map<String,Double> map = verifyUser(faceData,aipFace);
        String tokenStr = map.keySet() + "";
        String username = tokenStr.substring(1,tokenStr.length()-1);
        Double num = map.get(username);
        //获取UID
        String uid = userService.getUidByUsername(username);
        StpUtil.login(uid);
        int isPremium = userService.getIsPremiumByUid(uid);
        List<String> tokenAndIsPremium = List.of(StpUtil.getTokenValue(), String.valueOf(isPremium));

        if( num > 95) {
            return CommonResponse.creatForSuccess(tokenAndIsPremium);
        }else {
            return CommonResponse.creatForError("人脸匹配失败,请靠近或清洗摄像头!");
        }
    }


    /**
     * 人脸比对
     * @param imgBash64 照片转bash64格式
     * @return
     */
    public Map<String,Double> verifyUser(String imgBash64, AipFace client) throws JSONException {
        // 传入可选参数调用接口
        HashMap<String, String> options = new HashMap<String, String>();
        JSONObject res = client.search(imgBash64, "BASE64", "user_01", options);
        JSONObject user = (JSONObject) res.getJSONObject("result").getJSONArray("user_list").get(0);
        System.out.println(res);

        String username = user.get("user_id").toString();
        Double score = (Double) user.get("score");

        Map<String,Double> map = new HashMap<>();
        map.put(username,score);
        return map;
    }

    /**
     *
     * @Title: GenerateImage
     * @Description: 该方法的主要作用：// 对字节数组字符串进行Base64解码并生成图片,并上传至服务器
     * @param  @param imgStr
     * @param  @param imgFilePath
     * @param  @return 设定文件
     * @return  返回类型：boolean
     * @throws
     */
    public boolean saveLocalImage(String imgStr, File file) {
        // 图像数据为空
        if (imgStr == null) {
            return false;
        }else {
            Base64.Decoder decoder = Base64.getDecoder();
            try {
                byte[] bytes = decoder.decode(imgStr);
                for (int i = 0; i < bytes.length; ++i) {
                    if (bytes[i] < 0) {
                        bytes[i] += 256;
                    }
                }
                // 生成jpeg图片
                if(!file.exists()) {
                    file.getParentFile().mkdir();
                    OutputStream out = new FileOutputStream(file);
                    out.write(bytes);
                    out.flush();
                    out.close();
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    /**
     * @Title: facesetAddUser
     * @Description: 该方法的主要作用：人脸注册,给人脸库中注册一个人脸
     * @param  @param client 设定文件
     * @return  返回类型：void
     * @throws
     */
    public String faceSetAddUser(AipFace client, String faceBase, String username) throws JSONException {
        // 参数为数据库中注册的人脸
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("user_info", "user's info");
        JSONObject res = client.addUser(faceBase, "BASE64", "user_01", username, options);
        System.out.println("register:" + res);
        //		System.out.println(res.toString(2));
        return res.getJSONObject("result").get("face_token").toString();
    }

}

