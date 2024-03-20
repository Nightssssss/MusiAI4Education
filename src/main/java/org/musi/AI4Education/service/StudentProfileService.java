package org.musi.AI4Education.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.musi.AI4Education.domain.ConcreteQuestion;
import org.musi.AI4Education.domain.StudentProfile;
import org.musi.AI4Education.service.impl.StudentProfileServiceImpl;

import java.sql.Date;
import java.util.List;
import java.util.Map;

public interface StudentProfileService extends IService<StudentProfile> {
    public StudentProfile getStudentProfileByQidAndSid(StudentProfile studentProfile);
    public StudentProfile createStudentProfileByQidAndSid(StudentProfile studentProfile);
    public List<String> getStudentTopWrongTypeAndDetails(String sid);
    public Map<Date, Integer> countQuestionsByDateForStudent(String sid);
}
