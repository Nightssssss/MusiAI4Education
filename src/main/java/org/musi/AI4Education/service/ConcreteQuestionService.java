package org.musi.AI4Education.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.IService;
import org.json.JSONException;
import org.musi.AI4Education.common.CommonResponse;
import org.musi.AI4Education.domain.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface ConcreteQuestionService extends IService<ConcreteQuestion> {

    //调用大模型生成答案
    public JSON useWenxinToGetAnswerAndExplanation(String content) throws IOException;
    public JSON useWenxinToGetSteps(String content) throws IOException;
    public JSON useWenxinToCreateWrongAnswer(String content) throws IOException;
    public List<String> useWenxinToAnalyseWrongType(String question,String content) throws IOException, JSONException;
    public List<HashMap<String,String>> useWenxinToCommunicateWithUser(BasicQuestion basicQuestion, String content) throws IOException, JSONException;

    public List<HashMap<String,String>> useWenxinToCommunicateWithUserWithWrongAnswer(BasicQuestion basicQuestion, String wrongText, String wrongReason,String content) throws IOException, JSONException;


    public String getQuestionStepByQuestionNumber(String qid,int number);

    public List<String> getQuestionKnowledgesByQid(String qid);

    public String uploadQuestionNotesByQid(String qid,String note);

    public ConcreteQuestion getQuestionNotesByQid(String qid);

    public String modifyQuestionNotesByQid(String qid,String note);

    public String deleteQuestionNotesByQid(String qid);

    public JSON connectWithBigModel(String content) throws IOException;

    public ChatHistory getChatHistoryByQid(String qid) throws IOException;

    public WrongReasonChatHistory getWrongAnswerChatHistoryByQid(String qid) throws IOException;

    public CommonResponse<String> createConcreteQuestion(ConcreteQuestion concreteQuestion);

    public ArrayList<QuestionStep> createQuestionSteps(String steps);

    public List<String> splitAnswerAndExplanation(String steps);

    public List<String> splitKnowledges(String knowledges);


    //查询单个错题的详细信息
    public ConcreteQuestion getConcreteQuestionByQid(ConcreteQuestion concreteQuestion);
}
