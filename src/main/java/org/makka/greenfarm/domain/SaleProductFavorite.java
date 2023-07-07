package org.makka.greenfarm.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName(value = "sale_product_favorite")
public class SaleProductFavorite {
    private String fid;     //收藏ID

    private String uid;     //用户ID

    private String spid;    //可购买产品ID

    @TableField(value = "favoriteDate")
    private LocalDateTime favoriteDate;    //收藏时间

    @TableField(exist = false)
    private String picture;     //产品图片

    @TableField(exist = false)
    private String shelves;      //产品上架状态

    @TableField(exist = false)
    private String farmId;     //农场ID

    @TableField(exist = false)
    private String name;        //产品名称

    @TableField(exist = false)
    private double uniprice;    //产品单价
}
