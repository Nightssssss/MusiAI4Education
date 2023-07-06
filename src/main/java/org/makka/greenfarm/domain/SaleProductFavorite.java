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
}
