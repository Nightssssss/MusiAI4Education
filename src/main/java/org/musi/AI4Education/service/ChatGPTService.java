package org.musi.AI4Education.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.json.JSONException;
import org.musi.AI4Education.domain.ChatHistory;

import java.util.HashMap;
import java.util.List;

public interface ChatGPTService extends IService<ChatHistory> {
    public String getChatWav(String question, String chatHistory) ;
    public List<HashMap<String,String>> connectWithChatGPT(String question,String qid) throws JSONException;

    }
