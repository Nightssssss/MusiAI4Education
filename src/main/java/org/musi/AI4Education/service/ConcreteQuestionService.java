package org.musi.AI4Education.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.musi.AI4Education.common.CommonResponse;
import org.musi.AI4Education.domain.BasicQuestion;
import org.musi.AI4Education.domain.ConcreteQuestion;

public interface ConcreteQuestionService extends IService<ConcreteQuestion> {

    public CommonResponse<String> createConcreteQuestion(ConcreteQuestion concreteQuestion);

}
