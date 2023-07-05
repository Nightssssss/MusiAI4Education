package org.makka.greenfarm.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;

@Data
@TableName(value = "message")
public class Message {
    @TableId
    private String mid;     //消息id

    private DateTimeLiteralExpression.DateTime msgTime;     //消息时间

    private String sid;     //发送方id

    private String rid;     //接收方id

    private String content;     //消息内容
}
