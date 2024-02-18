package org.musi.AI4Education.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.musi.AI4Education.common.CommonResponse;
import org.musi.AI4Education.domain.BasicQuestion;
import org.musi.AI4Education.domain.Student;

public interface BasicQuestionService extends IService<BasicQuestion> {

    //大模型生成题目概要信息
    public CommonResponse<String> createBasicQuestion(BasicQuestion basicQuestion);

    //查询被标记的错题


    //将错题添加标记
    public BasicQuestion addQuestionMark(BasicQuestion basicQuestion);

    //将错题取消标记
    public BasicQuestion deleteQuestionMark(BasicQuestion basicQuestion);

    //设置错题所在位置
    public BasicQuestion addQuestionPosition(BasicQuestion basicQuestion);

    //修改错题所在位置
    public BasicQuestion modifyQuestionPosition(BasicQuestion basicQuestion);

    //查询错题所在位置
    public BasicQuestion getQuestionPosition(BasicQuestion basicQuestion);



}
