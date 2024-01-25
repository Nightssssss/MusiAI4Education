package org.musi.AI4Education.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName(value = "student_profile")
public class studentProfile {

    @TableId
    private String sid;      //学生ID

    private String subject;     //学科

    private String type;     //错误类型

    private String details;     //错误详情

    @TableField(value = "latest_date")
    private Date latestDate;      //题目文本内容

    private int times;      //犯错次数

    private double weight;      //权重
}
