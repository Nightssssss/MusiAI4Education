package org.musi.AI4Education.service.impl;

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
import java.util.*;

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
    public Map<Date, Integer> countQuestionsByDateForStudent(String sid) {
        Date today = new Date(System.currentTimeMillis()); // 使用当前系统时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.set(Calendar.HOUR_OF_DAY, 0); // 将小时设为0
        calendar.set(Calendar.MINUTE, 0); // 将分钟设为0
        calendar.set(Calendar.SECOND, 0); // 将秒数设为0
        calendar.set(Calendar.MILLISECOND, 0); // 将毫秒数设为0

        Date fifteenDaysAgo = new Date(calendar.getTimeInMillis());
        calendar.add(Calendar.DAY_OF_MONTH, -14); // 15 天前的日期

        QueryWrapper<BasicQuestion> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sid", sid)
                .ge("date", fifteenDaysAgo)
                .le("date", today);

        List<BasicQuestion> entities = basicQuestionMapper.selectList(queryWrapper);

        Map<Date, Integer> result = new TreeMap<>(); // 使用 TreeMap 来保持按日期排序

        for (int i = 0; i < 15; i++) {
            result.put(new Date(calendar.getTimeInMillis()), 0);
            calendar.add(Calendar.DAY_OF_MONTH, 1); // 日期按顺序增加
        }

        for (BasicQuestion entity : entities) {
            Date date = entity.getDate();
            calendar.setTime(date);
            calendar.set(Calendar.HOUR_OF_DAY, 0); // 将小时设为0
            calendar.set(Calendar.MINUTE, 0); // 将分钟设为0
            calendar.set(Calendar.SECOND, 0); // 将秒数设为0
            calendar.set(Calendar.MILLISECOND, 0); // 将毫秒数设为0
            date = new Date(calendar.getTimeInMillis());
            Integer count = result.get(date);
            if (count == null) {
                count = 0;
            }
            result.put(date, count + 1);
        }

        return result;
    }
}
