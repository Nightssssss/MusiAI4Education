package org.musi.AI4Education.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName(value = "question_basic_info")
public class basicQuestion {

    @TableId
    private String sid;     //学生ID

    private String qid;      //题目ID

    @TableField(value = "question_type")
    private String questionType;      //题型

    private Date date;      //拍题时间

    private String subject;     //科目

    @TableField(value = "wrong_type")
    private String wrongType;       //错误类型

    @TableField(value = "wrong_details")
    private String wrongDetails;      //错误详情

    private int mark;        //标记

    private String path;       //题目照片存储路径
}
