package org.musi.AI4Education.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.musi.AI4Education.common.CommonResponse;
import org.musi.AI4Education.domain.BasicQuestion;
import org.musi.AI4Education.domain.ConcreteQuestion;
import org.musi.AI4Education.domain.History;
import org.musi.AI4Education.mapper.BasicQuestionMapper;
import org.musi.AI4Education.service.BasicQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class BasicQuestionServiceImpl extends ServiceImpl<BasicQuestionMapper, BasicQuestion> implements BasicQuestionService {

    @Autowired
    private BasicQuestionMapper basicQuestionMapper;

    @Override
    public CommonResponse<String> createBasicQuestion(BasicQuestion basicQuestion) {
        basicQuestionMapper.insert(basicQuestion);
        return CommonResponse.creatForSuccess("添加成功");
    }

    @Override
    public List<BasicQuestion> getBasicQuestionList() {
        QueryWrapper<BasicQuestion> wrapper = new QueryWrapper<>();
        wrapper.eq("sid",StpUtil.getLoginIdAsString());
        return basicQuestionMapper.selectList(wrapper);
    }

    @Override
    public List<BasicQuestion> getBasicQuestionListInOrder() {
        QueryWrapper<BasicQuestion> wrapper = new QueryWrapper<>();
        wrapper.eq("sid",StpUtil.getLoginIdAsString());
        List<BasicQuestion> basicQuestionList = basicQuestionMapper.selectList(wrapper);
        Collections.sort(basicQuestionList, Comparator.comparing(BasicQuestion::getDate).reversed());
        return basicQuestionList;
    }

    @Override
    public List<BasicQuestion> getMarkedBasicQuestionList() {
        QueryWrapper<BasicQuestion> wrapper = new QueryWrapper<>();
        wrapper.eq("sid",StpUtil.getLoginIdAsString());
        wrapper.eq("mark",1);
        return basicQuestionMapper.selectList(wrapper);
    }

    @Override
    public BasicQuestion getBasicQuestionByQid(BasicQuestion basicQuestion) {
        QueryWrapper<BasicQuestion> wrapper = new QueryWrapper<>();
        wrapper.eq("qid",basicQuestion.getQid());
        return basicQuestionMapper.selectOne(wrapper);
    }


    @Override
    public BasicQuestion addQuestionMark(BasicQuestion basicQuestion) {
        UpdateWrapper<BasicQuestion> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("qid", basicQuestion.getQid());
        updateWrapper.set("mark",1);
        update(basicQuestion,updateWrapper);

        QueryWrapper<BasicQuestion> wrapper = new QueryWrapper<>();
        wrapper.eq("qid", basicQuestion.getQid());
        BasicQuestion basicQuestion1=basicQuestionMapper.selectOne(wrapper);
        return basicQuestion1;
    }

    @Override
    public BasicQuestion deleteQuestionMark(BasicQuestion basicQuestion) {
        UpdateWrapper<BasicQuestion> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("qid", basicQuestion.getQid());
        updateWrapper.set("mark",0);
        update(basicQuestion,updateWrapper);

        QueryWrapper<BasicQuestion> wrapper = new QueryWrapper<>();
        wrapper.eq("qid", basicQuestion.getQid());
        BasicQuestion basicQuestion1=basicQuestionMapper.selectOne(wrapper);
        return basicQuestion1;
    }

    @Override
    public BasicQuestion addQuestionPosition(BasicQuestion basicQuestion) {
        UpdateWrapper<BasicQuestion> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("qid", basicQuestion.getQid());
        updateWrapper.set("position",basicQuestion.getPosition());
        update(basicQuestion,updateWrapper);

        QueryWrapper<BasicQuestion> wrapper = new QueryWrapper<>();
        wrapper.eq("qid", basicQuestion.getQid());
        BasicQuestion basicQuestion1=basicQuestionMapper.selectOne(wrapper);
        return basicQuestion1;
    }

    @Override
    public BasicQuestion modifyQuestionPosition(BasicQuestion basicQuestion) {
        UpdateWrapper<BasicQuestion> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("qid", basicQuestion.getQid());
        updateWrapper.set("position",basicQuestion.getPosition());
        update(basicQuestion,updateWrapper);

        QueryWrapper<BasicQuestion> wrapper = new QueryWrapper<>();
        wrapper.eq("qid", basicQuestion.getQid());
        BasicQuestion basicQuestion1=basicQuestionMapper.selectOne(wrapper);
        return basicQuestion1;
    }

    @Override
    public BasicQuestion getQuestionPosition(BasicQuestion basicQuestion) {
        QueryWrapper<BasicQuestion> wrapper = new QueryWrapper<>();
        wrapper.eq("qid", basicQuestion.getQid());
        BasicQuestion basicQuestion1=basicQuestionMapper.selectOne(wrapper);
        return basicQuestion1;
    }

    @Override
    public String getQuestionTextByQid(BasicQuestion basicQuestion) {
        QueryWrapper<BasicQuestion> wrapper = new QueryWrapper<>();
        wrapper.eq("qid", basicQuestion.getQid());
        System.out.println(basicQuestion.getQid());
        BasicQuestion basicQuestion1=basicQuestionMapper.selectOne(wrapper);
        return basicQuestion1.getQuestionText();
    }
}
