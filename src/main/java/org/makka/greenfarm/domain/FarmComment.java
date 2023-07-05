package org.makka.greenfarm.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;

@Data
@TableName(value = "farm_comment")
public class FarmComment {
    @TableId
    private String cid;     //评论ID

    private DateTimeLiteralExpression.DateTime commentTime;     //评论时间

    private String uid;     //用户ID

    private String fid;     //农场ID

    private String content;     //评论内容
}
