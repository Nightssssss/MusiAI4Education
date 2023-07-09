package org.makka.greenfarm.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName(value = "progress")
public class Progress {
    @TableId
    private String pid;    //进度ID

    @TableField(value = "yieldDate")
    private Date yieldDate;    //收获时间

    private String state;   //状态

    @TableField(value = "fertilizeDate")
    private Date fertilizeDate;    //施肥时间

    @TableField(value = "waterDate")
    private int waterCycle;    //浇水周期

    private String brand;   //品牌

    private String picture1;    //状态图1

    private String picture2;    //状态图2

    private String picture3;    //状态图3

    private String picture4;    //状态图4

    private String rpid;    //种植产品ID

    private int area;   //种植面积

    private String uid;     //用户ID

    @TableField(exist = false)
    private String name;    //商品名称

    @TableField(exist = false)
    private String picture;     //商品图片

    @TableField(exist = false)
    private String fid;     //农场ID
}
