package org.musi.AI4Education.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSON;
import org.json.JSONException;
import org.json.JSONObject;
import org.musi.AI4Education.common.CommonResponse;
import org.musi.AI4Education.config.OCRConfig;
import org.musi.AI4Education.config.WenxinConfig;
import org.musi.AI4Education.domain.BasicQuestion;
import org.musi.AI4Education.domain.ConcreteQuestion;
import org.musi.AI4Education.domain.History;
import org.musi.AI4Education.domain.QuestionStep;
import org.musi.AI4Education.mapper.HistoryMapper;
import org.musi.AI4Education.service.BasicQuestionService;
import org.musi.AI4Education.service.ConcreteQuestionService;
import org.musi.AI4Education.service.HistoryService;
import org.musi.AI4Education.service.OSSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.musi.AI4Education.config.OCRConfig.latexOcr;

@RestController
@RequestMapping("/student")
public class QuestionController {

    @Autowired
    private BasicQuestionService basicQuestionService;
    @Autowired
    private ConcreteQuestionService concreteQuestionService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private OSSService ossService;


    @PostMapping("/bigModel")
//    public CommonResponse<JSON> createQuestion(@RequestParam String filePath) throws IOException, JSONException {
    public CommonResponse<String> createQuestion(MultipartFile file) throws Exception {

            if (StpUtil.isLogin()) {

            //将图片传输到阿里云OSS，并返回存储的URL
            String url = ossService.uploadFile(file);

            //调用图像识别OCR，返回Latex字符串
            String formatted_latex_output = latexOcr(file);

            JSONObject ocrObject = new JSONObject(formatted_latex_output);
            JSONObject res = ocrObject.getJSONObject("res");

            //获得题干文本信息
            String content = res.getString("latex");

            System.out.println(content);

            //根据文本信息，大模型生成答案
            JSON answerJSON=concreteQuestionService.useWenxinToGetAnswer(content);
            JSON explanationJSON=concreteQuestionService.useWenxinToGetExplanation(content);
            JSON stepsJSON=concreteQuestionService.useWenxinToGetSteps(content);

            JSONObject answerJSONObject= new JSONObject(String.valueOf(answerJSON));
            JSONObject explanationJSONObject= new JSONObject(String.valueOf(explanationJSON));
            JSONObject stepsJSONObject= new JSONObject(String.valueOf(stepsJSON));


            String answer = answerJSONObject.getString("result");
            String explanation = explanationJSONObject.getString("result");
            String steps = stepsJSONObject.getString("result");


            //存储错题概要信息
            BasicQuestion basicQuestion = new BasicQuestion();

            String sid =StpUtil.getLoginIdAsString();
            basicQuestion.setSid(sid);

            String qid = String.valueOf(System.currentTimeMillis());
            basicQuestion.setQid(qid);

            Date currentDate = new Date();
            basicQuestion.setDate(currentDate);

            basicQuestion.setSubject("数学");


            basicQuestion.setQuestionType("选择题");
            basicQuestion.setWrongType("计算错误");
            basicQuestion.setWrongDetails("正负值错误");

            basicQuestion.setMark(0);
            basicQuestion.setPath(url);
            basicQuestionService.createBasicQuestion(basicQuestion);


            //存储错题详细信息
            ConcreteQuestion concreteQuestion = new ConcreteQuestion();

            concreteQuestion.setQid(qid);

            //易错点
            concreteQuestion.setInspiration("负负得正");
            //题目文本
            concreteQuestion.setQuestionText(content);

            //存储题目答案
            concreteQuestion.setQuestionAnswer(answer);
            //存储题目解析
            concreteQuestion.setQuestionAnalysis(explanation);

            ArrayList<QuestionStep> questionStepList = new ArrayList<QuestionStep>();

            String stepsNew = steps+"\n";
            // 使用正则表达式匹配模式
            Pattern pattern = Pattern.compile("\n(\\d+)\\.(.*?)\n", Pattern.DOTALL);
            Matcher matcher = pattern.matcher(stepsNew);

            // 存储匹配结果
            List<String> results = new ArrayList<>();
            while (matcher.find()) {
                String match = matcher.group(2).trim(); // 提取第二个组的内容并去除两端空白
                results.add(match);
            }
            int step = 0;
            // 打印结果
            for (String result : results) {
                QuestionStep questionStep = new QuestionStep();
                questionStep.setNumber(step+1);
                questionStep.setContent(result);
                questionStepList.add(questionStep);
            }

            concreteQuestion.setQuestionSteps(questionStepList);
            concreteQuestionService.createConcreteQuestion(concreteQuestion);

            History history = new History();
            String hid = String.valueOf(System.currentTimeMillis());
            history.setSid(sid);
            history.setHid(hid);
            history.setQid(qid);
            history.setTime(currentDate);
            history.setType("计算错误");
            history.setDetails("正负值错误");

            historyService.createHistory(history);

            return CommonResponse.creatForSuccess(stepsNew);
        } else {
            // 令牌无效或解码错误
            return CommonResponse.creatForError("请先登录");
        }
    }

