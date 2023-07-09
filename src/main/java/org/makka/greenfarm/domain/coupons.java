package org.makka.greenfarm.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "coupons")
public class coupons {
    private String cid;     //优惠券ID

    private String uid;     //用户ID

    private String value;     //优惠券面值

    private String startTime;     //优惠券开始时间

    private String endTime;     //优惠券结束时间

    private String state;     //优惠券状态

}
