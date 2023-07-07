package org.makka.greenfarm.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName(value = "reserve_product_favorite")
public class ReserveProductFavorite {
    @TableId
    private String fid;     //收藏ID

    private String uid;     //用户ID

    private String rpid;    //可种植产品ID

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
