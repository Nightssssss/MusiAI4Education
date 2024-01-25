package org.musi.AI4Education.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName(value = "question_concrete_info")
public class concreteQuestion {

    @TableId
    private String qid;      //题目ID

    private String inspiration;     //易错点

    @TableField(value = "question_text")
    private String questionText;      //题目文本内容

    private String reason;      //错误原因

    @TableField(value = "question_steps")
    private String questionSteps;       //错误类型

    @TableField(value = "question_analysis")
    private String questionAnalysis;      //错误详情

    // Session
}
