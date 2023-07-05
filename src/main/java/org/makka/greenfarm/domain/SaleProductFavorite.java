package org.makka.greenfarm.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "sale_product_favorite")
public class SaleProductFavorite {
    private String fid;     //收藏ID

    private String uid;     //用户ID

    private String spid;    //可购买产品ID

    private String favoriteDate;    //收藏时间
}
