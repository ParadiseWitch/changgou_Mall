package com.changgou.order.mq.queue;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @ClassName com.changgou.order.mq.queue.QueueConfig
 * @Description
 * @Author Maid
 * @Date 2020/9/22 0022 17:51
 * @Version v1.0
 */
@Configuration
public class QueueConfig {
	/**
	 * 创建Queue1延时队列，
	 * @return
	 */
	@Bean
	public Queue orderDelayQueue(){
		return QueueBuilder.durable("orderDelayQueue")
				.withArgument("x-dead-letter-exchange", "orderListenerExchange")        // 消息超时进入死信队列，绑定死信队列交换机
				.withArgument("x-dead-letter-routing-key", "orderListenerQueue")   // 绑定指定的routing-key
				.build();
	}

	/**
	 * 创建Queue2
	 * @return
	 */
	@Bean
	public Queue orderListenerQueue() {
		return new Queue("orderListenerQueue",true);
	}

	/**
	 * 创建交换机
	 * @return
	 */
	@Bean
	public Exchange orderListenerExchange(){
		return new DirectExchange("orderListenerExchange");
	}

	/**
	 * 队列Queue2绑定Exchange
	 * @param orderListenerQueue
	 * @param orderListenerExchange
	 * @return
	 */
	 @Bean
	 public Binding orderListenerBinding(Queue orderListenerQueue, Exchange orderListenerExchange) {
		 return BindingBuilder.bind(orderListenerQueue)
				 .to(orderListenerExchange)
				 .with("orderListenerQueue").noargs();
	 }

}
