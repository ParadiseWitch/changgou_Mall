package com.changgou.order.service.impl;

import com.changgou.goods.feign.SkuFeign;
import com.changgou.goods.feign.SpuFeign;
import com.changgou.goods.pojo.Sku;
import com.changgou.goods.pojo.Spu;
import com.changgou.order.pojo.OrderItem;
import com.changgou.order.service.CartService;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName com.changgou.service.impl.CartServiceImpl
 * @Description
 * @Author Maid
 * @Date 2020/9/16 0016 14:57
 * @Version v1.0
 */
@Service
public class CartServiceImpl implements CartService {
	@Autowired
	private SkuFeign skuFeign;

	@Autowired
	private SpuFeign spuFeign;

	@Autowired
	private RedisTemplate redisTemplate;


	@Override
	public void add(Integer num, Long goodsId, String username) {
		Result<Sku> skuResult = skuFeign.findById(goodsId.toString());
		Sku sku = skuResult.getData();
		Result<Spu> spuResult = spuFeign.findById(sku.getSpuId());
		Spu spu = spuResult.getData();

		OrderItem orderItem = createOrderItem(num, goodsId, sku, spu);

		//将购物车存入redis
		redisTemplate.boundHashOps("Cart_"+username).put(goodsId,orderItem);




	}

	/**
	 * 查询所有购物车数据
	 * @param username
	 * @return
	 */
	@Override
	public List<OrderItem> list(String username) {
		return redisTemplate.boundHashOps("Cart_" + username).values();
	}

	private OrderItem createOrderItem(Integer num, Long goodsId, Sku sku, Spu spu) {
		OrderItem orderItem = new OrderItem();
		orderItem.setCategoryId1(spu.getCategory1Id());
		orderItem.setCategoryId2(spu.getCategory2Id());
		orderItem.setCategoryId3(spu.getCategory3Id());
		orderItem.setSkuId(goodsId.toString());
		orderItem.setSpuId(spu.getId());
		orderItem.setName(sku.getName());
		orderItem.setPrice(sku.getPrice());
		orderItem.setNum(num);
		orderItem.setMoney(num * orderItem.getPrice());
		orderItem.setImage(spu.getImage());
		return orderItem;
	}
}
