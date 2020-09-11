package com.changgou.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.changgou.goods.feign.SkuFeign;
import com.changgou.goods.pojo.Sku;
import com.changgou.search.dao.SkuEsMapper;
import com.changgou.search.pojo.SkuInfo;
import com.changgou.search.service.SkuService;
import com.github.pagehelper.PageInfo;
import entity.Result;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
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
		Map<String, Object> map = searchList(searchMap,builder);

		Map<String, Object> groupMap = searchGroupList(builder, searchMap);
		map.putAll(groupMap);
		return map;
	}

	/**
	 * 分组查询 -> 分类，品牌，规格分组
	 * @param builder
	 * @return
	 */
	private Map<String, Object> searchGroupList(NativeSearchQueryBuilder builder, Map<String, String> searchMap) {
		/**
		 * 分组查询
		 * add... 添加一个聚合操作
		 * terms 函数  取别名
		 * 根据那个Field进行分组查询
		 */
		if(searchMap == null || StringUtils.isEmpty(searchMap.get("category")))
			builder.addAggregation(AggregationBuilders.terms("skuCategory").field("categoryName"));
		if(searchMap == null || StringUtils.isEmpty(searchMap.get("brand"))) {
			builder.addAggregation(AggregationBuilders.terms("skuBrand").field("brandName"));
		}
		builder.addAggregation(AggregationBuilders.terms("skuSpec").field("spec.keyword"));
		AggregatedPage<SkuInfo> aggregatedPage = elasticsearchTemplate.queryForPage(builder.build(), SkuInfo.class);

		//定义一个Map，存储所有分组数据
		Map<String, Object> groupMapResult = new HashMap<>();

		if(searchMap == null || StringUtils.isEmpty(searchMap.get("category"))){
			StringTerms catygoryTerms = aggregatedPage.getAggregations().get("skuCategory");
			List<String> categoryList = getGroupList(catygoryTerms);
			groupMapResult.put("categoryList",categoryList);
		}
		if(searchMap == null || StringUtils.isEmpty(searchMap.get("brand"))){
			StringTerms brandTerms = aggregatedPage.getAggregations().get("skuBrand");
			List<String> brandList = getGroupList(brandTerms);
			groupMapResult.put("brandList",brandList);
		}
		StringTerms specTerms = aggregatedPage.getAggregations().get("skuSpec");
		List<String> specList = getGroupList(specTerms);
		Map<String, Set<String>> specMap = putAllSpec(specList);

		groupMapResult.put("specList",specMap);

		return groupMapResult;
	}


	/**
	 * 获取分组集合数据
	 * @param stringTerms
	 * @return
	 */
	private List<String> getGroupList(StringTerms stringTerms) {
		List<String> groupList = new ArrayList<>();
		for (StringTerms.Bucket bucket : stringTerms.getBuckets()) {
			String fieldName = bucket.getKeyAsString();//其中一个分类名字
			groupList.add(fieldName);
		}
		return groupList;
	}


	private NativeSearchQueryBuilder bulidBasicQuery(Map<String, String> searchMap) {
		NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();

		if(searchMap!=null && searchMap.size()>0){
			String keywords = searchMap.get("keywords");
			if(!StringUtils.isEmpty(keywords)){
				builder.withQuery(QueryBuilders.queryStringQuery(keywords).field("name"));
			}
			if (!StringUtils.isEmpty(searchMap.get("brand"))) {
				builder.withQuery(QueryBuilders.queryStringQuery(searchMap.get("brand")).field("brandName"));
			}

			if (!StringUtils.isEmpty(searchMap.get("category"))) {
				builder.withQuery(QueryBuilders.queryStringQuery(searchMap.get("category")).field("categoryName"));
			}



			for (Map.Entry<String, String> entry : searchMap.entrySet()) {
				String key = entry.getKey();
				if (key.startsWith("spec_")) {
					//es中type后面有 .keywords 的不分词
					builder.withQuery(QueryBuilders.queryStringQuery(
							entry.getValue())
							.field("specMap." + key.substring(5)));
				}
			}

			//价格区间
			BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

			if (!StringUtils.isEmpty(searchMap.get("price"))) {

				String price = searchMap.get("price");
				price = price.replace("元", "").replace("以上", "");
				String[] prices = price.split("-");
				boolQueryBuilder.must(QueryBuilders.rangeQuery("price").gt(Integer.parseInt(prices[0])));
				if(prices.length == 2){
					boolQueryBuilder.must(QueryBuilders.rangeQuery("price").lte(Integer.parseInt(prices[1])));
				}
			}

			builder.withFilter(boolQueryBuilder);


			//分页
			Integer pageNum = 1;
			Integer size = 30;
			if(!StringUtils.isEmpty(searchMap.get("pageNum"))){
				try {
					pageNum = Integer.valueOf(searchMap.get("pageNum"));
				} catch (NumberFormatException e) {
					e.printStackTrace();
					pageNum = 1;
				}

			}
			builder.withPageable(PageRequest.of(pageNum-1,size));


			//搜索排序
			String sortRule = searchMap.get("sortRule");
			String sortField = searchMap.get("sortField");
			if (!StringUtils.isEmpty(sortRule) && !StringUtils.isEmpty(sortField)) {
				builder.withSort(new FieldSortBuilder(sortField).order(SortOrder.valueOf(sortRule)));
			}
		}
		return builder;
	}

	/**
	 * 数据结果集搜索
	 * @param builder
	 * @return
	 */
	private Map<String, Object> searchList(Map<String, String> searchMap, NativeSearchQueryBuilder builder) {

		//设置高亮条件
		HighlightBuilder.Field field = new HighlightBuilder.Field("name");
		field.preTags("<em style=\"color:red\">");
		field.postTags("</em>");
		field.fragmentSize(100);

		builder.withHighlightFields(field);

		/**
		 * 执行搜索，响应结果给我
		 * 1)搜索条件封装对象
		 * 2)搜索的结果集（集合数据）需要转换的类型
		 * 3)AggregatedPage<SkuInfo>:搜索结果集的封装
		 */
		//AggregatedPage<SkuInfo> page = elasticsearchTemplate.queryForPage(builder.build(), SkuInfo.class);
		AggregatedPage<SkuInfo> page = elasticsearchTemplate
				.queryForPage(
						builder.build(),
						SkuInfo.class,
						new SearchResultMapper() {
							@Override
							public <T> AggregatedPage<T> mapResults(SearchResponse searchResponse, Class<T> aClass, Pageable pageable) {
								//存储所有转换后的高亮数据对象
								List<T> list = new ArrayList<T>();
								//如果没有结果返回为空
								if (searchResponse.getHits() == null || searchResponse.getHits().getTotalHits() <= 0) {
									return new AggregatedPageImpl<T>(list);
								}
								//执行查询  获取所有数据 -> 结果集[非高亮数据|高亮数据]
								for (SearchHit hit : searchResponse.getHits()) {
									// 分析结果集数据，获取高亮数据
									SkuInfo skuInfo = JSON.parseObject(hit.getSourceAsString(), SkuInfo.class);

									// 分析结果集数据，获取非高亮数据 -> 只有某个域的高亮数据
									HighlightField highlightField = hit.getHighlightFields().get("name");

									if(highlightField != null && highlightField.getFragments() != null){
										//高亮数据读取出来
										Text[] fragments = highlightField.getFragments();
										StringBuffer stringBuffer = new StringBuffer();
										for (Text fragment : fragments) {
											stringBuffer.append(fragment.toString());
										}
										skuInfo.setName(stringBuffer.toString());
									}
									list.add((T)skuInfo);
								}
								return new AggregatedPageImpl<T>(
										list,
										pageable,
										searchResponse.getHits().getTotalHits(),
										searchResponse.getAggregations(),
										searchResponse.getScrollId());
							}
						}
				);

		//获取搜索封装信息
		NativeSearchQuery query = builder.build();
		//可能会报错:UnsupportedOperationException
		int pageNumber,pageSize;
		if(!StringUtils.isEmpty(searchMap.get("pageNum"))){
			Pageable pageable = query.getPageable();
			try{
				pageNumber = pageable.getPageNumber();
				pageSize = pageable.getPageSize();
			}catch (Exception e){
				e.printStackTrace();
				pageNumber = 0;
				pageSize = 30;
			}
		}else {
			pageNumber = 0;
			pageSize = 30;
		}


		long totalElements = page.getTotalElements();
		int totalPages = page.getTotalPages();
		List<SkuInfo> content = page.getContent();

		Map<String, Object> map = new HashMap<>();
		map.put("rows",content);
		map.put("totalElements",totalElements);
		//FIXME: 无请求参数的时候,totalPages = 1  但是参数pageNum=1时 => totalNum=67
		map.put("totalPages",totalPages);
		//
		map.put("pageNumber",pageNumber);
		map.put("pageSize",pageSize);
		return map;
	}

	private Map<String, Set<String>> putAllSpec(List<String> specList) {
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