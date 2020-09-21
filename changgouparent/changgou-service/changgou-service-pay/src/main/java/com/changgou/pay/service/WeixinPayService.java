package com.changgou.pay.service;

import java.util.Map;

/**
 * @ClassName com.changgou.pay.service.WeixinPayService
 * @Description
 * @Author Maid
 * @Date 2020/9/20 0020 17:19
 * @Version v1.0
 */
public interface WeixinPayService {
	/**
	 * 创建二维码
	 * @param parameterMap
	 * @return
	 */
	Map createnative(Map<String,String> parameterMap);
}
