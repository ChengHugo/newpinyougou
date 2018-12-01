package com.pinyougou.sellergoods.service;

import com.pinyougou.pojo.TbGoods;
import com.pinyougou.service.BaseService;
import com.pinyougou.vo.Goods;
import com.pinyougou.vo.PageResult;

public interface GoodsService extends BaseService<TbGoods> {

    PageResult search(Integer page, Integer rows, TbGoods goods);

    /**
     * 商品的基本、描述、sku列表信息之后要保存到数据库中
     * @param goods 商品信息
     */
    void addGoods(Goods goods);

    /**
     * 根据spu id查询商品基本、描述、sku列表
     * @param id spu id
     * @return 商品基本、描述、sku列表
     */
    Goods findGoodsById(Long id);
}