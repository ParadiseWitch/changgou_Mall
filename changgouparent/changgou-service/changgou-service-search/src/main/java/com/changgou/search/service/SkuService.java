package com.changgou.search.service;

import java.util.Map;

/**
 * @ClassName com.changgou.search.service.SkuService
 * @Description
 * @Author Maid
 * @Date 2020/9/5 0005 15:22
 * @Version v1.0
 */
public interface SkuService {
	public void importData();
	public Map search(Map<String,String> searchMap);
}
