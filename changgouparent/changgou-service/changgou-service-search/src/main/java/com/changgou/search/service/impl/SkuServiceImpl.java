package com.changgou.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.changgou.goods.feign.SkuFeign;
import com.changgou.goods.pojo.Sku;
import com.changgou.search.dao.SkuEsMapper;
import com.changgou.search.pojo.SkuInfo;
import com.changgou.search.service.SkuService;
import com.github.pagehelper.PageInfo;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}