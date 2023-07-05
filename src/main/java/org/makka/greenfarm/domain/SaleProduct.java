package org.makka.greenfarm.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "sale_product")
public class SaleProduct {
    @TableId
    private String spid;        //可购买产品ID

    private String name;        //产品名称

    private String fid;     //农场ID

    private Double uniprice;    //单价

    private int stock;      //库存

    private int sales;      //销量

    private String desc;        //产品描述

    private String picture;     //产品图片

    private int shelves;        //是否上架
}
