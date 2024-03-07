package org.musi.AI4Education.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.musi.AI4Education.domain.BasicQuestion;

import java.util.List;

@Mapper
public interface BasicQuestionMapper extends BaseMapper<BasicQuestion> {

//   @Select("SELECT sid,qid,questionType,date,subject,wrongType,wrongDetails,mark,path,position,questionText FROM question_basic_info WHERE sid = #{sid} and position = #{position}")
//   List<BasicQuestion> getBasicQuestionByIdandPositon(@Param("sid") String sid, @Param("position") String position);

}
