package com.changgou.pay.mq;


import org.springframework.amqp.core.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * @ClassName com.changgou.pay.mq.MQConfig
 * @Description
 * @Author Maid
 * @Date 2020/9/22 0022 12:49
 * @Version v1.0
 */
@Configuration
public class MQConfig {
	/**
	 * 读取配置文件中的信息的对象
	 */
	@Autowired
	private Environment env;


	/**
	 * 创建队列
	 */
	@Bean
	public Queue orderQueue(){
		return new Queue(env.getProperty("mq.pay.queue.order"));
	}

	/**
	 * 创建交换机
	 */
	@Bean
	public Exchange orderExchange(){
		return new DirectExchange(env.getProperty("mq.pay.exchange.order"),true,false);
	}

	@Bean
	public Binding orderQueueExchange(Queue queue,Exchange exchange){
		return BindingBuilder.bind(queue).to(exchange).with(env.getProperty("mq.pay.routing.key")).noargs();
	}
}
