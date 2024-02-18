package org.musi.AI4Education.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.musi.AI4Education.common.CommonResponse;
import org.musi.AI4Education.domain.ConcreteQuestion;
import org.musi.AI4Education.mapper.ConcreteQuestionMapper;
import org.musi.AI4Education.service.ConcreteQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;

@Service
public class ConcreteQuestionServiceImpl extends ServiceImpl<ConcreteQuestionMapper, ConcreteQuestion> implements ConcreteQuestionService {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public CommonResponse<String> createConcreteQuestion(ConcreteQuestion concreteQuestion) {
        mongoTemplate.insert(concreteQuestion);
        return CommonResponse.creatForSuccess("添加成功");
    }

}
