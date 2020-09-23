package com.changgou.order.mq.listener;

import com.alibaba.fastjson.JSON;
import com.changgou.order.service.OrderService;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Map;

/**
 * @ClassName com.changgou.order.mq.listener.OrderMessageListener
 * @Description
 * @Author Maid
 * @Date 2020/9/22 0022 13:49
 * @Version v1.0
 */
@Component
@RabbitListener(queues = "${mq.pay.queue.order}")
public class OrderMessageListener {
	@Autowired
	private OrderService orderService;
	/**
	 * 支付结果监听
	 * @param message
	 */
	@RabbitHandler
	public void getMessage(String message) throws ParseException {
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
				String time_end = messageMap.get("time_end");
				//修改订单状态
				orderService.updateStatus(out_trade_no,time_end,transaction_id);
			}else{
				//支付失败
				//TODO:关闭订单 (https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=9_3)
				//删除订单
				orderService.delete(out_trade_no);
			}
		}
	}
}
