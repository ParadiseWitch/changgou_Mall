package com.changgou.seckill.task;

import com.alibaba.fastjson.JSON;
import com.changgou.seckill.dao.SeckillGoodsMapper;
import com.changgou.seckill.dao.SeckillOrderMapper;
import com.changgou.seckill.pojo.SeckillGoods;
import com.changgou.seckill.pojo.SeckillOrder;
import entity.IdWorker;
import entity.SeckillStatus;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
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

	@Autowired
	private IdWorker idWorker;

	@Autowired
	private RabbitTemplate rabbitTemplate;
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

			//先到SeckillGoodsCountList——ID队列获取商品的一个信息, 如果能获取, 则可以下单
			Object sgoods = redisTemplate.boundListOps("SeckillGoodsCountList_" + seckillStatus.getGoodsId()).rightPop();
			//如果不能获取队形信息, 则库存为空
			if (sgoods == null) {
				clearUserQueue(username);
				return;
			}

			String namespace = "SeckillGoods_" + time;
			SeckillGoods seckillGoods = (SeckillGoods) redisTemplate.boundHashOps(namespace).get(id);
			//判断有无库存
			if(seckillGoods == null || seckillGoods.getStockCount()<=0){
				throw new Error("已售罄!");
			}
			//创建订单对象
			SeckillOrder seckillOrder = new SeckillOrder();
			seckillOrder.setId(idWorker.nextId());
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
			Thread.sleep(10000);
			System.out.println(Thread.currentThread().getId() + "操作后剩余库存=" + seckillGoods.getStockCount());
			// 解决数据库可能不精准的问题
			Long size = redisTemplate.boundListOps("SeckillGoodsCountList_" + seckillGoods.getGoodsId()).size();
			if(size <= 0){
				//同步数据到mysql
				seckillGoodsMapper.updateByPrimaryKeySelective(seckillGoods);
				//直接删除redis中的秒杀商品
				redisTemplate.boundHashOps(namespace).delete(id);
			}else {
				//redis的秒杀商品库存递减
				redisTemplate.boundHashOps(namespace).put(id,seckillGoods);
			}
			seckillStatus.setOrderId(seckillOrder.getId());
			seckillStatus.setMoney(Float.valueOf(seckillGoods.getCostPrice()));
			seckillStatus.setStatus(2);  // 待付款
			redisTemplate.boundHashOps("UserQueueStatus").put(username,seckillStatus);

			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
			System.out.println("下单时间" + simpleDateFormat.format(new Date()));

			//发送消息给延时队列
			rabbitTemplate.convertAndSend("delaySeckillQueue", (Object) JSON.toJSONString(seckillStatus), new MessagePostProcessor() {
				@Override
				public Message postProcessMessage(Message message) throws AmqpException {
					message.getMessageProperties().setExpiration("10000");
					return message;
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 清理用户排队信息
	 * @param username
	 */
	public void clearUserQueue(String username){
		//排队表示(请求下单次数标识)
		redisTemplate.boundHashOps("UserQueueCount").delete(username);
		//排队状态信息
		redisTemplate.boundHashOps("UserQueueStatus").delete(username);
	}
}