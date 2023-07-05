package org.makka.greenfarm.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "addresslist")
public class AddressList {
    @TableId
    private String aid;     //地址簿ID

    private String uid;     //用户ID

    private String address;     //地址

    private String phone;       //联系电话

    private String name;        //收货人姓名

    private int isDefault;      //是否默认地址
}
