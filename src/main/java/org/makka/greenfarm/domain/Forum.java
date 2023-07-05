package org.makka.greenfarm.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;

@Data
@TableName(value = "forum")
public class Forum {
    private String fid;     //帖子ID

    private String uid;     //用户ID

    private DateTimeLiteralExpression.DateTime postTime;    //发帖时间

    private String title;   //帖子标题

    private String content; //帖子内容

    private String pircture;     //产品ID
}
