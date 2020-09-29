package com.changgou.seckill.mq;

import com.alibaba.fastjson.JSON;
import com.changgou.seckill.service.SeckillOrderService;
import entity.SeckillStatus;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * @ClassName com.changgou.search.mq.SeckillMessageListener
 * @Description
 * @Author Maid
 * @Date 2020/9/26 0026 16:42
 * @Version v1.0
 */
@Component
@RabbitListener(queues = "seckillQueue")
public class DelaySeckillMessageListener {
	@Autowired
	private SeckillOrderService seckillOrderService;

	@Autowired
	private RedisTemplate redisTemplate;

	@RabbitHandler
	public void getMessage(String message) {
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
			System.out.println("回滚时间" + simpleDateFormat.format(new Date()));

			SeckillStatus seckillStatus = JSON.parseObject(message, SeckillStatus.class);

			//如果此时没有用户排队信息 => 订单已经处理
			Object userQueueStatus = redisTemplate.boundHashOps("UserQueueStatus").get(seckillStatus.getUsername());
			if (userQueueStatus != null) {
				//TODO:关闭微信支付
				//删除订单
				seckillOrderService.deleteOrder(seckillStatus.getUsername());

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}

