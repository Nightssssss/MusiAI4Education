package org.makka.greenfarm.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "sale_product_comment")
public class SaleProductComment {
    private String cid;     //评论ID

    private String commentTime;     //评论时间

    private String uid;     //用户ID

    private String spid;    //可购买产品ID

    private String content;    //评论内容
}
