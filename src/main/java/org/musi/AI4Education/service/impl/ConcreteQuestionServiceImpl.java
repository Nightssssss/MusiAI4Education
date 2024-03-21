package org.musi.AI4Education.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.bson.Document;
import org.json.JSONException;
import org.json.JSONObject;
import org.musi.AI4Education.common.CommonResponse;
import org.musi.AI4Education.config.WenxinConfig;
import org.musi.AI4Education.domain.*;
import org.musi.AI4Education.mapper.ConcreteQuestionMapper;
import org.musi.AI4Education.service.BasicQuestionService;
import org.musi.AI4Education.service.ConcreteQuestionService;
import org.musi.AI4Education.service.StudentService;
import org.musi.AI4Education.utils.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
public class ConcreteQuestionServiceImpl extends ServiceImpl<ConcreteQuestionMapper, ConcreteQuestion> implements ConcreteQuestionService {

    private Map<String, ChatSession> sessions = new HashMap<>(); // Store sessions using user IDs
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private BasicQuestionService basicQuestionService;
    @Autowired
    private StudentService studentService;

    @Override
    public JSON useWenxinToGetAnswerAndExplanation(String content) throws IOException {
        return connectWithBigModel("我将会传输带有latex公式的数学题目，只需要给出题目的答案与题目解析与题目考察的与数学相关的知识点，分别用[]括起来，例如[题目答案],[题目解析],[{知识点1的具体内容},{知识点2的具体内容}],回答稍微简洁但不遗漏关键步骤，下面是题目： "+content);
    }

    @Override
    public JSON useWenxinToGetSteps(String content) throws IOException {
        String sid = StpUtil.getLoginIdAsString();
        String description = studentService.getStudentBySid(sid).getDescription();
        String require = "我将会提供带有 LaTeX 公式的数学题目，"+description+"，只需要给出题目的解题步骤。" + "每一个步骤都用[]括起来表示，如[1. 步骤一的内容],[2. 步骤二的内容]以此类推，下面是题目："+content;
        return connectWithBigModel(require);
    }
    @Override
    public JSON useWenxinToCreateWrongAnswer(String content) throws IOException {

        String wrongType="运算错误";
        String wrongDetail="忽略负负得正";

        String require = "我需要你模拟学生在解决数学问题时生成错误的回答。你将扮演一个中学生，他在数学学习中遇到了一些挑战。你在回答数学问题时需要根据给定的学生个性档案生成错误回答。\n" +
                "学生个性档案：\n" +
                "常见错误类型: "+wrongType+ "\n" +
                "错误模式: "+wrongDetail+ "\n" +
                "我需要你：第一步，给出正确答案。" +
                "第二步，生成错误回答，要确保答案是错误的，并且这种错误符合上述学生个性档案的特点。\n" +
                "下面是你需要生成错误答案的问题:"+content+
                "请先生成正确答案，之后把错误回答以解题步骤的形式写出来，以正式解题的口吻，你只需要写解题步骤，不要分析\n" +
                "正确答案："+
                "解题步骤：";

        return connectWithBigModel(require);
    }

    @Override
    public List<String> useWenxinToAnalyseWrongType(String question,String content) throws IOException, JSONException {
        String require =
                "我需要你分析学生在解决数学问题时生成错误解题步骤的错误类型。" +
                "你需要提在下列范围内，寻找一个（只有一个）最为接近的基本类型与一个（只有一个）细分类型\n" +

                "基本类型列表为：" +
                "{计算错误、概念错误、读题错误、解题错误}\n" +

                "对应的细分类型为："+
                "[{\"计算错误\":\"{代数,正负值错误,单位转换,值错误}\"},"+
                " {\"概念错误\":\"{理解错误,抄写题目信息疏忽}\"},"+
                " {\"读题错误\":\"{方程设立错误,错误格式,概念遗忘}\"},"+
                " {\"解题错误\":\"{解题步骤不完整,结论错误,猜答案}\"}]"+

                "如果没有与之匹配的的基本类型与细分类型，请新建一个基本类型与细分类型！\n"+

                "下面是原始问题:"+question+
                "下面是学生提供的错误解题步骤:"+content+
                "请分析学生所犯的错误类型，并且只返回一个基本类型 与 一个细分类型，不要过多解释！\n" +
                "基本类型：用[]括起来 "+
                "细分类型：用[]括起来 ";

        JSON result = connectWithBigModel(require);

        JSONObject resultJSON= new JSONObject(String.valueOf(result));
        String stringWithAnswer = resultJSON.getString("result");

        List<String> resultList = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\[(.*?)\\]");
        Matcher matcher = pattern.matcher(stringWithAnswer);

        while (matcher.find()) {
            resultList.add(matcher.group(1));
        }
        return resultList;
    }

