package com.pinyougou.order.service;

import com.pinyougou.pojo.TbOrder;
import com.pinyougou.service.BaseService;
import com.pinyougou.vo.PageResult;

public interface OrderService extends BaseService<TbOrder> {

    PageResult search(Integer page, Integer rows, TbOrder order);

    /**
     * 根据购物车列表保存订单和明细和支付日志信息
     * @param order 订单信息
     * @return 支付日志id
     */
    String addOrder(TbOrder order);
}