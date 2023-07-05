package org.makka.greenfarm.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "shopping_cart")
public class ShoppingCart {
    private String pid;     //产品id

    private String uid;     //用户ID

    private int type;    //产品类型 0:可购买产品 1:可种植产品

    private int quantity;      //数量
}