    @GetMapping("/question/base/basicQuestion")
    public CommonResponse<BasicQuestion> getBasicQuestionByQid(@RequestBody BasicQuestion basicQuestion){
        if (StpUtil.isLogin()){
            return CommonResponse.creatForSuccess(basicQuestionService.getBasicQuestionByQid(basicQuestion));
        }else{
            return CommonResponse.creatForError("请先登录");
        }
    }

    @GetMapping("/question/concrete")
    public CommonResponse<ConcreteQuestion> getConcreteQuestionByQid(@RequestBody ConcreteQuestion concreteQuestion){
        if (StpUtil.isLogin()){
            return CommonResponse.creatForSuccess(concreteQuestionService.getConcreteQuestionByQid(concreteQuestion));
        }else{
            return CommonResponse.creatForError("请先登录");
        }
    }



    @GetMapping("/question/base")
    public CommonResponse<List<BasicQuestion>> getBasicQuestionList(){
        if (StpUtil.isLogin()){
            return CommonResponse.creatForSuccess(basicQuestionService.getBasicQuestionList());
        }else{
            return CommonResponse.creatForError("请先登录");
        }
    }
    @GetMapping("/question/base/order")
    public CommonResponse<List<BasicQuestion>> getBasicQuestionListInOrder(){
        if (StpUtil.isLogin()){
            return CommonResponse.creatForSuccess(basicQuestionService.getBasicQuestionListInOrder());
        }else{
            return CommonResponse.creatForError("请先登录");
        }
    }
    @GetMapping("/question/base/mark")
    public CommonResponse<List<BasicQuestion>> getMarkedBasicQuestionList(){
        if (StpUtil.isLogin()){
            return CommonResponse.creatForSuccess(basicQuestionService.getMarkedBasicQuestionList());
        }else{
            return CommonResponse.creatForError("请先登录");
        }
    }



    @PostMapping("/question/mark")
    public CommonResponse<BasicQuestion> addQuestionMark(@RequestBody BasicQuestion basicQuestion){
        if(StpUtil.isLogin()){
            return CommonResponse.creatForSuccess(basicQuestionService.addQuestionMark(basicQuestion));
        }else{
            return CommonResponse.creatForError("请先登录");
        }
    }

    @DeleteMapping("/question/mark")
    public CommonResponse<BasicQuestion> deleteQuestionMark(@RequestBody BasicQuestion basicQuestion){
        if(StpUtil.isLogin()){
            return CommonResponse.creatForSuccess(basicQuestionService.deleteQuestionMark(basicQuestion));
        }else{
            return CommonResponse.creatForError("请先登录");
        }
    }

    @GetMapping("/question/position")
    public CommonResponse<BasicQuestion> getQuestionPosition(@RequestBody BasicQuestion basicQuestion){
        if(StpUtil.isLogin()){
            return CommonResponse.creatForSuccess(basicQuestionService.getQuestionPosition(basicQuestion));
        }else{
            return CommonResponse.creatForError("请先登录");
        }
    }

    @PostMapping("/question/position")
    public CommonResponse<BasicQuestion> addQuestionPosition(@RequestBody BasicQuestion basicQuestion){
        if(StpUtil.isLogin()){
            return CommonResponse.creatForSuccess(basicQuestionService.addQuestionPosition(basicQuestion));
        }else{
            return CommonResponse.creatForError("请先登录");
        }
    }

    @PutMapping("/question/position")
    public CommonResponse<BasicQuestion> modifyQuestionPosition(@RequestBody BasicQuestion basicQuestion){
        if(StpUtil.isLogin()){
            return CommonResponse.creatForSuccess(basicQuestionService.modifyQuestionPosition(basicQuestion));
        }else{
            return CommonResponse.creatForError("请先登录");
        }
    }
}
