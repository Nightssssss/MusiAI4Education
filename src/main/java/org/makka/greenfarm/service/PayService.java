package org.makka.greenfarm.service;
import com.baomidou.mybatisplus.extension.service.IService;
import org.makka.greenfarm.domain.Order;
import org.makka.greenfarm.domain.Pay;

import java.io.UnsupportedEncodingException;
import java.util.List;

public interface PayService extends IService<Pay> {
    public String getPay(List<Order> orderList) throws UnsupportedEncodingException;
}
