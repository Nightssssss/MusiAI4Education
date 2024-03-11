package org.musi.AI4Education.controller;

import org.json.JSONException;
import org.musi.AI4Education.service.ChatGPTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/student")
public class ChatGPTController {

    @Autowired
    private ChatGPTService chatGPTservice;

    @GetMapping("/chat")
    public List<HashMap<String,String>>  getInformation(@RequestParam String question,@RequestParam String qid) throws JSONException {
        List<HashMap<String,String>> result = chatGPTservice.connectWithChatGPT(question,qid);
        return result;
    }

}
