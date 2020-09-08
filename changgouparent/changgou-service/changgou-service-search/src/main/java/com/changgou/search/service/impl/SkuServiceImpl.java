package com.changgou.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.changgou.goods.feign.SkuFeign;
import com.changgou.goods.pojo.Sku;
import com.changgou.search.dao.SkuEsMapper;
import com.changgou.search.pojo.SkuInfo;
import com.changgou.search.service.SkuService;
import com.github.pagehelper.PageInfo;
import entity.Result;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @ClassName com.changgou.search.service.impl.SkuServiceImpl
 * @Description
 * @Author Maid
 * @Date 2020/9/5 0005 15:23
 * @Version v1.0
 */
@Service
public class SkuServiceImpl implements SkuService {
	@Autowired
	private SkuFeign skuFeign;

	@Autowired
	private SkuEsMapper skuEsMapper;

	@Autowired
	private ElasticsearchTemplate elasticsearchTemplate;

	/**
	 * 导入索引库
	 */
	@Override
	public void importData() {
		//	Feign调用查询
		Result<PageInfo> skuResult = skuFeign.findAll();
		//	Sku -> 转成SkuInfo
		//System.out.println(JSON.toJSONString(skuResult));
		List<SkuInfo> skuInfoList = JSON.parseArray(JSON.toJSONString(skuResult.getData().getList()), SkuInfo.class);
		//System.out.println(JSON.toJSONString(skuResult.getData().getList().get(1)));
		for (SkuInfo skuInfo : skuInfoList) {
			Map<String, Object> map = JSON.parseObject(skuInfo.getSpec());
			skuInfo.setSpecMap(map);
		}
		//System.out.println(skuInfoList);
		//  调用Dao实现数据批量注入
		skuEsMapper.saveAll(skuInfoList);
	}

	@Override
	public Map search(Map<String, String> searchMap) {

		//搜索条件封装
		NativeSearchQueryBuilder builder = bulidBasicQuery(searchMap);

		//集合搜索
		Map<String, Object> map = searchList(builder);

		//分类分组查询实现
		List<String> categoryList = searchCategoryList(builder);
		map.put("categoryList",categoryList);

		//查询品牌集合实现
		List<String> brandList = searchBrandList(builder);
		map.put("brandList", brandList);

		//查询spec集合实现
		Map<String, Set<String>> specMap = searchSpecList(builder);
		map.put("specMap",specMap);
		return map;
	}

	/**
	 * 搜索条件封装
	 * @param searchMap
	 * @return
	 */
	private NativeSearchQueryBuilder bulidBasicQuery(Map<String, String> searchMap) {
		NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();

		if(searchMap!=null && searchMap.size()>0){
			String keywords = searchMap.get("keywords");
			if(!StringUtils.isEmpty(keywords)){
				builder.withQuery(QueryBuilders.queryStringQuery(keywords).field("name"));
			}
		}
		return builder;
	}

	/**
	 * 数据结果集搜索
	 * @param builder
	 * @return
	 */
	private Map<String, Object> searchList(NativeSearchQueryBuilder builder) {
		//聚合构造对象(匹配)
		AggregatedPage<SkuInfo> page = elasticsearchTemplate.queryForPage(builder.build(), SkuInfo.class);

		long totalElements = page.getTotalElements();
		int totalPages = page.getTotalPages();
		List<SkuInfo> content = page.getContent();

		Map<String, Object> map = new HashMap<>();
		map.put("rows",content);
		map.put("totalElements",totalElements);
		map.put("totalPages",totalPages);
		return map;
	}

	/**
	 * 分类分组查询
	 * @param builder
	 * @return
	 */
	private List<String> searchCategoryList(NativeSearchQueryBuilder builder) {
		/**
		 * 分组查询
		 * add... 添加一个聚合操作
		 * terms 函数  取别名
		 * 根据那个Field进行分组查询
		 */
		builder.addAggregation(AggregationBuilders.terms("skuCategory").field("categoryName"));
		AggregatedPage<SkuInfo> aggregatedPage = elasticsearchTemplate.queryForPage(builder.build(), SkuInfo.class);

		/**
		 * aggregatedPage.getAggregations() 获取聚合数据，是集合(s)，可以根据多个域进行分组
		 * get("skuCategory")  获取指定域的集合数
		 */
		StringTerms stringTerms = aggregatedPage.getAggregations().get("skuCategory");
		List<String> categoryList = new ArrayList<>();
		for (StringTerms.Bucket bucket : stringTerms.getBuckets()) {
			String categoryName = bucket.getKeyAsString();//其中一个分类名字
			System.out.println(categoryName);
			categoryList.add(categoryName);
		}
		return categoryList;
	}

	/**
	 * 品牌分组查询
	 * @param builder
	 * @return
	 */
	private List<String> searchBrandList(NativeSearchQueryBuilder builder) {
		/**
		 * 品牌集合查询
		 */
		builder.addAggregation(AggregationBuilders.terms("skuBrand").field("brandName"));
		AggregatedPage<SkuInfo> aggregatedPage = elasticsearchTemplate.queryForPage(builder.build(), SkuInfo.class);

		/**
		 */
		StringTerms stringTerms = aggregatedPage.getAggregations().get("skuBrand");
		List<String> brandList = new ArrayList<>();
		for (StringTerms.Bucket bucket : stringTerms.getBuckets()) {
			String brandName = bucket.getKeyAsString();//其中一个品牌名
			System.out.println(brandName);
			brandList.add(brandName);
		}
		return brandList;
	}

	/**
	 * spec集合查询 	 
	 * @param builder
	 * @return
	 */
	private Map<String, Set<String>> searchSpecList(NativeSearchQueryBuilder builder) {

		builder.addAggregation(AggregationBuilders.terms("skuSpec").field("spec.keyword"));
		AggregatedPage<SkuInfo> aggregatedPage = elasticsearchTemplate.queryForPage(builder.build(), SkuInfo.class);


		//1.获取所有规格数据
		StringTerms stringTerms = aggregatedPage.getAggregations().get("skuSpec");
		List<String> specList = new ArrayList<>();
		for (StringTerms.Bucket bucket : stringTerms.getBuckets()) {
			String specName = bucket.getKeyAsString();//其中一个spec
			System.out.println(specName);
			specList.add(specName);
		}
		//3.定义一个Map<String,Set>,key是规格名字，防止重复所以用Map，value是规格值，规格值有多个，所以用集合，为了防止规格重复，用Set去除重复
		Map<String, Set<String>> allSpecMap = new HashMap<>();
		//2.将所有规格数据转换成Map
		for (String specjson : specList) {
			Map<String, String> specMap = JSON.parseObject(specjson, Map.class);


			//4.循环规格的Map，将数据填充到定义的Map<String,Set>中
			for (Map.Entry<String, String> entry : specMap.entrySet()) {
				//4.1 取出当前Map  去除 key  value
				String key = entry.getKey();
				String value = entry.getValue();

				Set specSet = allSpecMap.get(key);
				if (specSet == null) {
					// 对应的Set == null  =>  new Set
					specSet = new HashSet<String>();
				}


				specSet.add(value);
				allSpecMap.put(key,specSet);
			}
		}


		return allSpecMap;
	}



}