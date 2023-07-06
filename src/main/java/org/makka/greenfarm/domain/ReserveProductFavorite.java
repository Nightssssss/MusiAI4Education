package org.makka.greenfarm.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "sale_product_favorite")
public class ReserveProductFavorite {
    @TableId
    private String fid;     //收藏ID

    private String uid;     //用户ID

    private String rpid;    //可种植产品ID

    private String favoriteDate;    //收藏时间
}
