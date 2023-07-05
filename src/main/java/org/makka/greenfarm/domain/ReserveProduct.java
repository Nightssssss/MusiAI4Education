package org.makka.greenfarm.domain;

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

    private int costTime;

    private String desc;

    private int choice;

    private String picture;
}
