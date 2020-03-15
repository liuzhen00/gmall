package com.zl.gmall.search.service;

import com.alibaba.fastjson.JSON;
import com.atguigu.core.bean.Resp;
import com.zl.gmall.pms.entity.BrandEntity;
import com.zl.gmall.pms.entity.CategoryEntity;
import com.zl.gmall.pms.entity.ProductAttrValueEntity;
import com.zl.gmall.pms.entity.SkuInfoEntity;
import com.zl.gmall.search.feign.GmallPmsFeign;
import com.zl.gmall.search.feign.GmallWmsFeign;
import com.zl.gmall.search.repository.GoodsRepository;
import com.zl.gmall.search.vo.*;
import com.zl.gmall.wms.entity.WareSkuEntity;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.nested.ParsedNested;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author shkstart
 * @create 2020-02-28 22:52
 */
@Service
public class SearcherService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;



    public SearchResponseVo search(SearchParamVo searchParamVo) throws IOException {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        SearchRequest searchRequest = new SearchRequest(new String[]{"goods"}, builderSource(searchParamVo));

        SearchResponse response = this.restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchResponseVo searchResponseVo = parseResult(response);
        searchResponseVo.setPageNum(searchParamVo.getPageNum());
        searchResponseVo.setPageSize(searchResponseVo.getPageSize());
        return searchResponseVo;

    }
     //解析结果集
    private SearchResponseVo parseResult(SearchResponse response) {
        SearchResponseVo searchResponseVo = new SearchResponseVo();
        SearchHits hits = response.getHits();
        searchResponseVo.setTotal(hits.totalHits);//获取总记录数
        //解析Hit获取查询记录
        /**
         * 遍历hitsHits，并且解析出结果集
         * 首先将遍历的结果集转换成为字符串，然后将转成的字符串
         * 转换成JSON格式，使用GoodsVo的class文件，因为页面显示的
         * 数据需要GoodsVo封装的内容，然后对高亮内容进行解析
         * 获取出高亮 再设置给setTitle，再复制给goodsVo
         */
        SearchHit[] hitsHits = hits.getHits();
        ArrayList<GoodsVo> goodsVos = new ArrayList<>();
        //遍历hitsHits数组，把hitsHits转为GoodsVo
        for (SearchHit hitsHit : hitsHits) {
            //页面渲染只需要skuId、pic、title、price
            String goodsVoJson = hitsHit.getSourceAsString();
            //对获取的JSON数据进行反序列化
            GoodsVo goodsVo = JSON.parseObject(goodsVoJson, GoodsVo.class);
            //对获取高亮结果集
            HighlightField highlightField = hitsHit.getHighlightFields().get("title");
            //??????
            Text fragment=highlightField.getFragments()[0];
            goodsVo.setTitle(fragment.string());
            goodsVos.add(goodsVo);

        }
        searchResponseVo.setData(goodsVos);

        //解析品牌
        Map<String, Aggregation> aggsMap = response.getAggregations().getAsMap();
        //获取品牌的聚合 并强转为可解析的品牌聚合
        ParsedLongTerms brandIdAgg =(ParsedLongTerms) aggsMap.get("brandIdAgg");
        //        //获取品牌聚合下的所有桶
        List<? extends Terms.Bucket> buckets = brandIdAgg.getBuckets();
        if(!CollectionUtils.isEmpty(buckets)){
            List<String> brandValues = buckets.stream().map(bucket -> {
                Map<String, Object> map = new HashMap<>();
                long brandId = ((Terms.Bucket) bucket).getKeyAsNumber().longValue();
                map.put("id", brandId);
                ParsedStringTerms brandNameAgg = (ParsedStringTerms) bucket.getAggregations().get("brandNameAgg");
                map.put("name", brandNameAgg);
                return JSON.toJSONString(map);


            }).collect(Collectors.toList());
            SearchResponseAttrVo brandVo = new SearchResponseAttrVo();
            brandVo.setAttrName("品牌");
            brandVo.setAttrValues(brandValues);
            searchResponseVo.setBrand(brandVo);
        }

         //解析聚合分类
        ParsedLongTerms categoryIdAgg = (ParsedLongTerms)aggsMap.get("categoryIdAgg");
        //获取桶
        List<? extends Terms.Bucket> categoryIdAggBuckets = categoryIdAgg.getBuckets();
        if(!CollectionUtils.isEmpty(categoryIdAggBuckets)){
            List<String> categoryValues = categoryIdAggBuckets.stream().map(categoryBuckets -> {
                //遍历获取分类的值
                Map<String, Object> map = new HashMap<>();
                long categoryId = ((Terms.Bucket) categoryBuckets).getKeyAsNumber().longValue();
                map.put("id", categoryId);
                //获取分类的名称
                ParsedStringTerms categoryNameAgg = ((ParsedStringTerms) categoryBuckets.getAggregations().get("categoryNameAgg"));
                map.put("name", categoryNameAgg.getBuckets().get(0).getKeyAsString());
                return JSON.toJSONString(map);
            }).collect(Collectors.toList());
           SearchResponseAttrVo categoryVo=new SearchResponseAttrVo();
            categoryVo.setAttrName("分类");
            categoryVo.setAttrValues(categoryValues);
           searchResponseVo.setCategory(categoryVo);

        }
        //解析聚合结果集获取规格参数
        ParsedNested attrsAgg =(ParsedNested) aggsMap.get("attrsAgg");
        //获取嵌套聚合中的规格参数ID子聚合
        ParsedLongTerms attrIdAgg =(ParsedLongTerms) attrsAgg.getAggregations().get("attrIdAgg");
        //获取桶
        List<? extends Terms.Bucket> attrIdAggBuckets = attrIdAgg.getBuckets();
        if(!CollectionUtils.isEmpty(attrIdAggBuckets)){
            List<SearchResponseAttrVo> attrVoList = attrIdAggBuckets.stream().map(attrIdAggBucket -> {
                SearchResponseAttrVo searchResponseAttrVo = new SearchResponseAttrVo();
                searchResponseAttrVo.setAttrId(((Terms.Bucket) attrIdAggBucket).getKeyAsNumber().longValue());
                ParsedStringTerms attrNameAgg = ((Terms.Bucket) attrIdAggBucket).getAggregations().get("attrNameAgg");
                searchResponseAttrVo.setAttrName(attrNameAgg.getBuckets().get(0).toString());
                ParsedStringTerms attrValueAgg = ((Terms.Bucket) attrIdAggBucket).getAggregations().get("attrValueAgg");
                List<? extends Terms.Bucket> valueAggBuckets = attrValueAgg.getBuckets();
                if (!CollectionUtils.isEmpty(valueAggBuckets)) {
                    List<String> attsValue = valueAggBuckets.stream().map(Terms.Bucket::getKeyAsString).collect(Collectors.toList());
                    searchResponseAttrVo.setAttrValues(attsValue);
                }
                return searchResponseAttrVo;
            }).collect(Collectors.toList());

            searchResponseVo.setAttrs(attrVoList);

        }
        return searchResponseVo;
    }

    //构建查询条件
    public SearchSourceBuilder builderSource(SearchParamVo searchParamVo){
          SearchSourceBuilder sourceBuilder=new SearchSourceBuilder();
          //1、构建查询条件和过滤条件
        BoolQueryBuilder boolQueryBuilder= QueryBuilders.boolQuery();
        sourceBuilder.query(boolQueryBuilder);
          //1.1构建匹配查询(keywords)
        String keyword = searchParamVo.getKeyword();
        if(StringUtils.isEmpty(keyword)){
            return null;
        }
        boolQueryBuilder.must(QueryBuilders.matchQuery("title",keyword).operator(Operator.AND));
        //1.2构建过滤条件
          //1.2.1构建品牌过滤
        Long[] brandId = searchParamVo.getBrandId();
        if(brandId!=null&&brandId.length!=0){
            boolQueryBuilder.filter(QueryBuilders.termsQuery("brandId",brandId));
        }
        //1.2.2构建分类过滤
        Long[] categoryId = searchParamVo.getCategoryId();
        if(categoryId!=null&&categoryId.length!=0){
            boolQueryBuilder.filter(QueryBuilders.termsQuery("categoryId",categoryId));
        }
        //1.2.3构建价格区间
        Double priceFrom = searchParamVo.getPriceFrom();
        Double priceTo = searchParamVo.getPriceTo();
        if(priceFrom!=null||priceFrom!=null){
            RangeQueryBuilder rangeQueryBuilder=QueryBuilders.rangeQuery("price");
            if(priceFrom!=null){
                rangeQueryBuilder.gt(priceFrom);
            }
            if(priceTo!=null){
                rangeQueryBuilder.lt(priceTo);
            }
            boolQueryBuilder.filter(rangeQueryBuilder);
        }
        //1.2.4构建是否有货的过滤
        Boolean store = searchParamVo.getStore();
        if(store!=null){
            boolQueryBuilder.filter(QueryBuilders.termQuery("store",store));
        }
        //1.2.5构建规格参数的嵌套过滤// 33:3000-4000-50003
        String[] props = searchParamVo.getProps();
        if(props!=null&&props.length!=0){
            for (String prop : props) {
                String[] attr = StringUtils.split(prop, ":");
                if(attr==null||attr.length!=2){
                    continue;
                }
                BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
                boolQuery.must(QueryBuilders.termQuery("attrs.attrId",attr[0]));
                boolQuery.must(QueryBuilders.termsQuery("attrs.attrValue",attr[1]));
                boolQueryBuilder.filter(QueryBuilders.nestedQuery("attrs",boolQuery, ScoreMode.None));

            }
        }
        //2、构建排序  order=1:desc  （0 ：得分 1：价格  2：销量 3：新品）
        String order = searchParamVo.getOrder();
        if(StringUtils.isNotBlank(order)){
            String[] sorts = StringUtils.split(order,":");
            if(sorts!=null&&sorts.length==2){
                String sortField="_score";
                switch (sorts[0]){
                    case "1":sortField="price";break;
                    case "2":sortField="sale";break;
                    case "3":sortField="createTime";break;
                    default:break;
                }
                sourceBuilder.sort(sortField,StringUtils.equals("desc",sorts[1])? SortOrder.DESC:SortOrder.ASC);
            }
        }

          //3、构建分页
        Integer pageNum = searchParamVo.getPageNum();
        Integer pageSize = searchParamVo.getPageSize();
        sourceBuilder.from((pageNum-1)*pageSize);
        sourceBuilder.size(pageSize);
         //4、构建高亮
        sourceBuilder.highlighter(new HighlightBuilder().field("title")
           .preTags("<font style='color:red'>")
            .postTags("</font>"));
          //5、构建聚合


          //5.1品牌聚合
        sourceBuilder.aggregation(AggregationBuilders.terms("brandIdAgg").field("brandId")
                .subAggregation(AggregationBuilders.terms("brandNameAgg").field("brandName")));

        //5.2分类聚合
        sourceBuilder.aggregation(AggregationBuilders.terms("categoryIdAgg").field("categoryId")
                .subAggregation(AggregationBuilders.terms("categoryNameAgg").field("categoryName")));

        //5.3规格参数的聚合
        sourceBuilder.aggregation(AggregationBuilders.nested("attrsAgg","attrs")
                .subAggregation(AggregationBuilders.terms("attrIdAgg").field("attrs.attrId")
                        .subAggregation(AggregationBuilders.terms("attrNameAgg").field("attrs.attrName")
                                .subAggregation(AggregationBuilders.terms("attrValueAgg").field("attrs.attrValue")))));

        System.out.println(sourceBuilder.toString());
        return sourceBuilder;

    }

    //创建索引
    public void createIndex(Long id){

    }
}
