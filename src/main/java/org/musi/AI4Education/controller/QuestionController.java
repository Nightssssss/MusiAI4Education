package org.musi.AI4Education.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;
import org.musi.AI4Education.common.CommonResponse;
import org.musi.AI4Education.domain.*;
import org.musi.AI4Education.service.BasicQuestionService;
import org.musi.AI4Education.service.ConcreteQuestionService;
import org.musi.AI4Education.service.HistoryService;
import org.musi.AI4Education.service.OSSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
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
    public CommonResponse<Map<String, Object>> createQuestion(MultipartFile question,MultipartFile wrongAnswer) throws Exception {

            if (StpUtil.isLogin()) {

            //将图片传输到阿里云OSS，并返回存储的URL
            String url = ossService.uploadFile(question);

            //调用图像识别OCR，返回Latex字符串
            String formatted_latex_output = latexOcr(question);

            JSONObject ocrObject = new JSONObject(formatted_latex_output);
            JSONObject res = ocrObject.getJSONObject("res");

            //获得题干文本信息
            String content = res.getString("latex");

            System.out.println(content);

            //根据文本信息，大模型生成答案
            JSON answerJSON=concreteQuestionService.useWenxinToGetAnswer(content);
            JSON explanationJSON=concreteQuestionService.useWenxinToGetExplanation(content);
            JSON stepsJSON=concreteQuestionService.useWenxinToGetSteps(content);
            List<String> knowledges = concreteQuestionService.useWenxinToAnalyseKnowledge(content);

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
            basicQuestion.setQuestionText(content);

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
            //存储题目知识点
            concreteQuestion.setKnowledges(knowledges);
            concreteQuestion.setNote("");

            ArrayList<QuestionStep> questionStepList = new ArrayList<QuestionStep>();

            String stepsNew = steps+"\n";

            int firstNewlineIndex = stepsNew.indexOf('\n');
            if (firstNewlineIndex != -1) {
                stepsNew = stepsNew.substring(0, firstNewlineIndex) + stepsNew.substring(firstNewlineIndex + 1);
            }
            System.out.println(stepsNew);

            // 使用正则表达式匹配模式
            Pattern pattern = Pattern.compile("\\n(.*?)\\n", Pattern.DOTALL);
            Matcher matcher = pattern.matcher(stepsNew);

            // 存储匹配结果
            List<String> results = new ArrayList<>();
            while (matcher.find()) {
                String match = matcher.group(1).trim(); // 提取第一个组的内容并去除两端空白
                results.add(match);
            }
            int step = 1;
            // 打印结果
            for (String result : results) {
                QuestionStep questionStep = new QuestionStep();
                questionStep.setNumber(step++);
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

            Map<String, Object> data = new HashMap<>();
            data.put("basicQuestion",basicQuestion);
            data.put("concreteQuestion",concreteQuestion);

            return CommonResponse.creatForSuccess(data);
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

    @GetMapping("/question/wrongAnswer")
    public CommonResponse<JSON> createWrongAnswerByQuestion(@RequestParam String question) throws IOException {
        if(StpUtil.isLogin()){
            JSON result = concreteQuestionService.useWenxinToCreateWrongAnswer(question);
            return CommonResponse.creatForSuccess(result);
        }else{
            return CommonResponse.creatForError("请先登录");
        }
    }

    @GetMapping("/question/analyse")
    public CommonResponse<JSON> getWrongTypeByQuestion(MultipartFile question,MultipartFile wrongAnswer) throws IOException {
        if(StpUtil.isLogin()){
            String question_latex = latexOcr(question);
            String wrongAnswer_latex = latexOcr(wrongAnswer);
            System.out.println(question_latex);
            System.out.println(wrongAnswer_latex);
            JSON result = concreteQuestionService.useWenxinToAnalyseWrongType(question_latex,wrongAnswer_latex);
            return CommonResponse.creatForSuccess(result);
        }else{
            return CommonResponse.creatForError("请先登录");
        }
    }

    @PostMapping("/question/communication")
    public CommonResponse<List<HashMap<String,String>>> communicateWithWenxin(@RequestBody Map<String,Object> map) throws IOException, JSONException {
        if(StpUtil.isLogin()){
            Object basicQuestion =  map.get("basicQuestion");
            String json = JSONUtil.toJsonStr(basicQuestion);
            ObjectMapper objectMapper = new ObjectMapper();
            BasicQuestion basicQuestion1 = objectMapper.readValue(json, new TypeReference<BasicQuestion>() {});
            String content  = (String) map.get("content");

            List<HashMap<String,String>> result = concreteQuestionService.useWenxinToCommunicateWithUser(basicQuestion1,content);
            return CommonResponse.creatForSuccess(result);
        }else{
            return CommonResponse.creatForError("请先登录");
        }
    }

    @GetMapping("/question/communication")
    public CommonResponse<ChatHistory> getChatHistroyByQid(@RequestParam String qid) throws IOException {
        if(StpUtil.isLogin()){
            ChatHistory chatHistory = concreteQuestionService.getChatHistoryByQid(qid);
            return CommonResponse.creatForSuccess(chatHistory);
        }else{
            return CommonResponse.creatForError("请先登录");
        }
    }

    @GetMapping("/question/stepInfo")
    public CommonResponse<String> getQuestionAnalysisStepInfo(@RequestParam String qid,@RequestParam int number) throws IOException, JSONException {
        if(StpUtil.isLogin()){
            String result = concreteQuestionService.getQuestionStepByQuestionNumber(qid,number);
            return CommonResponse.creatForSuccess(result);
        }else{
            return CommonResponse.creatForError("请先登录");
        }
    }

    @GetMapping("/question/knowledges")
    public CommonResponse<List<String>> getQuestionKnowledges(@RequestParam String qid) throws IOException, JSONException {
        if(StpUtil.isLogin()){
            List<String> resultTemp = concreteQuestionService.getQuestionKnowledgesByQid(qid);
            List<String> result = new ArrayList<>();
            result.add("请生成这个题的答案");
            result.add("请生成这个题的解析");
            result.add("这个问题与我们之前学过的哪些知识相关");
            result.add("我可以用哪些方法来解决这个问题");
            result.add("这个题目有什么实际应用吗");
            for(String temp : resultTemp){
                temp = "如何理解"+temp;
                result.add(temp);
            }
            return CommonResponse.creatForSuccess(result);
        }else{
            return CommonResponse.creatForError("请先登录");
        }
    }

    @PostMapping("/question/note")
    public CommonResponse<String> uploadQuestionNoteByQid(@RequestParam String qid,@RequestParam String note) throws IOException, JSONException {
        if(StpUtil.isLogin()){
            String result = concreteQuestionService.uploadQuestionNotesByQid(qid,note);
            return CommonResponse.creatForSuccess(result);
        }else{
            return CommonResponse.creatForError("请先登录");
        }
    }

    @GetMapping("/question/note")
    public CommonResponse<String> getQuestionNoteByQid(@RequestParam String qid) throws IOException, JSONException {
        if(StpUtil.isLogin()){
            ConcreteQuestion result = concreteQuestionService.getQuestionNotesByQid(qid);
            return CommonResponse.creatForSuccess(result.getNote());
        }else{
            return CommonResponse.creatForError("请先登录");
        }
    }

    @PutMapping("/question/note")
    public CommonResponse<String> modifyQuestionNoteByQid(@RequestParam String qid,@RequestParam String note) throws IOException, JSONException {
        if(StpUtil.isLogin()){
            String result = concreteQuestionService.modifyQuestionNotesByQid(qid,note);
            return CommonResponse.creatForSuccess(result);
        }else{
            return CommonResponse.creatForError("请先登录");
        }
    }

    @DeleteMapping("/question/note")
    public CommonResponse<String> deleteQuestionNoteByQid(@RequestParam String qid) throws IOException, JSONException {
        if(StpUtil.isLogin()){
            String result = concreteQuestionService.deleteQuestionNotesByQid(qid);
            return CommonResponse.creatForSuccess(result);
        }else{
            return CommonResponse.creatForError("请先登录");
        }
    }

    @GetMapping("/question/position/details")
    public CommonResponse<List<HashMap<String,Object>>> getQuestionInfoByPosition(@RequestParam String position){
        List<HashMap<String,Object>> result = basicQuestionService.getQuestionInfoByPosition(position);
        return CommonResponse.creatForSuccess(result);
    }

    @GetMapping("/question/position/all")
    public CommonResponse<List<String>> getPositionsByUid(){
        List<String> result = basicQuestionService.getPositionsByUid();
        return CommonResponse.creatForSuccess(result);
    }

    @DeleteMapping("/question/position/all")
    public CommonResponse<String> deleteQuestion_PositionsByPosition(@RequestParam String position){
        basicQuestionService.deleteQuestion_PositionsByPosition(position);
        return CommonResponse.creatForSuccess("删除成功");
    }



}
