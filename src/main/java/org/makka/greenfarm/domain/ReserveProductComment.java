package org.makka.greenfarm.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;

@Data
@TableName(value = "reserve_product_comment")
public class ReserveProductComment {
    @TableId
    private String cid;     //评论ID

    private DateTimeLiteralExpression.DateTime commentTime;     //评论时间

    private String uid;     //用户ID

    private String rpid;    //可种植产品ID

    private String content;    //评论内容

    @TableField(exist = false)
    private String avatar;      //用户头像

    @TableField(exist = false)
    private String nickname;        //用户昵称
}
