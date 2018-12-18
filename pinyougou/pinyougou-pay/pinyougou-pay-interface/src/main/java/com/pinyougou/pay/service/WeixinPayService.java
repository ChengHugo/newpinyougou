package com.pinyougou.pay.service;

import java.util.Map;

public interface WeixinPayService {
    /**
     * 获取支付二维码链接、总金额、操作结果、交易号
     * @param outTradeNo 交易号
     * @param totalFee 本次要支付的总金额
     * @return 支付二维码链接、总金额、操作结果、交易号
     */
    Map<String, String> createNative(String outTradeNo, String totalFee);
}
