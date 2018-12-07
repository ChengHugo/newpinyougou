package com.pinyougou.search.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.search.service.ItemSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.HighlightEntry;
import org.springframework.data.solr.core.query.result.HighlightPage;
import org.springframework.data.solr.core.query.result.ScoredPage;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class ItemSearchServiceImpl implements ItemSearchService {

    @Autowired
    private SolrTemplate solrTemplate;

    @Override
    public Map<String, Object> search(Map<String, Object> searchMap) {
        Map<String, Object> resultMap = new HashMap<String, Object>();

        //创建查询对象
        //SimpleQuery query = new SimpleQuery();
        SimpleHighlightQuery query = new SimpleHighlightQuery();

        //设置查询条件 is会对关键字分词
        Criteria criteria = new Criteria("item_title").is(searchMap.get("keywords"));
        query.addCriteria(criteria);

        //设置高亮的配置信息
        HighlightOptions highlightOptions = new HighlightOptions();
        //高亮的域名
        highlightOptions.addField("item_title");
        //设置高亮的起始标签
        highlightOptions.setSimplePrefix("<font style='color:red'>");
        //设置高亮的结束标签
        highlightOptions.setSimplePostfix("</font>");
        query.setHighlightOptions(highlightOptions);

        //按照商品分类进行条件过滤
        if(!StringUtils.isEmpty(searchMap.get("category"))){
            //创建过滤查询对象
            SimpleFilterQuery categoryFilterQuery = new SimpleFilterQuery();
            //创建条件对象：参数1：域名，is之后的是查询的值
            Criteria categoryCriteria = new Criteria("item_category").is(searchMap.get("category"));
            categoryFilterQuery.addCriteria(categoryCriteria);

            //添加过滤条件
            query.addFilterQuery(categoryFilterQuery);
        }
        //按照品牌进行条件过滤
        if(!StringUtils.isEmpty(searchMap.get("brand"))){
            //创建过滤查询对象
            SimpleFilterQuery brandFilterQuery = new SimpleFilterQuery();
            //创建条件对象：参数1：域名，is之后的是查询的值
            Criteria brandCriteria = new Criteria("item_brand").is(searchMap.get("brand"));
            brandFilterQuery.addCriteria(brandCriteria);

            //添加过滤条件
            query.addFilterQuery(brandFilterQuery);
        }

        //按照规格进行条件过滤
        if(searchMap.get("spec") != null){

            //获取每一个规格 及其 值
            Map<String, String> specMap = (Map<String, String>) searchMap.get("spec");
            Set<Map.Entry<String, String>> entries = specMap.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                //创建过滤查询对象
                SimpleFilterQuery filterQuery = new SimpleFilterQuery();
                //创建条件对象：参数1：域名，is之后的是查询的值
                Criteria specCriteria = new Criteria("item_spec_" + entry.getKey()).is(entry.getValue());
                filterQuery.addCriteria(specCriteria);

                //添加过滤条件
                query.addFilterQuery(filterQuery);
            }

        }


        //查询
        //ScoredPage<TbItem> scoredPage = solrTemplate.queryForPage(query, TbItem.class);
        HighlightPage<TbItem> highlightPage = solrTemplate.queryForHighlightPage(query, TbItem.class);

        //处理高亮标题
        List<HighlightEntry<TbItem>> highlighted = highlightPage.getHighlighted();
        if (highlighted != null && highlighted.size() > 0) {
            for (HighlightEntry<TbItem> entry : highlighted) {

                List<HighlightEntry.Highlight> highlights = entry.getHighlights();
                //获取高亮的标题
                if(highlights != null && highlights.size() > 0 && highlights.get(0).getSnipplets() != null){
                    //第一个0则表示高亮的域，取第一个；第二个get(0)表示域中的第一个高亮标题
                    String title = highlights.get(0).getSnipplets().get(0);
                    //设置回商品的标题
                    entry.getEntity().setTitle(title);
                }

            }
        }


        //返回
        resultMap.put("rows", highlightPage.getContent());

        return resultMap;
    }
}
