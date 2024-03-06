package org.musi.AI4Education.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.IService;
import org.json.JSONException;
import org.musi.AI4Education.common.CommonResponse;
import org.musi.AI4Education.domain.BasicQuestion;
import org.musi.AI4Education.domain.ConcreteQuestion;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public interface ConcreteQuestionService extends IService<ConcreteQuestion> {

    //调用大模型生成答案
    public JSON useWenxinToGetAnswer(String content) throws IOException;
    public JSON useWenxinToGetExplanation(String content) throws IOException;
    public JSON useWenxinToGetSteps(String content) throws IOException;

    public JSON useWenxinToGetWrongAnswer(String content) throws IOException;
    public JSON useWenxinToAnalyseWrongType(String question,String content) throws IOException;

    public List<HashMap<String,String>> useWenxinToCommunicateWithUser(BasicQuestion basicQuestion, String content) throws IOException, JSONException;

    public List<String> useWenxinToAnalyseKnowledge(String question) throws IOException, JSONException;

    public String getQuestionStepByQuestionNumber(String qid,int number);

    public List<String> getQuestionKnowledgesByQid(String qid);

    public String uploadQuestionNotesByQid(String qid,String note);

    public ConcreteQuestion getQuestionNotesByQid(String qid);

    public String modifyQuestionNotesByQid(String qid,String note);

    public String deleteQuestionNotesByQid(String qid);

    public CommonResponse<String> createConcreteQuestion(ConcreteQuestion concreteQuestion);

    //查询单个错题的详细信息
    public ConcreteQuestion getConcreteQuestionByQid(ConcreteQuestion concreteQuestion);
}
