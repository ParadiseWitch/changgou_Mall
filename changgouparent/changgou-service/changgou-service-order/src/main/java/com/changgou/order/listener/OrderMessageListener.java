package com.changgou.order.listener;

import com.alibaba.fastjson.JSON;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @ClassName com.changgou.order.listener.OrderMessageListener
 * @Description
 * @Author Maid
 * @Date 2020/9/22 0022 13:49
 * @Version v1.0
 */
@Component
@RabbitListener(queues = "${mq.pay.queue.order}")
public class OrderMessageListener {
	/**
	 * 支付结果监听
	 * @param message
	 */
	@RabbitHandler
	public void getMessage(String message) {
		//支付结果
		Map<String, String> messageMap = JSON.parseObject(message, Map.class);
		String return_code = messageMap.get("return_code");

		if(return_code.equals("SUCCESS")){
			String result_code = messageMap.get("result_code");
			//订单号
			String out_trade_no = messageMap.get("out_trade_no");

			//支付成功
			if(result_code.equals("SUCCESS")){
				//微信支付交易流水号
				String transaction_id = messageMap.get("transaction_id");


			}else{

			}
		}
	}
}
