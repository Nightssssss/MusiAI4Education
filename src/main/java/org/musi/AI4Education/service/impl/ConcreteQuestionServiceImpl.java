package org.musi.AI4Education.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.bson.Document;
import org.json.JSONException;
import org.json.JSONObject;
import org.musi.AI4Education.common.CommonResponse;
import org.musi.AI4Education.config.Wen_XinConfig;
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
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
@Slf4j
public class ConcreteQuestionServiceImpl extends ServiceImpl<ConcreteQuestionMapper, ConcreteQuestion> implements ConcreteQuestionService {

    private Map<String, ChatSession> sessions = new HashMap<>(); // Store sessions using user IDs
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private BasicQuestionService basicQuestionService;
    @Autowired
    private StudentService studentService;

    @Resource
    private Wen_XinConfig wenXinConfig;
    //历史对话，需要按照user,assistant
    List<Map<String,String>> messages = new ArrayList<>();

    private final ConcurrentMap<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

    List<Map<String,String>> listMessage = new ArrayList<>();
    private static final WebClient WEB_CLIENT = WebClient.builder().baseUrl("https://aip.baidubce.com").defaultHeader(HttpHeaders.CONTENT_TYPE, org.springframework.http.MediaType.APPLICATION_STREAM_JSON_VALUE).build();


    /**
     * 构造请求的请求参数
     * @param userId
     * @param temperature
     * @param topP
     * @param penaltyScore
     * @param messages
     * @return
     */
    @Override
    public String constructRequestJson(Integer userId,
                                       Double temperature,
                                       Double topP,
                                       Double penaltyScore,
                                       boolean stream,
                                       List<Map<String, String>> messages) {
        Map<String,Object> request = new HashMap<>();
        request.put("user_id",userId.toString());
        request.put("temperature",temperature);
        request.put("top_p",topP);
        request.put("penalty_score",penaltyScore);
        request.put("stream",stream);
        request.put("messages",messages);
        System.out.println(JSON.toJSONString(request));
        return JSON.toJSONString(request);
    }

    @Override
    public String useWenxinStreamTransformToGetAnswerAndExplanationAndKnowledge(String question) throws IOException {
        question = "我将会传输带有latex公式的数学题目，只需要给出(1)题目的标准答案(2)题目的简略解析(3)题目考察的与数学相关的知识点，分别用[]括起来，例如“[答案：2],[解析：1+1=2],[{知识点1的具体内容},{知识点2的具体内容}]，注意解题内容中不要包含‘[]’”,下面是题目： "+question;
        return connectWithBigModelStreamTransition(question);
    }

    @Override
    public String useWenxinStreamTransformToGetSteps(String question) throws IOException {

        String sid = StpUtil.getLoginIdAsString();
        String description = studentService.getStudentBySid(sid).getDescription();
        question = "我将会提供带有 LaTeX 公式的数学题目，"+"，只需要给出题目的解题步骤。" + "每一个单独的解题步骤都要用[]中括号括起来表示，如[1.步骤一...],[2.步骤2...]以此类推，"+description+"下面是题目："+question;
        return connectWithBigModelStreamTransition(question);

    }

    @Override
    public List<String> useWenxinStreamTransformToAnalyseWrongType(String question, String content) throws IOException, JSONException {
        String sid = StpUtil.getLoginIdAsString();
        String description = studentService.getStudentBySid(sid).getDescription();

        String require =
                "我需要你分析学生在解决数学问题时生成错误解题步骤的错误类型,该学生情况为："+description+"，请结合学生的个人情况，并结合学生的错误答案，以教师的口吻，重复一下学生情况，并设计一个教学方案" +
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
                        "请分析学生所犯的错误类型，并且只返回一个基本类型 与 一个细分类型,与一份教学方案，不要过多解释！\n" +
                        "基本类型：用[]括起来 "+
                        "细分类型：用[]括起来 "+
                        "教学方案：用[]括起来 ";


        String stringWithAnswer = connectWithBigModelStreamTransition(require);
        System.out.println(stringWithAnswer);

        List<String> resultList = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\[(.*?)\\]");
        Matcher matcher = pattern.matcher(stringWithAnswer);

        while (matcher.find()) {
            resultList.add(matcher.group(1));
        }
        return resultList;
    }

