package org.musi.AI4Education.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.musi.AI4Education.domain.StudentProfile;
import org.musi.AI4Education.mapper.StudentProfileMapper;
import org.musi.AI4Education.service.StudentProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class StudentProfileServiceImpl extends ServiceImpl<StudentProfileMapper, StudentProfile> implements StudentProfileService {

    @Autowired
    private StudentProfileMapper studentProfileMapper;

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
    public HashMap<String, String> getStudentTopWrongTypeAndDetails(String sid) {

        LambdaQueryWrapper<StudentProfile> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(StudentProfile::getSid, sid).orderByDesc(StudentProfile::getWeight).last("limit 1");

        StudentProfile studentProfile = studentProfileMapper.selectOne(queryWrapper);
        String type = studentProfile.getType();
        String details = studentProfile.getDetails();
        System.out.println(studentProfile.getType()+studentProfile.getDetails());

        HashMap<String,String> result = new HashMap<>();
        result.put("type",type);
        result.put("details",details);

        return result;
    }
}
