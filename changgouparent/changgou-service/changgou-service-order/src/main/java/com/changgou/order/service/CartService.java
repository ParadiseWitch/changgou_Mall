package com.changgou.order.service;

import org.springframework.stereotype.Service;

/**
 * @ClassName com.changgou.service.CartService
 * @Description
 * @Author Maid
 * @Date 2020/9/16 0016 14:56
 * @Version v1.0
 */
public interface CartService {
	public void add(Integer num, Long goodsId,String goodsName);
}
