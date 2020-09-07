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
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
/*		//1. 获取关键字
		String keywords = searchMap.get("keywords");
		//2. 关键字为空就设置默认值
		if (StringUtils.isEmpty(keywords)){
			keywords = "华为";
		}
		//3.
		NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
		//4. Aggregation ->  聚合
		nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms("skuCategorygroup").field("CategoryName").size(50));

		*/

		NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();

		if(searchMap!=null && searchMap.size()>0){
			String keywords = searchMap.get("keywords");
			if(StringUtils.isEmpty(keywords)){
				builder.withQuery(QueryBuilders.queryStringQuery(keywords).field("name"));
			}
		}
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


}