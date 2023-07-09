package org.makka.greenfarm.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "owner")
public class Owner {
    @TableId
    private String oid;     //农场主ID

    private String name;        //农场主姓名

    private String phone;       //农场主电话

    private int experience;     //农场主经验

    private String description;        //农场主描述

    private String avatar;      //农场主头像

    private String fid;         //农场ID

    private String username;    //农场主用户名

    private String password;    //农场主密码
}
