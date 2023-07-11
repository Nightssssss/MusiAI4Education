package org.makka.greenfarm.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.List;

@Data
@TableName(value = "reserve_product")
public class ReserveProduct {
    @TableId
    private String rpid;

    private String name;

    private String fid;

    private Double uniprice;

    private int yield;

    @TableField("costTime")
    private int costTime;

    private String description;

    private int choice;

    private String picture;

    private int sales;

    private int stock;

    @TableField(exist = false)
    private List<ReserveProductComment> reserveProductCommentList;      //评论列表

}