    @Override
    public Flux<String> useWenxinStreamTransformToCommunicateWithUser(String qid, String content) throws IOException, JSONException {

        //获得学生基本信息
//        String sid = StpUtil.getLoginIdAsString();
        String sid = "1707103528830";
        String qidForChatHistory = qid+"001";
        BasicQuestion basicQuestion = new BasicQuestion();
        basicQuestion.setQid(qid);
        String question = String.valueOf(basicQuestionService.getQuestionTextByQid(basicQuestion));


        // 直接尝试获取会话对象
        ChatSession session = sessions.get(qidForChatHistory);

        if (session == null) { // 如果获取的会话对象为空
            // 说明这是第一次创建
            session = new ChatSession(); // 创建新的会话对象
            sessions.put(qidForChatHistory, session); // 将新的会话对象放入 sessions 中
            // 在这里可以执行第一次创建会话的相关逻辑
            String front = "我将提供一个含有LaTex公式的数学题目，请根据这个题目回答下列问题，题目为：";
            content =  front+question+" 请回答我的提问："+content;
        }


        //设置请求体 这一部分可以放到Service
        HashMap<String, String> user = new HashMap<>();
        user.put("role","user");
        user.put("content",content);
        listMessage.add(user);
        String requestJson = constructRequestJson(1,0.95,0.8,1.0,true,listMessage);

        //进行数据访问 返回String类型的数据
        StringBuffer answer=new StringBuffer();
        return WEB_CLIENT.post().uri(wenXinConfig.ERNIE_Bot_4_0_URL + "?access_token=" + wenXinConfig.flushAccessToken())
                .bodyValue(requestJson)
                .retrieve()
                .bodyToFlux(String.class)
                // 可能需要其他流处理，比如map、filter等
                .flatMap(data -> {
                    String result = JSON.parseObject(data).getString("result");
                    //终结符会对后续的传输造成影响
                    result = result.replace("\n", " ");
                    System.out.println(result);
                    answer.append(result);
                    return Mono.just(result); // 使用Mono.just将结果包装为一个发布者
                }).doOnComplete(() -> {
                    // 当Flux完成时，输出结束消息
                    System.out.println("处理完毕，流已关闭。");
                    System.out.println(answer);
                    //将回复的内容添加到消息中
                    HashMap<String, String> assistant = new HashMap<>();
                    assistant.put("role","assistant");
                    assistant.put("content",answer.toString());
                    listMessage.add(assistant);

                    //将数据存入MongoDB数据库

                    // 组合多个查询条件，并在MongoDB中查询
                    Criteria criteria1 = Criteria.where("sid").is(sid);
                    Criteria criteria2 = Criteria.where("qid").is(qidForChatHistory);
                    Criteria criteria = new Criteria().andOperator(criteria1, criteria2);
                    Query query = new Query(criteria);
                    List<ChatHistory> result = mongoTemplate.find(query, ChatHistory.class);


                    List<HashMap<String,String>> chatHistoryTemp = new ArrayList<>();
                    //存入MongoDB
                    //获取当前该用户的该题的聊天记录
                    for (Map<String, String> message : listMessage) {
                        String role1 = message.get("role");
                        String content1 = message.get("content");
                        HashMap<String,String> temp = new HashMap<>();
                        temp.put(role1,content1);
                        chatHistoryTemp.add(temp);
                    }

                    if (result.isEmpty()) {
                        System.out.println("查询结果为空");
                        ChatHistory chatHistory = new ChatHistory();
                        chatHistory.setQid(qidForChatHistory);
                        chatHistory.setSid(sid);
                        chatHistory.setWenxinChatHistory(chatHistoryTemp);
                        mongoTemplate.insert(chatHistory);
                    } else {
                        System.out.println("查询结果不为空");
                        Update update = new Update().set("wenxinChatHistory",chatHistoryTemp);
                        mongoTemplate.updateFirst(query, update, "chatHistory");
                    }

                }).log();
    }

