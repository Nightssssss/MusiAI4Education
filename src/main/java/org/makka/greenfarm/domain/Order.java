package org.makka.greenfarm.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;

import java.util.Date;

@Data
@TableName(value = "orders")
public class Order {

        @TableId(value = "oid",type = IdType.INPUT)
        private String oid;     // 订单id

        private String pid;     // 产品id

        private String uid;     // 用户id

        private String aid;     // 地址id

        @TableField(value = "orderDate")
        private Date orderDate; // 订单日期

        private int quantity;   // 数量

        private Double uniprice;        // 单价

        private int type;       // 类型

        private int status;     // 状态
}
