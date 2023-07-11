package org.makka.greenfarm.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.makka.greenfarm.config.AlipayConfig;
import org.makka.greenfarm.controller.BlockController;
import org.makka.greenfarm.domain.Order;
import org.makka.greenfarm.domain.Pay;
import org.makka.greenfarm.mapper.PayMapper;
import org.makka.greenfarm.service.PayService;
import org.makka.greenfarm.utils.BlockUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@Service
public class PayServiceImpl extends ServiceImpl<PayMapper, Pay> implements PayService {

    @Autowired
    private PayMapper payMapper;

    @Autowired
    private BlockUtil blockUtil;

    //获取订单信息
    @Override
    public String getPay(List<Order> orderList) throws UnsupportedEncodingException {

        //获取订单总金额
        Double totalPrice = 0.00;
        for(Order order:orderList){
            totalPrice += order.getQuantity()*order.getUniprice();
            System.out.println(totalPrice);
        }
        blockUtil.createNewBlock(orderList);
        //获取订单ID
        String orderId = orderList.get(0).getOid();

        //获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.APP_ID, AlipayConfig.APP_PRIVATE_KEY, "json", AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.sign_type);

        //设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(AlipayConfig.return_url + "?orderId=" + orderId);
        alipayRequest.setNotifyUrl(AlipayConfig.notify_url + "?orderId=" + orderId);

        //商户订单号，商户网站订单系统中唯一订单号，必填
        String out_trade_no = new String((orderId).getBytes("ISO-8859-1"),"UTF-8");
        //付款金额，必填
        String total_amount = new String((Double.toString(totalPrice)).getBytes("ISO-8859-1"),"UTF-8");
        //订单名称，必填
        String subject = new String(("this is order").getBytes("ISO-8859-1"),"UTF-8");
        //商品描述，可空
        String body = new String(("444").getBytes("ISO-8859-1"),"UTF-8");

        alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
                + "\"total_amount\":\""+ total_amount +"\","
                + "\"subject\":\""+ subject +"\","
                + "\"body\":\""+ body +"\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
        //请求
        String result = null;
        try {
            result = alipayClient.pageExecute(alipayRequest).getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return result;
    }
}
