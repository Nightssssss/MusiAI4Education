package org.makka.greenfarm.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

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
}
