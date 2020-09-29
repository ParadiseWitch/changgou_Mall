package com.changgou.seckill.mq;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @ClassName com.changgou.seckill.mq.QueueConfig
 * @Description 1. 延时超时队列 => 负责数据暂时存储	Queue1
 * 2. 真正得监听的消息队列			Queue2
 * 3. 创建交换机
 * @Author Maid
 * @Date 2020/9/29 0029 11:22
 * @Version v1.0
 */
@Configuration
public class QueueConfig {
	/**
	 * 1. 延时超时队列 => 负责数据暂时存储	Queue1
	 * @return
	 */
	@Bean
	public Queue delaySeckillQueue(){
		return QueueBuilder.durable("delaySeckillQueue")
				.withArgument("x-dead-letter-exchange", "seckillExchange")        // 消息超时进入死信队列，绑定死信队列交换机
				.withArgument("x-dead-letter-routing-key", "seckillQueue")   // 绑定指定的routing-key
				.build();

	}


	/**
	 * 2. 真正得监听的消息队列				Queue2
	 * @return
	 */
	@Bean
	public Queue seckillQueue(){
		return new Queue("seckillQueue");
	};

	/**
	 * 3. 创建交换机
	 * @return
	 */
	@Bean
	public Exchange seckillExchange(){
		return new DirectExchange("seckillExchange");
	}

	/**
	 * 队列绑定交换机
	 * @return
	 */
	@Bean
	public Binding seckillQueueBindExchange(Queue seckillQueue, Exchange seckillExchange){
		return BindingBuilder.bind(seckillQueue).to(seckillExchange).with("seckillQueue").noargs();
	}

}
