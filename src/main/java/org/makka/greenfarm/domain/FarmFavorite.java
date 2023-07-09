package org.makka.greenfarm.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@TableName(value = "farm_favorite")
public class FarmFavorite {
    //收藏编号
    @TableId
    private String ffid;

    private String uid;

    //农场编号
    private String fid;

    @TableField(value = "favoriteDate")
    private LocalDateTime favoriteDate;    //收藏时间

    @TableField(exist = false)
    private String picture;     //农场图片

    @TableField(exist = false)
    private String scroe;        //农场评分

    @TableField(exist = false)
    private String name;         //农场名称

    @TableField(exist = false)
    private String description;  //农场描述

}