    @Override
    public List<HashMap<String,String>> useWenxinToCommunicateWithUser(BasicQuestion basicQuestion, String content) throws IOException, JSONException {

        String question = String.valueOf(basicQuestionService.getQuestionTextByQid(basicQuestion));
        String access_token = new WenxinConfig().getWenxinToken();
        String requestMethod = "POST";
        String url = "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/completions_pro?access_token=" + access_token;

        //获取用户ID与题目ID
        String sid = StpUtil.getLoginIdAsString();
        String qid = basicQuestion.getQid();

        // 直接尝试获取会话对象
        ChatSession session = sessions.get(qid);

        if (session == null) { // 如果获取的会话对象为空
            // 说明这是第一次创建
            session = new ChatSession(); // 创建新的会话对象
            sessions.put(qid, session); // 将新的会话对象放入 sessions 中
            // 在这里可以执行第一次创建会话的相关逻辑
            String front = "我将提供一个含有LaTex公式的数学题目，请根据这个题目回答下列问题，题目为：";
            content =  front+question+" 请回答我的提问："+content;
        }

        //如果获得了会话对象，说明并不是第一次创建，则直接在Session里面添加接下来的问题
        //将用户的问题存入Session
        HashMap<String, String> msg = new HashMap<>();
        msg.put("role", "user");
        msg.put("content", content);
        session.addMessage(msg);
        sessions.put(qid, session);

        //请求大模型，获得大模型的回答
        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put("messages", session.getMessages());
        String outputStr = JSON.toJSONString(requestBody);
        JSON json = HttpRequest.httpRequest(url, requestMethod, outputStr, "application/json");

        JSONObject answerJSONObject= new JSONObject(String.valueOf(json));
        String answer = answerJSONObject.getString("result");

        //首先获取之前的Session，将大模型答案添加到Session中
        HashMap<String, String> msg1 = new HashMap<>();
        ChatSession session1 = sessions.getOrDefault(qid, new ChatSession());
        List<HashMap<String, String>> messages = session1.getMessages();
        for (HashMap<String, String> message : messages) {
            String role1 = message.get("role");
            String content1 = message.get("content");
            msg1.put(role1, content1);
        }
        msg1.put("role", "assistant");
        msg1.put("content", answer);
        session1.addMessage(msg1);
        sessions.put(qid, session1);

        //打印
        ChatSession session2 = sessions.getOrDefault(qid, new ChatSession());
        List<HashMap<String, String>> messages2 = session2.getMessages();
        List<HashMap<String,String>> chatHistoryTemp = new ArrayList<>();
        for (HashMap<String, String> message : messages2) {
            String role1 = message.get("role");
            String content1 = message.get("content");
            System.out.println(role1 + ": " + content1);
            HashMap<String,String> temp = new HashMap<>();
            temp.put(role1,content1);
            chatHistoryTemp.add(temp);
        }

        Criteria criteria1 = Criteria.where("sid").is(sid);
        Criteria criteria2 = Criteria.where("qid").is(qid);

        // 组合多个查询条件
        Criteria criteria = new Criteria().andOperator(criteria1, criteria2);

        // 创建查询对象
        Query query = new Query(criteria);

        List<ChatHistory> result = mongoTemplate.find(query, ChatHistory.class);

        if (result.isEmpty()) {
            System.out.println("查询结果为空");

            ChatHistory chatHistory = new ChatHistory();
            chatHistory.setQid(qid);
            chatHistory.setSid(sid);
            chatHistory.setWenxinChatHistory(chatHistoryTemp);

            mongoTemplate.insert(chatHistory);
        } else {
            System.out.println("查询结果不为空");
            Update update = new Update().set("wenxinChatHistory",chatHistoryTemp);
            mongoTemplate.updateFirst(query, update, "chatHistory");
        }
        return chatHistoryTemp;

    }

