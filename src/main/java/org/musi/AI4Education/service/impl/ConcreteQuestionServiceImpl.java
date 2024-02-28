package org.musi.AI4Education.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.musi.AI4Education.common.CommonResponse;
import org.musi.AI4Education.config.WenxinConfig;
import org.musi.AI4Education.domain.ConcreteQuestion;
import org.musi.AI4Education.mapper.ConcreteQuestionMapper;
import org.musi.AI4Education.service.ConcreteQuestionService;
import org.musi.AI4Education.utils.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


@Service
public class ConcreteQuestionServiceImpl extends ServiceImpl<ConcreteQuestionMapper, ConcreteQuestion> implements ConcreteQuestionService {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public JSON useWenxinToGetAnswer(String content) throws IOException {
        String access_token = new WenxinConfig().getWenxinToken();
        //2、访问数据
        String requestMethod = "POST";
//        String body = URLEncoder.encode("junshi","UTF-8");//设置要传的信息
        String url = "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/eb-instant?access_token="+access_token;//post请求时格式
        //测试：访问聚合数据的地区新闻api
        HashMap<String, String> msg = new HashMap<>();
        msg.put("role","user");
        msg.put("content", "我将会传输带有latex公式的数学题目，请只给我题目的答案 "+content);
        ArrayList<HashMap> messages = new ArrayList<>();
        messages.add(msg);
        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put("messages", messages);
        String outputStr = JSON.toJSONString(requestBody);
        JSON json = HttpRequest.httpRequest(url,requestMethod,outputStr,"application/json");
        return json;
    }

    public JSON useWenxinToGetExplanation(String content) throws IOException {
        String access_token = new WenxinConfig().getWenxinToken();
        //2、访问数据
        String requestMethod = "POST";
//        String body = URLEncoder.encode("junshi","UTF-8");//设置要传的信息
        String url = "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/eb-instant?access_token="+access_token;//post请求时格式
        //测试：访问聚合数据的地区新闻api
        HashMap<String, String> msg = new HashMap<>();
        msg.put("role","user");
        msg.put("content", "我将会传输带有latex公式的数学题目，请只给我题目的解析 "+content);
        ArrayList<HashMap> messages = new ArrayList<>();
        messages.add(msg);
        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put("messages", messages);
        String outputStr = JSON.toJSONString(requestBody);
        JSON json = HttpRequest.httpRequest(url,requestMethod,outputStr,"application/json");
        return json;
    }

    public JSON useWenxinToGetSteps(String content) throws IOException {
        String access_token = new WenxinConfig().getWenxinToken();
        //2、访问数据
        String requestMethod = "POST";
//        String body = URLEncoder.encode("junshi","UTF-8");//设置要传的信息
        String url = "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/eb-instant?access_token="+access_token;//post请求时格式
        //测试：访问聚合数据的地区新闻api
        HashMap<String, String> msg = new HashMap<>();
        msg.put("role","user");
        msg.put("content", "我将会传输带有latex公式的数学题目，请只给我题目的解题步骤，列出1.2.3.步 "+content);
        ArrayList<HashMap> messages = new ArrayList<>();
        messages.add(msg);
        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put("messages", messages);
        String outputStr = JSON.toJSONString(requestBody);
        JSON json = HttpRequest.httpRequest(url,requestMethod,outputStr,"application/json");
        return json;
    }

    @Override
    public CommonResponse<String> createConcreteQuestion(ConcreteQuestion concreteQuestion) {
        mongoTemplate.insert(concreteQuestion);
        return CommonResponse.creatForSuccess("添加成功");
    }

    @Override
    public ConcreteQuestion getConcreteQuestionByQid(ConcreteQuestion concreteQuestion) {
        Query query = new Query();
        query.addCriteria(Criteria.where("qid").is(concreteQuestion.getQid()));
        ConcreteQuestion concreteQuestion1 = mongoTemplate.findOne(query, ConcreteQuestion.class);
        return concreteQuestion1;
    }

}
