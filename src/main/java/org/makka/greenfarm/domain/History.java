package org.makka.greenfarm.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;

@Data
@TableName(value = "history")
public class History {
    private String hid;     //历史记录id

    private String uid;     //用户id

    private String pid;     //产品id

    private DateTimeLiteralExpression.DateTime browseTime;      //浏览时间

    // 0:可购买产品 1:可种植产品
    private int type;
}
