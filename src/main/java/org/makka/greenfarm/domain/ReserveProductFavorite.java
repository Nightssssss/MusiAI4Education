package org.makka.greenfarm.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName(value = "sale_product_favorite")
public class ReserveProductFavorite {
    @TableId
    private String fid;     //收藏ID

    @TableField(value = "favoriteTime")
    private LocalDateTime favoriteTime;     //评论时间

    private String uid;     //用户ID

    private String rpid;    //可种植产品ID

    private String favoriteDate;    //收藏时间
}
