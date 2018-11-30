package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.mapper.*;
import com.pinyougou.pojo.*;
import com.pinyougou.sellergoods.service.GoodsService;
import com.pinyougou.service.impl.BaseServiceImpl;
import com.pinyougou.vo.Goods;
import com.pinyougou.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service(interfaceClass = GoodsService.class)
public class GoodsServiceImpl extends BaseServiceImpl<TbGoods> implements GoodsService {

    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private GoodsDescMapper goodsDescMapper;

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private ItemCatMapper itemCatMapper;

    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    private SellerMapper sellerMapper;

    @Override
    public PageResult search(Integer page, Integer rows, TbGoods goods) {
        PageHelper.startPage(page, rows);

        Example example = new Example(TbGoods.class);
        Example.Criteria criteria = example.createCriteria();
        /*if(!StringUtils.isEmpty(goods.get***())){
            criteria.andLike("***", "%" + goods.get***() + "%");
        }*/

        List<TbGoods> list = goodsMapper.selectByExample(example);
        PageInfo<TbGoods> pageInfo = new PageInfo<>(list);

        return new PageResult(pageInfo.getTotal(), pageInfo.getList());
    }

    @Override
    public void addGoods(Goods goods) {
        //1. 保存商品基本
        add(goods.getGoods());

        //2. 保存商品描述信息
        goods.getGoodsDesc().setGoodsId(goods.getGoods().getId());
        goodsDescMapper.insertSelective(goods.getGoodsDesc());

        //3. 保存商品sku列表（每个sku都要保存到tb_item）
        saveItemList(goods);
    }

    /**
     * 保存动态sku列表
     * @param goods 商品信息（基本、描述、sku列表）
     */
    private void saveItemList(Goods goods) {

        if (goods.getItemList() != null && goods.getItemList().size() > 0) {
            for (TbItem item : goods.getItemList()) {

                //标题=spu名称+所有规格的选项值
                String title = goods.getGoods().getGoodsName();
                //获取规格；{"网络":"移动3G","机身内存":"16G"}
                Map<String, String> map = JSON.parseObject(item.getSpec(), Map.class);
                Set<Map.Entry<String, String>> entries = map.entrySet();
                for (Map.Entry<String, String> entry : entries) {
                    title += " " + entry.getValue();
                }
                item.setTitle(title);

                //商品分类 来自 商品spu的第3级商品分类id
                item.setCategoryid(goods.getGoods().getCategory3Id());
                TbItemCat itemCat = itemCatMapper.selectByPrimaryKey(item.getCategoryid());
                item.setCategory(itemCat.getName());

                //图片；可以从spu中的图片地址列表中获取第1张图片
                if (!StringUtils.isEmpty(goods.getGoodsDesc().getItemImages())) {
                    List<Map> imageList = JSONArray.parseArray(goods.getGoodsDesc().getItemImages(), Map.class);
                    item.setImage(imageList.get(0).get("url").toString());
                }

                item.setGoodsId(goods.getGoods().getId());

                //品牌
                TbBrand brand = brandMapper.selectByPrimaryKey(goods.getGoods().getBrandId());
                item.setBrand(brand.getName());

                item.setCreateTime(new Date());
                item.setUpdateTime(item.getCreateTime());

                //卖家
                item.setSellerId(goods.getGoods().getSellerId());
                TbSeller seller = sellerMapper.selectByPrimaryKey(item.getSellerId());
                item.setSeller(seller.getName());

                //保存sku
                itemMapper.insertSelective(item);
            }
        }
    }
}
