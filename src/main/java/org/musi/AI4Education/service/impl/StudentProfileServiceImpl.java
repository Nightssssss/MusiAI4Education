package org.musi.AI4Education.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.musi.AI4Education.domain.BasicQuestion;
import org.musi.AI4Education.domain.StudentProfile;
import org.musi.AI4Education.mapper.BasicQuestionMapper;
import org.musi.AI4Education.mapper.StudentProfileMapper;
import org.musi.AI4Education.service.StudentProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StudentProfileServiceImpl extends ServiceImpl<StudentProfileMapper, StudentProfile> implements StudentProfileService {

    @Autowired
    private StudentProfileMapper studentProfileMapper;
    @Autowired
    private BasicQuestionMapper basicQuestionMapper;

    @Override
    public StudentProfile getStudentProfileByQidAndSid(StudentProfile studentProfile) {

        QueryWrapper<StudentProfile> wrapper = new QueryWrapper<>();

        wrapper.eq("sid",studentProfile.getSid());
        wrapper.eq("type",studentProfile.getType());
        wrapper.eq("details",studentProfile.getDetails());

        StudentProfile studentProfileTemp = studentProfileMapper.selectOne(wrapper);

        return studentProfileTemp;
    }

    @Override
    public StudentProfile createStudentProfileByQidAndSid(StudentProfile studentProfile) {
        studentProfileMapper.insert(studentProfile);
        return studentProfile;
    }

    @Override
    public List<String> getStudentTopWrongTypeAndDetails(String sid) {

        LambdaQueryWrapper<StudentProfile> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(StudentProfile::getSid, sid).orderByDesc(StudentProfile::getWeight).last("limit 1");

        StudentProfile studentProfile = studentProfileMapper.selectOne(queryWrapper);
        String type = studentProfile.getType();
        String details = studentProfile.getDetails();

        List<String> result = new ArrayList<>();
        result.add(type);
        result.add(details);

        return result;
    }

    @Override
    public Map<String, Long> countQuestionPerDay() {
        String sid = StpUtil.getLoginIdAsString();
        LocalDate endDate = LocalDate.now(); // 结束日期为今天
        LocalDate startDate = endDate.minusDays(14); // 开始日期为最近15天内的前一天

        QueryWrapper<BasicQuestion> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sid", sid);
        queryWrapper.between("date", Date.valueOf(startDate), Date.valueOf(endDate)); // 查询条件为日期在最近15天内的数据

        List<BasicQuestion> resultList = basicQuestionMapper.selectList(queryWrapper);

        // 使用Java8的流式操作进行统计每天的搜题量
        Map<String, Long> countPerDayMap = resultList.stream()
                .collect(Collectors.groupingBy(
                        entity -> entity.getDate().toString(),
                        TreeMap::new, // 使用 TreeMap 保证按日期排序
                        Collectors.counting()
                ));

        // 补全缺少的日期，并设置对应日期的搜题量为0
        for (int i = 0; i < 15; i++) {
            LocalDate date = endDate.minusDays(i);
            countPerDayMap.putIfAbsent(String.valueOf(Date.valueOf(date)).substring(0,10), 0L);
        }

        Map<String, Long> sortedMap = new TreeMap<>(countPerDayMap);

        return sortedMap;
    }
}
