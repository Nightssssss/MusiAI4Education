package org.makka.greenfarm.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;

import java.util.Date;

@Data
@TableName(value = "order")
public class Order {
        @TableId
        private String oid;
        @TableId
        private String pid;

        private String uid;

        private String aid;

        private Date orderDate;

        private int quantity;

        private Double uniprice;

        private int type;

        private int status;
}