    @Override
    public Flux<String> useWenxinStreamTransformToCommunicateWithUserWithWrongAnswer(String qid, String wrongText, String wrongReason, String content) throws IOException, JSONException {

        //获取用户ID与题目ID
        String sid = StpUtil.getLoginIdAsString();

        String qidForChatHistory = qid+"002";
        BasicQuestion basicQuestion = new BasicQuestion();
        basicQuestion.setQid(qid);
        String question = String.valueOf(basicQuestionService.getQuestionTextByQid(basicQuestion));


        // 直接尝试获取会话对象
        ChatSession session = sessions.get(qidForChatHistory);

        if (session == null) { // 如果获取的会话对象为空
            // 说明这是第一次创建
            session = new ChatSession(); // 创建新的会话对象
            sessions.put(qidForChatHistory, session); // 将新的会话对象放入 sessions 中
            // 在这里可以执行第一次创建会话的相关逻辑
            String front = "我将提供一个含有LaTex公式的数学题目，并提供我的有错误的解题思路，以及该我犯错误的原因.\n";
            content =  front+"题目为："+question+"  我的错解为"+wrongText+"  我的错因为："+wrongReason+ " 请结合我犯错误的原因，以教师的口吻分析我犯错的地方在哪";
        }

        //设置请求体 这一部分可以放到Service
        HashMap<String, String> user = new HashMap<>();
        user.put("role","user");
        user.put("content",content);
        listMessage.add(user);
        String requestJson = constructRequestJson(1,0.95,0.8,1.0,true,listMessage);

        //进行数据访问 返回String类型的数据
        StringBuffer answer=new StringBuffer();
        return WEB_CLIENT.post().uri(wenXinConfig.ERNIE_Bot_4_0_URL + "?access_token=" + wenXinConfig.flushAccessToken())
                .bodyValue(requestJson)
                .retrieve()
                .bodyToFlux(String.class)
                // 可能需要其他流处理，比如map、filter等
                .flatMap(data -> {
                    String result = JSON.parseObject(data).getString("result");
                    //终结符会对后续的传输造成影响
                    result = result.replace("\n", " ");
                    System.out.println(result);
                    answer.append(result);
                    return Mono.just(result); // 使用Mono.just将结果包装为一个发布者
                }).doOnComplete(() -> {
                    // 当Flux完成时，输出结束消息
                    System.out.println("处理完毕，流已关闭。");
                    System.out.println(answer);
                    //将回复的内容添加到消息中
                    HashMap<String, String> assistant = new HashMap<>();
                    assistant.put("role","assistant");
                    assistant.put("content",answer.toString());
                    listMessage.add(assistant);

                    //将数据存入MongoDB数据库

                    // 组合多个查询条件，并在MongoDB中查询
                    Criteria criteria1 = Criteria.where("sid").is(sid);
                    Criteria criteria2 = Criteria.where("qid").is(qidForChatHistory);
                    Criteria criteria = new Criteria().andOperator(criteria1, criteria2);
                    Query query = new Query(criteria);
                    List<WrongReasonChatHistory> result = mongoTemplate.find(query, WrongReasonChatHistory.class);


                    List<HashMap<String,String>> chatHistoryTemp = new ArrayList<>();
                    //存入MongoDB
                    //获取当前该用户的该题的聊天记录
                    for (Map<String, String> message : listMessage) {
                        String role1 = message.get("role");
                        String content1 = message.get("content");
                        HashMap<String,String> temp = new HashMap<>();
                        temp.put(role1,content1);
                        chatHistoryTemp.add(temp);
                    }

                    if (result.isEmpty()) {
                        System.out.println("查询结果为空");
                        WrongReasonChatHistory chatHistory = new WrongReasonChatHistory();
                        chatHistory.setQid(qidForChatHistory);
                        chatHistory.setSid(sid);
                        chatHistory.setWenxinChatHistory(chatHistoryTemp);
                        mongoTemplate.insert(chatHistory);
                    } else {
                        System.out.println("查询结果不为空");
                        Update update = new Update().set("wenxinChatHistory",chatHistoryTemp);
                        mongoTemplate.updateFirst(query, update, "wrongReasonChatHistory");
                    }

                }).log();
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
    public ChatHistory getChatHistoryByQid(String qid) throws IOException {
        Criteria criteria1 = Criteria.where("sid").is(StpUtil.getLoginIdAsString());
        Criteria criteria2 = Criteria.where("qid").is(qid+"001");
        // 组合多个查询条件
        Criteria criteria = new Criteria().andOperator(criteria1, criteria2);
        // 创建查询对象
        Query query = new Query(criteria);
        ChatHistory result = mongoTemplate.findOne(query, ChatHistory.class);
        return result;
    }

    @Override
    public WrongReasonChatHistory getWrongAnswerChatHistoryByQid(String qid) throws IOException {
        Criteria criteria1 = Criteria.where("sid").is(StpUtil.getLoginIdAsString());
        Criteria criteria2 = Criteria.where("qid").is(qid+"002");
        // 组合多个查询条件
        Criteria criteria = new Criteria().andOperator(criteria1, criteria2);
        // 创建查询对象
        Query query = new Query(criteria);
        WrongReasonChatHistory result = mongoTemplate.findOne(query, WrongReasonChatHistory.class);
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
        Pattern pattern = Pattern.compile("\\[([^\\[\\]]*)\\]"); // 匹配被"[]"括起来的内容
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
    public String connectWithBigModelStreamTransition(String question) throws IOException {
        OkHttpClient client = new OkHttpClient();

        HashMap<String, String> user = new HashMap<>();
        user.put("role","user");
        user.put("content",question);
        messages.add(user);
        String requestJson = constructRequestJson(1,0.95,0.8,1.0,true,messages);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), requestJson);
        Request request = new Request.Builder()
                .url(wenXinConfig.ERNIE_Bot_4_0_URL + "?access_token=" + wenXinConfig.flushAccessToken())
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();

        StringBuilder answer = new StringBuilder();
        // 发起异步请求
        try {
            Response response = client.newCall(request).execute();
            // 检查响应是否成功
            if (response.isSuccessful()) {
                // 获取响应流
                try (ResponseBody responseBody = response.body()) {
                    if (responseBody != null) {
                        InputStream inputStream = responseBody.byteStream();
                        // 以流的方式处理响应内容，输出到控制台
                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            // 在控制台输出每个数据块
                            System.out.write(buffer, 0, bytesRead);
                            //将结果汇总起来
                            answer.append(new String(buffer, 0, bytesRead));
                        }
                    }
                }
            } else {
                System.out.println("Unexpected code " + response);
            }

        } catch (IOException e) {
            log.error("流式请求出错");
            throw new RuntimeException(e);
        }
        //将回复的内容添加到消息中
        HashMap<String, String> assistant = new HashMap<>();
        assistant.put("role","assistant");
        assistant.put("content","");
        //取出我们需要的内容,也就是result部分
        String[] answerArray = answer.toString().split("data: ");
        for (int i=1;i<answerArray.length;++i) {
            answerArray[i] = answerArray[i].substring(0,answerArray[i].length() - 2);
            assistant.put("content",assistant.get("content") + JSON.parseObject(answerArray[i]).get("result"));
        }
        messages.add(assistant);
        return assistant.get("content");
    }

    @Override
    public String test(String content) throws IOException {

        Long qid = Long.valueOf("123456"+"001");
        SseEmitter emitter = new SseEmitter();
        emitters.put(qid, emitter);

        System.out.println(content);

        OkHttpClient client = new OkHttpClient();

        HashMap<String, String> user = new HashMap<>();
        user.put("role","user");
        user.put("content",content);
        messages.add(user);
        String requestJson = constructRequestJson(1,0.95,0.8,1.0,true,messages);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), requestJson);
        Request request = new Request.Builder()
                .url(wenXinConfig.ERNIE_Bot_4_0_URL + "?access_token=" + wenXinConfig.flushAccessToken())
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();

        StringBuilder answer = new StringBuilder();
        // 发起异步请求
        try {
            Response response = client.newCall(request).execute();
            // 检查响应是否成功
            if (response.isSuccessful()) {
                // 获取响应流
                try (ResponseBody responseBody = response.body()) {
                    if (responseBody != null) {
                        InputStream inputStream = responseBody.byteStream();
                        // 以流的方式处理响应内容，输出到控制台
                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            // 在控制台输出每个数据块
                            System.out.write(buffer, 0, bytesRead);
                            //将结果汇总起来
                            answer.append(new String(buffer, 0, bytesRead));
                        }
                    }
                }
            } else {
                System.out.println("Unexpected code " + response);
            }

        } catch (IOException e) {
            log.error("流式请求出错");
            throw new RuntimeException(e);
        }
        //将回复的内容添加到消息中
        HashMap<String, String> assistant = new HashMap<>();
        assistant.put("role","assistant");
        assistant.put("content","");
        //取出我们需要的内容,也就是result部分
        String[] answerArray = answer.toString().split("data: ");
        for (int i=1;i<answerArray.length;++i) {
            answerArray[i] = answerArray[i].substring(0,answerArray[i].length() - 2);
            assistant.put("content",assistant.get("content") + JSON.parseObject(answerArray[i]).get("result"));
            sendDataToClient(qid,assistant.get("content"));
        }
        messages.add(assistant);


        emitter.onCompletion(() -> emitters.remove(emitter.hashCode()));
        emitter.onTimeout(() -> emitters.remove(emitter.hashCode()));

        return assistant.get("content");
    }

    @Override
    public void sendDataToClient(Long clientId, String data) {

        SseEmitter emitter = emitters.get(clientId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().data(data));
            } catch (IOException e) {
                emitter.complete();
                emitters.remove(clientId);
            }
        }
    }

}
