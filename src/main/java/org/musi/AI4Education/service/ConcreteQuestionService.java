package org.musi.AI4Education.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.IService;
import org.musi.AI4Education.common.CommonResponse;
import org.musi.AI4Education.domain.BasicQuestion;
import org.musi.AI4Education.domain.ConcreteQuestion;

import java.io.IOException;

public interface ConcreteQuestionService extends IService<ConcreteQuestion> {

    //调用大模型生成答案
    public JSON useWenxinToGetAnswer(String content) throws IOException;
    public JSON useWenxinToGetExplanation(String content) throws IOException;
    public JSON useWenxinToGetSteps(String content) throws IOException;

    public CommonResponse<String> createConcreteQuestion(ConcreteQuestion concreteQuestion);

    //查询单个错题的详细信息
    public ConcreteQuestion getConcreteQuestionByQid(ConcreteQuestion concreteQuestion);
}
