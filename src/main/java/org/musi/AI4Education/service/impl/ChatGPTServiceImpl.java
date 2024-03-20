package org.musi.AI4Education.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.json.JSONException;
import org.musi.AI4Education.domain.ChatHistory;
import org.musi.AI4Education.domain.ChatSession;
import org.musi.AI4Education.mapper.ChatHistoryMapper;
import org.musi.AI4Education.service.ChatGPTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;


@Service
public class ChatGPTServiceImpl extends ServiceImpl<ChatHistoryMapper, ChatHistory> implements ChatGPTService {

    @Autowired
    private MongoTemplate mongoTemplate;

    private Map<String, ChatSession> sessions = new HashMap<>(); // Store sessions using user IDs

    @Override
    public String getChatWavForInspiration(String question,String chatHistory) {
        StringBuilder answer = new StringBuilder();
        try {
            ProcessBuilder pb;
            if (question.equals("")) {

//                pb = new ProcessBuilder("/root/miniconda3/bin/python3.12", "/MusiProject/Python_API/WenxinPlusGPT4/main.py");
                pb = new ProcessBuilder("G:\\connectChatGPT\\venv\\Scripts\\python.exe", "G:\\green-farm\\src\\main\\java\\Python_API\\WenxinPlusGPT4\\main.py");
            } else {
                Gson gson = new Gson();
                String jsonInput = gson.toJson(chatHistory);
//                pb = new ProcessBuilder("/root/miniconda3/bin/python3.12", "/MusiProject/Python_API/WenxinPlusGPT4/main.py",question,jsonInput);

                pb = new ProcessBuilder("G:\\connectChatGPT\\venv\\Scripts\\python.exe", "G:\\green-farm\\src\\main\\java\\Python_API\\WenxinPlusGPT4\\main.py",question,jsonInput);
            }
            Process p = pb.start();
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream(), "gb2312"));
            BufferedReader err = new BufferedReader(new InputStreamReader(p.getErrorStream(), "gb2312"));

