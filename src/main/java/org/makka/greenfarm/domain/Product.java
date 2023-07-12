package org.makka.greenfarm.domain;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

@Data
public class Product {
    //产品编号
    private String productId;
    //产品数量
    private int quantity;
    //产品类型
    private int type;

    private String longitude;       //经度

    private String latitude;        //纬度

}