    @Override
    public List<HashMap<String, String>> useWenxinToCommunicateWithUserWithWrongAnswer(BasicQuestion basicQuestion,String wrongText,String wrongReason,String content) throws IOException, JSONException {
        String question = String.valueOf(basicQuestionService.getQuestionTextByQid(basicQuestion));
        String access_token = new WenxinConfig().getWenxinToken();
        String requestMethod = "POST";
        String url = "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/completions_pro?access_token=" + access_token;

        //获取用户ID与题目ID
        String sid = StpUtil.getLoginIdAsString();
        String qid = basicQuestion.getQid()+"521";

        // 直接尝试获取会话对象
        ChatSession session = sessions.get(qid);

        if (session == null) { // 如果获取的会话对象为空
            // 说明这是第一次创建
            session = new ChatSession(); // 创建新的会话对象
            sessions.put(qid, session); // 将新的会话对象放入 sessions 中
            // 在这里可以执行第一次创建会话的相关逻辑
            String front = "我将提供一个含有LaTex公式的数学题目，并提供我的有错误的解题思路，以及该我犯错误的原因.\n";
            content =  front+"题目为："+question+"  我的错解为"+wrongText+"  我的错因为："+wrongReason+ " 请结合我犯错误的原因，以教师的口吻分析我犯错的地方在哪。";
        }

        //如果获得了会话对象，说明并不是第一次创建，则直接在Session里面添加接下来的问题
        //将用户的问题存入Session
        HashMap<String, String> msg = new HashMap<>();
        msg.put("role", "user");
        msg.put("content", content);
        session.addMessage(msg);
        sessions.put(qid, session);

        //请求大模型，获得大模型的回答
        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put("messages", session.getMessages());
        String outputStr = JSON.toJSONString(requestBody);
        JSON json = HttpRequest.httpRequest(url, requestMethod, outputStr, "application/json");

        JSONObject answerJSONObject= new JSONObject(String.valueOf(json));
        String answer = answerJSONObject.getString("result");

        //首先获取之前的Session，将大模型答案添加到Session中
        HashMap<String, String> msg1 = new HashMap<>();
        ChatSession session1 = sessions.getOrDefault(qid, new ChatSession());
        List<HashMap<String, String>> messages = session1.getMessages();
        for (HashMap<String, String> message : messages) {
            String role1 = message.get("role");
            String content1 = message.get("content");
            msg1.put(role1, content1);
        }
        msg1.put("role", "assistant");
        msg1.put("content", answer);
        session1.addMessage(msg1);
        sessions.put(qid, session1);

        //打印
        ChatSession session2 = sessions.getOrDefault(qid, new ChatSession());
        List<HashMap<String, String>> messages2 = session2.getMessages();
        List<HashMap<String,String>> chatHistoryTemp = new ArrayList<>();
        for (HashMap<String, String> message : messages2) {
            String role1 = message.get("role");
            String content1 = message.get("content");
            System.out.println(role1 + ": " + content1);
            HashMap<String,String> temp = new HashMap<>();
            temp.put(role1,content1);
            chatHistoryTemp.add(temp);
        }

        Criteria criteria1 = Criteria.where("sid").is(sid);
        Criteria criteria2 = Criteria.where("qid").is(qid);

        // 组合多个查询条件
        Criteria criteria = new Criteria().andOperator(criteria1, criteria2);

        // 创建查询对象
        Query query = new Query(criteria);

        List<ChatHistory> result = mongoTemplate.find(query, ChatHistory.class);

        if (result.isEmpty()) {
            System.out.println("查询结果为空");

            ChatHistory chatHistory = new ChatHistory();
            chatHistory.setQid(qid);
            chatHistory.setSid(sid);
            chatHistory.setWenxinChatHistory(chatHistoryTemp);

            mongoTemplate.insert(chatHistory);
        } else {
            System.out.println("查询结果不为空");
            Update update = new Update().set("wenxinChatHistory",chatHistoryTemp);
            mongoTemplate.updateFirst(query, update, "chatHistory");
        }
        return chatHistoryTemp;
    }

    @Override
    public String getQuestionStepByQuestionNumber(String qid, int targetNumber) {
        // 构建查询条件，匹配指定 id 的文档
        Query query = new Query(Criteria.where("qid").is(qid));
        // 查询指定 qid 的文档
        Document document = mongoTemplate.findOne(query, Document.class, "concreteQuestion");
        // 如果找到了文档
        if (document != null) {
            // 获取 questionSteps 数组
            List<Document> questionSteps = (List<Document>) document.get("questionSteps");

            // 遍历 questionSteps 数组
            for (Document step : questionSteps) {
                // 获取当前步骤的数字
                // 如果当前步骤的数字与目标数字匹配
                int number = step.getInteger("number");
                if (number == targetNumber) {
                    // 获取当前步骤的 content 值
                    return step.getString("content");
                }
            }
        }
        // 如果未找到匹配的 content，则返回空字符串或者 null，视需求而定
        return null;

    }

    @Override
    public List<String> getQuestionKnowledgesByQid(String qid) {
        // 构建查询条件，匹配指定 id 的文档
        Query query = new Query(Criteria.where("qid").is(qid));
        // 查询指定 qid 的文档
        Document document = mongoTemplate.findOne(query, Document.class, "concreteQuestion");
        // 如果找到了文档
        if (document != null) {
            // 获取 questionSteps 数组
            List<String> questionKnowledges = (List<String>) document.get("knowledges");
            List<String> stringList = new ArrayList<>();
            // 遍历 questionSteps 数组
            for (String step : questionKnowledges) {
                stringList.add(String.valueOf(step));
                }
            return stringList;
            }
        // 如果未找到匹配的 content，则返回空字符串或者 null，视需求而定
        return null;
    }

