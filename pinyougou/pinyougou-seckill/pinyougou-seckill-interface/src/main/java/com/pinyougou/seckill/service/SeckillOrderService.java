package com.pinyougou.seckill.service;

import com.pinyougou.pojo.TbSeckillOrder;
import com.pinyougou.service.BaseService;
import com.pinyougou.vo.PageResult;

public interface SeckillOrderService extends BaseService<TbSeckillOrder> {

    PageResult search(Integer page, Integer rows, TbSeckillOrder seckillOrder);

    /**
     * 生成秒杀订单并返回订单号
     * @param seckillId 秒杀商品id
     * @return 操作结果
     */
    String submitOrder(String username, Long seckillId);
}