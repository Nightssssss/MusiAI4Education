package org.musi.AI4Education.controller;

import cn.dev33.satoken.stp.StpUtil;
import org.musi.AI4Education.common.CommonResponse;
import org.musi.AI4Education.domain.BasicQuestion;
import org.musi.AI4Education.domain.ConcreteQuestion;
import org.musi.AI4Education.domain.History;
import org.musi.AI4Education.domain.QuestionStep;
import org.musi.AI4Education.mapper.HistoryMapper;
import org.musi.AI4Education.service.BasicQuestionService;
import org.musi.AI4Education.service.ConcreteQuestionService;
import org.musi.AI4Education.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;

@RestController
@RequestMapping("/student")
public class QuestionController {

    @Autowired
    private BasicQuestionService basicQuestionService;

    @Autowired
    private ConcreteQuestionService concreteQuestionService;

    @Autowired
    private HistoryService historyService;

    @PostMapping("/bigModel")
    public CommonResponse<String> createQuestion(@RequestParam String context,@RequestParam String filePath) {
        if (StpUtil.isLogin()) {
            //获得题干文本信息
            System.out.println("题干文本信息");
            System.out.println(context);
            //获得题目图片
            System.out.println(filePath);
            //根据文本与图片信息，大模型生成答案

            //存储错题概要信息
            BasicQuestion basicQuestion = new BasicQuestion();
            String sid =StpUtil.getLoginIdAsString();
            basicQuestion.setSid(sid);
            String qid = String.valueOf(System.currentTimeMillis());
            basicQuestion.setQid(qid);
            basicQuestion.setQuestionType("选择题");
            Date currentDate = new Date();
            basicQuestion.setDate(currentDate);
            basicQuestion.setSubject("数学");
            basicQuestion.setWrongType("计算错误");
            basicQuestion.setWrongDetails("正负值错误");
            basicQuestion.setMark(0);
            basicQuestion.setPosition("");

            basicQuestionService.createBasicQuestion(basicQuestion);

            //存储错题详细信息
            ConcreteQuestion concreteQuestion = new ConcreteQuestion();

            concreteQuestion.setQid(qid);
            concreteQuestion.setInspiration("负负得正");
            concreteQuestion.setQuestionText(context);

            ArrayList<QuestionStep> questionStepList = new ArrayList<QuestionStep>();

            QuestionStep questionStep1 = new QuestionStep();
            questionStep1.setNumber(1);
            questionStep1.setContent("first step reason");

            QuestionStep questionStep2 = new QuestionStep();
            questionStep2.setNumber(2);
            questionStep2.setContent("second step reason");

            QuestionStep questionStep3 = new QuestionStep();
            questionStep3.setNumber(3);
            questionStep3.setContent("third step reason");

            questionStepList.add(questionStep1);
            questionStepList.add(questionStep2);
            questionStepList.add(questionStep3);

            concreteQuestion.setQuestionSteps(questionStepList);
            concreteQuestion.setQuestionAnalysis("这道题很简单");

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

            return CommonResponse.creatForSuccess("插入成功");
        } else {
            // 令牌无效或解码错误
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
