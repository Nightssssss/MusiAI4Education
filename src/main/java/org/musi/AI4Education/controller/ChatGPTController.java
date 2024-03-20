package org.musi.AI4Education.controller;

import cn.dev33.satoken.stp.StpUtil;
import org.json.JSONException;
import org.musi.AI4Education.service.ChatGPTService;
import org.musi.AI4Education.service.StudentProfileService;
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

    @Autowired
    private StudentProfileService studentProfileService;

    @GetMapping("/chat/inspiration")
    public List<HashMap<String,String>>  connectWithChatGPTForInspiration(@RequestParam String question,@RequestParam String qid) throws JSONException {
        List<HashMap<String,String>> result = chatGPTservice.connectWithChatGPTForinspiration(question,qid);
        return result;
    }

    @GetMapping("/chat/explanation")
    public List<HashMap<String,String>>  connectWithChatGPTForExplanation(@RequestParam String question,@RequestParam String qid) throws JSONException {
        String sid = StpUtil.getLoginIdAsString();
        List<String> result1 = studentProfileService.getStudentTopWrongTypeAndDetails(sid);
        String studentCharactor = result1.get(0)+"中的"+result1.get(1);
        List<HashMap<String,String>> result = chatGPTservice.connectWithChatGPTForExplanation(question,qid,studentCharactor);
        return result;
    }

    @GetMapping("/chat/feiman")
    public List<HashMap<String,String>>  connectWithChatGPTForFeiman(@RequestParam String question,@RequestParam String qid) throws JSONException {
        List<HashMap<String,String>> result = chatGPTservice.connectWithChatGPTForFeiman(question,qid);
        return result;
    }

}
