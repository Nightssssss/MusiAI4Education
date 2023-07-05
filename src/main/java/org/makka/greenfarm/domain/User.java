package org.makka.greenfarm.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName(value = "user")
public class User {
    @TableId
    private String uid;     //用户ID

    private String username;        //用户名

    private String password;        //密码

    private String name;        //姓名

    private String gender;      //性别

    private int age;        //年龄

    private Date birth;     //出生日期

    private String avatar;      //头像

    private String phone;       //联系电话

    private String email;       //邮箱

    private String description;        //个人简介

    @TableField(value = "isPremium")
    private int isPremium;      //是否是会员 0：否 1：是

    private int state;      //状态 0：下线 1：登录

    private String faceid;      //人脸识别ID

    private String voiceprint;      //声纹识别ID

    private String nickname;        //昵称

    private int credit;     //积分
}
