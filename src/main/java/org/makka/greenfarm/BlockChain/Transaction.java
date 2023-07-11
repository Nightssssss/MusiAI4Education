package org.makka.greenfarm.BlockChain;

import lombok.Data;
import org.makka.greenfarm.domain.Order;

import java.io.Serializable;
import java.util.List;

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

    private List<Order> orderList;

}