    @Override
    public String uploadQuestionNotesByQid(String qid,String note) {
        Query query = new Query(Criteria.where("qid").is(qid));
        Update update = new Update();
        update.set("note", note);
        mongoTemplate.updateFirst(query, update, "concreteQuestion"); // 你的文档类名替换为实际的类名
        return note;
    }

    @Override
    public ConcreteQuestion getQuestionNotesByQid(String qid) {
        Query query = new Query(Criteria.where("qid").is(qid));
        ConcreteQuestion result = mongoTemplate.findOne(query, ConcreteQuestion.class); // 你的文档类名替换为实际的类名
        return result;

    }

    @Override
    public String modifyQuestionNotesByQid(String qid, String note) {
        Query query = new Query(Criteria.where("qid").is(qid));
        Update update = new Update();
        update.set("note", note);
        mongoTemplate.updateFirst(query, update, "concreteQuestion"); // 你的文档类名替换为实际的类名
        return note;
    }

    @Override
    public String deleteQuestionNotesByQid(String qid) {
        Query query = new Query(Criteria.where("qid").is(qid));
        Update update = new Update();
        update.set("note", "");
        mongoTemplate.updateFirst(query, update, "concreteQuestion"); // 你的文档类名替换为实际的类名
        return "删除成功";
    }

    @Override
    public JSON connectWithBigModel(String content) throws IOException {
        String access_token = new WenxinConfig().getWenxinToken();
        String requestMethod = "POST";
        String url = "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/completions_pro?access_token="+access_token;//post请求时格式
        HashMap<String, String> msg = new HashMap<>();
        msg.put("role","user");
        msg.put("content", content);
        ArrayList<HashMap> messages = new ArrayList<>();
        messages.add(msg);
        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put("messages", messages);
        String outputStr = JSON.toJSONString(requestBody);
        JSON json = HttpRequest.httpRequest(url,requestMethod,outputStr,"application/json");
        return json;
    }

    @Override
    public ChatHistory getChatHistoryByQid(String qid) throws IOException {
        Criteria criteria1 = Criteria.where("sid").is(StpUtil.getLoginIdAsString());
        Criteria criteria2 = Criteria.where("qid").is(qid);
        // 组合多个查询条件
        Criteria criteria = new Criteria().andOperator(criteria1, criteria2);
        // 创建查询对象
        Query query = new Query(criteria);
        ChatHistory result = mongoTemplate.findOne(query, ChatHistory.class);
        return result;
    }

    @Override
    public CommonResponse<String> createConcreteQuestion(ConcreteQuestion concreteQuestion) {
        mongoTemplate.insert(concreteQuestion);
        return CommonResponse.creatForSuccess("添加成功");
    }

    @Override
    public ArrayList<QuestionStep> createQuestionSteps(String steps) {

        ArrayList<QuestionStep> questionStepList = new ArrayList<QuestionStep>();

        ArrayList<String> contents = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\[(.*?)\\]"); // 匹配被"[]"括起来的内容
        Matcher matcher = pattern.matcher(steps);

        // 使用正则表达式逐个匹配并提取内容
        while (matcher.find()) {
            contents.add(matcher.group(1)); // group(1) 提取括号中的内容
        }
        int step = 1;
        for (String result : contents) {
            QuestionStep questionStep = new QuestionStep();
            questionStep.setNumber(step++);
            questionStep.setContent(result);
            questionStepList.add(questionStep);
        }

        return questionStepList;
    }

    @Override
    public List<String> splitAnswerAndExplanation(String steps) {
        List<String> result = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\[(.*?)\\]"); // 匹配被"[]"括起来的内容
        Matcher matcher = pattern.matcher(steps);

        // 使用正则表达式逐个匹配并提取内容
        while (matcher.find()) {
            result.add(matcher.group(1));
            System.out.println(matcher.group(1));
        }
        return result;
    }

    @Override
    public List<String> splitKnowledges(String knowledges) {
        List<String> result = new ArrayList<>();

        Pattern pattern = Pattern.compile("\\{(.*?)\\}"); // 匹配被"[]"括起来的内容
        Matcher matcher = pattern.matcher(knowledges);

        // 使用正则表达式逐个匹配并提取内容
        while (matcher.find()) {
            result.add(matcher.group(1)); // group(1) 提取括号中的内容
        }
        return result;
    }

    @Override
    public ConcreteQuestion getConcreteQuestionByQid(ConcreteQuestion concreteQuestion) {
        Query query = new Query();
        query.addCriteria(Criteria.where("qid").is(concreteQuestion.getQid()));
        ConcreteQuestion concreteQuestion1 = mongoTemplate.findOne(query, ConcreteQuestion.class);
        return concreteQuestion1;
    }

}