            String line;
            while ((line = in.readLine()) != null) {
                answer.append(line).append("\n");
            }
            while ((line = err.readLine()) != null) {
                answer.append(line).append("\n");
            }
            in.close();
            err.close();
            p.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return answer.toString();
    }
    @Override
    public List<HashMap<String,String>> connectWithChatGPTForinspiration(String question, String qid) throws JSONException {


        //获取用户ID与题目ID
        String sid = StpUtil.getLoginIdAsString();

        // 直接尝试获取会话对象
        ChatSession session = sessions.get(qid);
        String chatHistoryTemp = "";

        // 如果获取的会话对象为空
        if (session == null) {

            // 说明这是第一次创建,创建新的会话对象
            session = new ChatSession();

        }else{

            // 说明这是不是第一次创建,获取之前与大模型的会话历史，并传给Python程序
            List<HashMap<String, String>> messages2 = session.getMessages();
            List<HashMap<String,String>> chatHistory = new ArrayList<>();
            for (HashMap<String, String> message : messages2) {
                String role1 = message.get("role");
                String content1 = message.get("content");
                System.out.println(role1 + ": " + content1);
                HashMap<String,String> temp = new HashMap<>();
                temp.put("role",role1);
                temp.put("content",content1);
                chatHistory.add(temp);
            }

            ObjectMapper objectMapper = new ObjectMapper();
            try {
                chatHistoryTemp = objectMapper.writeValueAsString(chatHistory);
                System.out.println("发送给Python程序的JSON数据: " + chatHistoryTemp);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        //获取大模型输出的答案
        String answer = getChatWavForInspiration(question,chatHistoryTemp);
        System.out.println(answer);


        //提取聊天记录部分
        int startIndex = answer.indexOf("[");
        int endIndex = answer.lastIndexOf("]");
        String chatString = "";


        // 提取两个[]之间的内容
        if (startIndex != -1 && endIndex != -1 && startIndex < endIndex) {
            chatString = answer.substring(startIndex + 1, endIndex);
            System.out.println("提取的内容: " + chatString);
        } else {
            System.out.println("未找到匹配的内容");
        }
        String result = "[" + chatString + "]" ;


        //存储并输出result（将HashMap的形式存储到MongoDB）
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // 将 JSON 字符串解析为 JsonNode 对象
            JsonNode jsonNode = objectMapper.readTree(result);

            // 遍历 JsonNode 对象并将每个 JSON 对象添加到列表中
            List<JsonNode> jsonObjects = new ArrayList<>();
            Iterator<JsonNode> elements = jsonNode.elements();
            while (elements.hasNext()) {
                JsonNode element = elements.next();
                jsonObjects.add(element);
            }
            //清空之前的session，避免重复添加
            session.clearMessages();
            // 打印结果
            for (JsonNode jsonObject : jsonObjects) {
                String role = jsonObject.get("role").asText();
                String content = jsonObject.get("content").asText();

                System.out.println("Role: " + role);
                System.out.println("Content: " + content);

                HashMap<String,String> temp = new HashMap<>();
                temp.put("role",role);
                temp.put("content",content);

                //存储进入后端项目 会话Session
                session.addMessage(temp);
                sessions.put(qid, session);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        //打印
        ChatSession session2 = sessions.getOrDefault(qid, new ChatSession());
        List<HashMap<String, String>> messages2 = session2.getMessages();
        List<HashMap<String,String>> chatHistoryTemp1 = new ArrayList<>();
        for (HashMap<String, String> message : messages2) {
            String role1 = message.get("role");
            String content1 = message.get("content");
            System.out.println(role1 + ": " + content1);
            HashMap<String,String> temp = new HashMap<>();
            temp.put(role1,content1);
            chatHistoryTemp1.add(temp);
        }

        Criteria criteria1 = Criteria.where("sid").is(sid);
        Criteria criteria2 = Criteria.where("qid").is(qid);

        // 组合多个查询条件
        Criteria criteria = new Criteria().andOperator(criteria1, criteria2);

        // 创建查询对象
        Query query = new Query(criteria);

        List<ChatHistory> result2 = mongoTemplate.find(query, ChatHistory.class);

        if (result2.isEmpty()) {
            System.out.println("查询结果为空");

            ChatHistory chatHistory = new ChatHistory();
            chatHistory.setQid(qid);
            chatHistory.setSid(sid);
            chatHistory.setWenxinChatHistory(chatHistoryTemp1);

            mongoTemplate.insert(chatHistory);
        } else {
            System.out.println("查询结果不为空");
            Update update = new Update().set("wenxinChatHistory",chatHistoryTemp1);
            mongoTemplate.updateFirst(query, update, "chatHistory");
        }
        return chatHistoryTemp1;

    }

    @Override
    public String getChatWavForExplanation(String question, String chatHistory,String studentCharactor) {
        StringBuilder answer = new StringBuilder();
        try {
            ProcessBuilder pb;
            if (question.equals("")) {

//                pb = new ProcessBuilder("/root/miniconda3/bin/python3.12", "/MusiProject/Python_API/PersonalExplanation/main.py");
                pb = new ProcessBuilder("G:\\connectChatGPT\\venv\\Scripts\\python.exe", "G:\\green-farm\\src\\main\\java\\Python_API\\PersonalExplanation\\main.py");
            } else {
                Gson gson = new Gson();
                String jsonInput = gson.toJson(chatHistory);
//                pb = new ProcessBuilder("/root/miniconda3/bin/python3.12", "/MusiProject/Python_API/PersonalExplanation/main.py",question,jsonInput);

                pb = new ProcessBuilder("G:\\connectChatGPT\\venv\\Scripts\\python.exe", "G:\\green-farm\\src\\main\\java\\Python_API\\PersonalExplanation\\main.py",question,jsonInput,studentCharactor);
            }
            Process p = pb.start();
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream(), "gb2312"));
            BufferedReader err = new BufferedReader(new InputStreamReader(p.getErrorStream(), "gb2312"));

            String line;
            while ((line = in.readLine()) != null) {
                answer.append(line).append("\n");
            }
            while ((line = err.readLine()) != null) {
                answer.append(line).append("\n");
            }
            in.close();
            err.close();
            p.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return answer.toString();
    }

    @Override
    public List<HashMap<String, String>> connectWithChatGPTForExplanation(String question, String qid,String studentCharactor) throws JSONException {
        //获取用户ID与题目ID
        String sid = StpUtil.getLoginIdAsString();

        // 直接尝试获取会话对象
        ChatSession session = sessions.get(qid);
        String chatHistoryTemp = "";

        // 如果获取的会话对象为空
        if (session == null) {

            // 说明这是第一次创建,创建新的会话对象
            session = new ChatSession();

        }else{

            // 说明这是不是第一次创建,获取之前与大模型的会话历史，并传给Python程序
            List<HashMap<String, String>> messages2 = session.getMessages();
            List<HashMap<String,String>> chatHistory = new ArrayList<>();
            for (HashMap<String, String> message : messages2) {
                String role1 = message.get("role");
                String content1 = message.get("content");
                System.out.println(role1 + ": " + content1);
                HashMap<String,String> temp = new HashMap<>();
                temp.put("role",role1);
                temp.put("content",content1);
                chatHistory.add(temp);
            }

            ObjectMapper objectMapper = new ObjectMapper();
            try {
                chatHistoryTemp = objectMapper.writeValueAsString(chatHistory);
                System.out.println("发送给Python程序的JSON数据: " + chatHistoryTemp);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        //获取大模型输出的答案
        String answer = getChatWavForExplanation(question,chatHistoryTemp,studentCharactor);
        System.out.println(answer);


        //提取聊天记录部分
        int startIndex = answer.indexOf("[");
        int endIndex = answer.lastIndexOf("]");
        String chatString = "";


        // 提取两个[]之间的内容
        if (startIndex != -1 && endIndex != -1 && startIndex < endIndex) {
            chatString = answer.substring(startIndex + 1, endIndex);
            System.out.println("提取的内容: " + chatString);
        } else {
            System.out.println("未找到匹配的内容");
        }
        String result = "[" + chatString + "]" ;


        //存储并输出result（将HashMap的形式存储到MongoDB）
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // 将 JSON 字符串解析为 JsonNode 对象
            JsonNode jsonNode = objectMapper.readTree(result);

            // 遍历 JsonNode 对象并将每个 JSON 对象添加到列表中
            List<JsonNode> jsonObjects = new ArrayList<>();
            Iterator<JsonNode> elements = jsonNode.elements();
            while (elements.hasNext()) {
                JsonNode element = elements.next();
                jsonObjects.add(element);
            }
            //清空之前的session，避免重复添加
            session.clearMessages();
            // 打印结果
            for (JsonNode jsonObject : jsonObjects) {
                String role = jsonObject.get("role").asText();
                String content = jsonObject.get("content").asText();

                System.out.println("Role: " + role);
                System.out.println("Content: " + content);

                HashMap<String,String> temp = new HashMap<>();
                temp.put("role",role);
                temp.put("content",content);

                //存储进入后端项目 会话Session
                session.addMessage(temp);
                sessions.put(qid, session);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        //打印
        ChatSession session2 = sessions.getOrDefault(qid, new ChatSession());
        List<HashMap<String, String>> messages2 = session2.getMessages();
        List<HashMap<String,String>> chatHistoryTemp1 = new ArrayList<>();
        for (HashMap<String, String> message : messages2) {
            String role1 = message.get("role");
            String content1 = message.get("content");
            System.out.println(role1 + ": " + content1);
            HashMap<String,String> temp = new HashMap<>();
            temp.put(role1,content1);
            chatHistoryTemp1.add(temp);
        }

        Criteria criteria1 = Criteria.where("sid").is(sid);
        Criteria criteria2 = Criteria.where("qid").is(qid);

        // 组合多个查询条件
        Criteria criteria = new Criteria().andOperator(criteria1, criteria2);

        // 创建查询对象
        Query query = new Query(criteria);

        List<ChatHistory> result2 = mongoTemplate.find(query, ChatHistory.class);

        if (result2.isEmpty()) {
            System.out.println("查询结果为空");

            ChatHistory chatHistory = new ChatHistory();
            chatHistory.setQid(qid);
            chatHistory.setSid(sid);
            chatHistory.setWenxinChatHistory(chatHistoryTemp1);

            mongoTemplate.insert(chatHistory);
        } else {
            System.out.println("查询结果不为空");
            Update update = new Update().set("wenxinChatHistory",chatHistoryTemp1);
            mongoTemplate.updateFirst(query, update, "chatHistory");
        }
        return chatHistoryTemp1;

    }

    @Override
    public String getChatWavForFeiman(String question, String chatHistory) {
        StringBuilder answer = new StringBuilder();
        try {
            ProcessBuilder pb;
            if (chatHistory.equals("")) {

//                pb = new ProcessBuilder("/root/miniconda3/bin/python3.12", "/MusiProject/Python_API/PersonalExplanation/main.py",question);
                pb = new ProcessBuilder("G:\\connectChatGPT\\venv\\Scripts\\python.exe", "G:\\green-farm\\src\\main\\java\\Python_API\\FeimanLearningMethod\\main.py",question);
            } else {
                Gson gson = new Gson();
                String jsonInput = gson.toJson(chatHistory);
//                pb = new ProcessBuilder("/root/miniconda3/bin/python3.12", "/MusiProject/Python_API/PersonalExplanation/main.py",question,jsonInput);

                pb = new ProcessBuilder("G:\\connectChatGPT\\venv\\Scripts\\python.exe", "G:\\green-farm\\src\\main\\java\\Python_API\\FeimanLearningMethod\\main.py",question,jsonInput);
            }
            Process p = pb.start();
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream(), "gb2312"));
            BufferedReader err = new BufferedReader(new InputStreamReader(p.getErrorStream(), "gb2312"));

            String line;
            while ((line = in.readLine()) != null) {
                answer.append(line).append("\n");
            }
            while ((line = err.readLine()) != null) {
                answer.append(line).append("\n");
            }
            in.close();
            err.close();
            p.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return answer.toString();
    }

    @Override
    public List<HashMap<String, String>> connectWithChatGPTForFeiman(String question, String qid) throws JSONException {
        //获取用户ID与题目ID
        String sid = StpUtil.getLoginIdAsString();

        // 直接尝试获取会话对象
        ChatSession session = sessions.get(qid);
        String chatHistoryTemp = "";

        // 如果获取的会话对象为空
        if (session == null) {

            // 说明这是第一次创建,创建新的会话对象
            session = new ChatSession();

        }else{

            // 说明这是不是第一次创建,获取之前与大模型的会话历史，并传给Python程序
            List<HashMap<String, String>> messages2 = session.getMessages();
            List<HashMap<String,String>> chatHistory = new ArrayList<>();
            for (HashMap<String, String> message : messages2) {
                String role1 = message.get("role");
                String content1 = message.get("content");
                System.out.println(role1 + ": " + content1);
                HashMap<String,String> temp = new HashMap<>();
                temp.put("role",role1);
                temp.put("content",content1);
                chatHistory.add(temp);
            }

            ObjectMapper objectMapper = new ObjectMapper();
            try {
                chatHistoryTemp = objectMapper.writeValueAsString(chatHistory);
                System.out.println("发送给Python程序的JSON数据: " + chatHistoryTemp);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        //获取大模型输出的答案
        String answer = getChatWavForFeiman(question,chatHistoryTemp);
        System.out.println(answer);


        //提取聊天记录部分
        int startIndex = answer.indexOf("[");
        int endIndex = answer.lastIndexOf("]");
        String chatString = "";


        // 提取两个[]之间的内容
        if (startIndex != -1 && endIndex != -1 && startIndex < endIndex) {
            chatString = answer.substring(startIndex + 1, endIndex);
            System.out.println("提取的内容: " + chatString);
        } else {
            System.out.println("未找到匹配的内容");
        }
        String result = "[" + chatString + "]" ;


        //存储并输出result（将HashMap的形式存储到MongoDB）
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // 将 JSON 字符串解析为 JsonNode 对象
            JsonNode jsonNode = objectMapper.readTree(result);

            // 遍历 JsonNode 对象并将每个 JSON 对象添加到列表中
            List<JsonNode> jsonObjects = new ArrayList<>();
            Iterator<JsonNode> elements = jsonNode.elements();
            while (elements.hasNext()) {
                JsonNode element = elements.next();
                jsonObjects.add(element);
            }
            //清空之前的session，避免重复添加
            session.clearMessages();
            // 打印结果
            for (JsonNode jsonObject : jsonObjects) {
                String role = jsonObject.get("role").asText();
                String content = jsonObject.get("content").asText();

                System.out.println("Role: " + role);
                System.out.println("Content: " + content);

                HashMap<String,String> temp = new HashMap<>();
                temp.put("role",role);
                temp.put("content",content);

                //存储进入后端项目 会话Session
                session.addMessage(temp);
                sessions.put(qid, session);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        //打印
        ChatSession session2 = sessions.getOrDefault(qid, new ChatSession());
        List<HashMap<String, String>> messages2 = session2.getMessages();
        List<HashMap<String,String>> chatHistoryTemp1 = new ArrayList<>();
        for (HashMap<String, String> message : messages2) {
            String role1 = message.get("role");
            String content1 = message.get("content");
            System.out.println(role1 + ": " + content1);
            HashMap<String,String> temp = new HashMap<>();
            temp.put(role1,content1);
            chatHistoryTemp1.add(temp);
        }

        Criteria criteria1 = Criteria.where("sid").is(sid);
        Criteria criteria2 = Criteria.where("qid").is(qid);

        // 组合多个查询条件
        Criteria criteria = new Criteria().andOperator(criteria1, criteria2);

        // 创建查询对象
        Query query = new Query(criteria);

        List<ChatHistory> result2 = mongoTemplate.find(query, ChatHistory.class);

        if (result2.isEmpty()) {
            System.out.println("查询结果为空");

            ChatHistory chatHistory = new ChatHistory();
            chatHistory.setQid(qid);
            chatHistory.setSid(sid);
            chatHistory.setWenxinChatHistory(chatHistoryTemp1);

            mongoTemplate.insert(chatHistory);
        } else {
            System.out.println("查询结果不为空");
            Update update = new Update().set("wenxinChatHistory",chatHistoryTemp1);
            mongoTemplate.updateFirst(query, update, "chatHistory");
        }
        return chatHistoryTemp1;
    }
}
