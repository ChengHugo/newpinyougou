package com.pinyougou.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.cart.service.CartService;
import com.pinyougou.order.service.OrderService;
import com.pinyougou.pay.service.WeixinPayService;
import com.pinyougou.pojo.TbPayLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/pay")
@RestController
public class PayController {

    @Reference
    private OrderService orderService;

    @Reference
    private WeixinPayService weixinPayService;


    /**
     * 获取支付二维码链接、总金额、操作结果、交易号
     * @param outTradeNo 交易号（支付日志id）
     * @return 支付二维码链接、总金额、操作结果、交易号
     */
    @GetMapping("/createNative")
    public Map<String, String> createNative(String outTradeNo){
        try {
            //1、根据支付日志查询支付日志信息获取总金额
            TbPayLog payLog = orderService.findPayLogByOutTradeNo(outTradeNo);
            //本次要支付的总金额，精确到分
            String totalFee = payLog.getTotalFee().toString();

            //2、调用支付业务方法获取返回信息
            return weixinPayService.createNative(outTradeNo, totalFee);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }

}
