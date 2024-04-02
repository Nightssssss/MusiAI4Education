package org.musi.AI4Education.controller;

import cn.dev33.satoken.stp.StpUtil;
import org.json.JSONException;
import org.musi.AI4Education.common.CommonResponse;
import org.musi.AI4Education.domain.*;
import org.musi.AI4Education.service.ChatGPTService;
import org.musi.AI4Education.service.OSSService;
import org.musi.AI4Education.service.StudentProfileService;
import org.musi.AI4Education.service.impl.GptServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/student")
public class ChatGPTController {

    @Resource
    GptServiceImpl gptService;
    @Autowired
    private ChatGPTService chatGPTservice;
    @Autowired
    private StudentProfileService studentProfileService;
    @Autowired
    private OSSService ossService;


    @GetMapping(value = "/chat/inspiration", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<AIAnswerDTO>> getChatGPTForInspirationStream(@RequestParam String content, @RequestParam String qid) {
        System.out.println("Question："+content);
        return gptService.doChatGPTStreamForInspiration(qid,content)
                .map(aiAnswerDTO -> ServerSentEvent.<AIAnswerDTO>builder()//进行结果的封装，再返回给前端
                        .data(aiAnswerDTO)
                        .build()
                )
                .onErrorResume(e -> Flux.empty());
    }

    @GetMapping(value = "/chat/inspiration/audio", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<AIAnswerDTO>> getChatGPTForInspirationStreamByAudio(MultipartFile file, @RequestParam String qid) {

        String url = ossService.uploadPCMFileAndReturnName(file);
        System.out.println("图片存储路径："+url);
        String content = chatGPTservice.getTextByPcm(url);
        System.out.println("语音识别内容：" + content);

        System.out.println("Question："+content);
        return gptService.doChatGPTStreamForInspiration(qid,content)
                .map(aiAnswerDTO -> ServerSentEvent.<AIAnswerDTO>builder()//进行结果的封装，再返回给前端
                        .data(aiAnswerDTO)
                        .build()
                )
                .onErrorResume(e -> Flux.empty());
    }

    @GetMapping(value = "/chat/explanation", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<AIAnswerDTO>> getChatGPTForExplanationStream(@RequestParam String content,@RequestParam String qid) throws JSONException {

//        String sid = StpUtil.getLoginIdAsString();
        String sid = "1707103528830";
        List<String> result1 = studentProfileService.getStudentTopWrongTypeAndDetails(sid);
        String studentCharactor = result1.get(0)+"中的"+result1.get(1);

        System.out.println("Question："+content);
        return gptService.doChatGPTStreamForExplanation(qid,content,studentCharactor)
                .map(aiAnswerDTO -> ServerSentEvent.<AIAnswerDTO>builder()//进行结果的封装，再返回给前端
                        .data(aiAnswerDTO)
                        .build()
                )
                .onErrorResume(e -> Flux.empty());

    }

    @GetMapping(value = "/chat/explanation/audio", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<AIAnswerDTO>> getChatGPTForExplanationStreamByAudio(MultipartFile file,@RequestParam String qid) throws JSONException {

//        String sid = StpUtil.getLoginIdAsString();
        String sid = "1707103528830";
        List<String> result1 = studentProfileService.getStudentTopWrongTypeAndDetails(sid);
        String studentCharactor = result1.get(0)+"中的"+result1.get(1);

        String url = ossService.uploadPCMFileAndReturnName(file);
        System.out.println("图片存储路径："+url);
        String content = chatGPTservice.getTextByPcm(url);
        System.out.println("语音识别内容：" + content);

        System.out.println("Question："+content);
        return gptService.doChatGPTStreamForExplanation(qid,content,studentCharactor)
                .map(aiAnswerDTO -> ServerSentEvent.<AIAnswerDTO>builder()//进行结果的封装，再返回给前端
                        .data(aiAnswerDTO)
                        .build()
                )
                .onErrorResume(e -> Flux.empty());

    }


    @GetMapping("/chat/feiman")
    public Flux<ServerSentEvent<AIAnswerDTO>> getChatGPTForFeimanStream(@RequestParam String content,@RequestParam String qid) throws JSONException {

        System.out.println("Question："+content);
        return gptService.doChatGPTStreamForFeiman(qid,content)
                .map(aiAnswerDTO -> ServerSentEvent.<AIAnswerDTO>builder()//进行结果的封装，再返回给前端
                        .data(aiAnswerDTO)
                        .build()
                )
                .onErrorResume(e -> Flux.empty());
    }

    @GetMapping("/chat/feiman/audio")
    public Flux<ServerSentEvent<AIAnswerDTO>> getChatGPTForFeimanStreamByAudio(MultipartFile file,@RequestParam String qid) throws JSONException {

        String url = ossService.uploadPCMFileAndReturnName(file);
        System.out.println("图片存储路径："+url);
        String content = chatGPTservice.getTextByPcm(url);
        System.out.println("语音识别内容：" + content);


        System.out.println("Question："+content);
        return gptService.doChatGPTStreamForFeiman(qid,content)
                .map(aiAnswerDTO -> ServerSentEvent.<AIAnswerDTO>builder()//进行结果的封装，再返回给前端
                        .data(aiAnswerDTO)
                        .build()
                )
                .onErrorResume(e -> Flux.empty());
    }






    @GetMapping("/chat/inspiration/history")
    public CommonResponse<InspirationChatHistory> getInspirationChatHistroyByQid(@RequestParam String qid) throws IOException {
        if(StpUtil.isLogin()){
            InspirationChatHistory chatHistory = chatGPTservice.getInspirationChatHistoryByQid(qid);
            System.out.println(chatHistory);
            return CommonResponse.creatForSuccess(chatHistory);
        }else{
            return CommonResponse.creatForError("请先登录");
        }
    }

    @GetMapping("/chat/explanation/history")
    public CommonResponse<ExplanationChatHistory> getExplanationChatHistroyByQid(@RequestParam String qid) throws IOException {
        if(StpUtil.isLogin()){
            ExplanationChatHistory chatHistory = chatGPTservice.getExplanationChatHistoryByQid(qid);
            System.out.println(chatHistory);
            return CommonResponse.creatForSuccess(chatHistory);
        }else{
            return CommonResponse.creatForError("请先登录");
        }
    }

    @GetMapping("/chat/feiman/history")
    public CommonResponse<FeimanChatHistory> getFeimanChatHistroyByQid(@RequestParam String qid) throws IOException {
        if(StpUtil.isLogin()){
            FeimanChatHistory chatHistory = chatGPTservice.getFeimanChatHistoryByQid(qid);
            System.out.println(chatHistory);
            return CommonResponse.creatForSuccess(chatHistory);
        }else{
            return CommonResponse.creatForError("请先登录");
        }
    }

}
