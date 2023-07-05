package org.makka.greenfarm.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName(value = "progress")
public class Progress {
    @TableId
    private String pid;

    //此处为订单编号
    private String oid;

    private Date yieldDate;

    private String state;

    private Date fertilizeDate;

    private int waterCycle;

    private String brand;

    private String picture1;

    private String picture2;

    private String picture3;

    private String picture4;
}
