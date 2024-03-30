package org.musi.AI4Education.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.json.JSONException;
import org.musi.AI4Education.domain.ChatHistory;
import org.musi.AI4Education.domain.ExplanationChatHistory;
import org.musi.AI4Education.domain.FeimanChatHistory;
import org.musi.AI4Education.domain.InspirationChatHistory;

import java.util.HashMap;
import java.util.List;

public interface ChatGPTService extends IService<ChatHistory> {
    public String getChatWavForInspiration(String question, String chatHistory,String questionText, String wrongtext) ;
    public List<HashMap<String,String>> connectWithChatGPTForinspiration(String question,String qid) throws JSONException;
    public String getChatWavForExplanation(String questionText,String question, String chatHistory,String studentCharactor) ;
    public List<HashMap<String,String>> connectWithChatGPTForExplanation(String question,String qid,String studentCharactor) throws JSONException;
    public String getChatWavForFeiman(String question, String chatHistory,String questionText) ;
    public List<HashMap<String,String>> connectWithChatGPTForFeiman(String question,String qid) throws JSONException;

    public InspirationChatHistory getInspirationChatHistoryByQid(String qid);

    public ExplanationChatHistory getExplanationChatHistoryByQid(String qid);

    public FeimanChatHistory getFeimanChatHistoryByQid(String qid);

    public String getTextByPcm(String filePath);

    public Boolean getWavByText(String text);

}
