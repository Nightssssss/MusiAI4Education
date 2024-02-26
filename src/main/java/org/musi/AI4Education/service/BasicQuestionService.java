package org.musi.AI4Education.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.musi.AI4Education.common.CommonResponse;
import org.musi.AI4Education.domain.BasicQuestion;
import org.musi.AI4Education.domain.ConcreteQuestion;
import org.musi.AI4Education.domain.Student;

import java.util.List;

public interface BasicQuestionService extends IService<BasicQuestion> {

    //大模型生成题目概要信息
    public CommonResponse<String> createBasicQuestion(BasicQuestion basicQuestion);

    //查询所有错题的概要信息
    public List<BasicQuestion> getBasicQuestionList();

    //查询所有错题的概要信息并排序
    public List<BasicQuestion> getBasicQuestionListInOrder();

    //查询被标记的错题
    public List<BasicQuestion> getMarkedBasicQuestionList();

    //查询单个错题的概要信息
    public BasicQuestion getBasicQuestionByQid(BasicQuestion basicQuestion);

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
