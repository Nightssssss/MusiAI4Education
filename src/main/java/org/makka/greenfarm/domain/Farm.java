package org.makka.greenfarm.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Blob;
import java.util.Date;

@Data
@TableName(value = "farm")
public class Farm {
    @TableId
    private String fid;     //农场ID

    private String name;    //农场名称

    private  String address;    //农场地址

    private Double score;     //农场评分

    private int tot_area;       //农场总面积

    private int rent_area;       //农场已出租面积

    private int res_area;     //农场剩余面积

    private String ownerid;        //农场主ID

    private String phone;      //联系电话

    private String picture;      //农场图片

    private Blob video;     //农场视频

    private String description;        //农场描述

    private Date createdDate;       //创建时间

    private int openDate;        //开放时间

    private int closeDate;       //关闭时间
}
