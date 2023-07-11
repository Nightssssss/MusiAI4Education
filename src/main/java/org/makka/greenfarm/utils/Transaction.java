package org.makka.greenfarm.utils;

import lombok.Data;

import java.io.Serializable;

@Data
public class Transaction implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 唯一标识
     */
    private String id;
    /**
     * 业务数据
     */
    private String businessInfo;

}
