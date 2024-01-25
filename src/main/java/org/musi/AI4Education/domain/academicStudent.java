package org.musi.AI4Education.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "student_academic_info")
public class academicStudent {

    @TableId
    private String sid;     //学生ID

    private String grade;      //年级

    private String major;      //选科

    private String rank;      //排名
}
