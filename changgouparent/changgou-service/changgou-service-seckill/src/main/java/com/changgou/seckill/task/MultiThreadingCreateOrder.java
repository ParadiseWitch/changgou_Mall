package com.changgou.seckill.task;

import com.changgou.seckill.dao.SeckillGoodsMapper;
import com.changgou.seckill.dao.SeckillOrderMapper;
import com.changgou.seckill.pojo.SeckillGoods;
import com.changgou.seckill.pojo.SeckillOrder;
import entity.SeckillStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @ClassName com.changgou.seckill.task.MultiThreadingCreateOrder
 * @Description
 * @Author Maid
 * @Date 2020/9/25 0025 12:44
 * @Version v1.0
 */
@Component
public class MultiThreadingCreateOrder {
	@Autowired
	private SeckillOrderMapper seckillOrderMapper;

	@Autowired
	private RedisTemplate redisTemplate;

	@Autowired
	private SeckillGoodsMapper seckillGoodsMapper;

	/***
	 * 多线程下单操作
	 */
	@Async
	public void createOrder(){
		try {
			//延时一段时间再取出 可以模拟堵塞也方便观察调试
			Thread.sleep(10000);
			//从redis队列中获取用户排队信息
			System.out.println("移出队列");
			SeckillStatus seckillStatus = (SeckillStatus) redisTemplate.boundListOps("SeckillOrderQueue").rightPop();
			if (seckillStatus == null) {
				return;
			}
			String time = seckillStatus.getTime();
			Long id = seckillStatus.getGoodsId();
			String username = seckillStatus.getUsername();

			String namespace = "SeckillGoods_" + time;
			SeckillGoods seckillGoods = (SeckillGoods) redisTemplate.boundHashOps(namespace).get(id);
			//判断有无库存
			if(seckillGoods == null || seckillGoods.getStockCount()<=0){
				throw new Error("已售罄!");
			}
			//创建订单对象
			SeckillOrder seckillOrder = new SeckillOrder();
			seckillOrder.setSeckillId(id);                      //商品id
			seckillOrder.setMoney(seckillGoods.getCostPrice()); //支付金额
			seckillOrder.setUserId(username);                   //用户
			seckillOrder.setCreateTime(new Date());             //订单创建时间
			seckillGoods.setStatus("0");                        //未支付

			/**
			 * 存储订单对象
			 * 1. 一个用户只允许有一个未支付订单
			 * 2. 订单存到redis
			 *      "SeckillOrder"
			 *          username -> SeckillOrder
			 */
			redisTemplate.boundHashOps("SeckillOrder").put(username,seckillOrder);
			/**
			 * 库存递减
			 */
			seckillGoods.setStockCount(seckillGoods.getStockCount()-1);
			if(seckillGoods.getStockCount() <= 0){
				//同步数据到mysql
				seckillGoodsMapper.updateByPrimaryKeySelective(seckillGoods);
				//直接删除redis中的秒杀商品
				redisTemplate.boundHashOps(namespace).delete(id);
			}else {
				//redis的秒杀商品库存递减
				redisTemplate.boundHashOps(namespace).put(id,seckillGoods);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}