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
        //商家
        if(!StringUtils.isEmpty(goods.getSellerId())){
            criteria.andEqualTo("sellerId", goods.getSellerId());
        }
        //审核状态
        if(!StringUtils.isEmpty(goods.getAuditStatus())){
            criteria.andEqualTo("auditStatus", goods.getAuditStatus());
        }
        //商品名称
        if(!StringUtils.isEmpty(goods.getGoodsName())){
            criteria.andLike("goodsName", "%" + goods.getGoodsName() + "%");
        }

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

    @Override
    public Goods findGoodsById(Long id) {
        Goods goods = new Goods();
        /**
         * SELECT * FROM tb_goods WHERE id=? ;
         * SELECT * FROM tb_goods_desc WHERE goods_id=? ;
         * SELECT * FROM tb_item WHERE goods_id=?;
         */
        //1、基本信息
        goods.setGoods(findOne(id));
        //2、描述信息
        goods.setGoodsDesc(goodsDescMapper.selectByPrimaryKey(id));
        //3、根据spu id 查询sku列表
        TbItem item = new TbItem();
        item.setGoodsId(id);
        List<TbItem> itemList = itemMapper.select(item);
        goods.setItemList(itemList);

        return goods;
    }

    /**
     * 保存动态sku列表
     * @param goods 商品信息（基本、描述、sku列表）
     */
    private void saveItemList(Goods goods) {

        if ("1".equals(goods.getGoods().getIsEnableSpec())) {
            //启用规格
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

                    setItemValue(item, goods);

                    //保存sku
                    itemMapper.insertSelective(item);
                }
            }
        } else {
            //未启用规格
            //1. 创建item对象；大多数据数据来自spu设置到对象中；
            TbItem tbItem = new TbItem();
            //2. 如果spu中没有的数据，如：spec（｛｝），num（9999），status(0未启用)，isDefault(1默认)
            tbItem.setSpec("{}");
            tbItem.setPrice(goods.getGoods().getPrice());
            tbItem.setStatus("0");
            tbItem.setIsDefault("1");
            tbItem.setNum(9999);
            tbItem.setTitle(goods.getGoods().getGoodsName());

            //设置商品的其它信息
            setItemValue(tbItem, goods);

            //3. 保存到数据库中
            itemMapper.insertSelective(tbItem);
        }
    }

    private void setItemValue(TbItem item, Goods goods) {
        //商品分类 来自 商品spu的第3级商品分类id
        item.setCategoryid(goods.getGoods().getCategory3Id());
        TbItemCat itemCat = itemCatMapper.selectByPrimaryKey(item.getCategoryid());
        item.setCategory(itemCat.getName());

        //图片；可以从spu中的图片地址列表中获取第1张图片
        if (!StringUtils.isEmpty(goods.getGoodsDesc().getItemImages())) {
            List<Map> imageList = JSONArray.parseArray(goods.getGoodsDesc().getItemImages(), Map.class);
            if (imageList.get(0).get("url") != null) {
                item.setImage(imageList.get(0).get("url").toString());
            }
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
    }
}
