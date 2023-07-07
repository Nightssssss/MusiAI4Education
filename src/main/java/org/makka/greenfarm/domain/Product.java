package org.makka.greenfarm.domain;
import lombok.Data;

@Data
public class Product {
    //产品编号
    private String productId;
    //产品数量
    private int quantity;
    //产品类型
    private int type;

}
