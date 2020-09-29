package com.changgou.seckill.mq;

import com.alibaba.fastjson.JSON;
import com.changgou.seckill.service.SeckillOrderService;
import com.github.wxpay.sdk.WXPayUtil;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @ClassName com.changgou.search.mq.SeckillMessageListener
 * @Description
 * @Author Maid
 * @Date 2020/9/26 0026 16:42
 * @Version v1.0
 */
@Component
@RabbitListener(queues = "${mq.pay.queue.seckillorder}")
public class SeckillMessageListener {
	@Autowired
	private SeckillOrderService seckillOrderService;
	@RabbitHandler
	public void getMessage(String message) {
		try {
			//System.out.println("秒杀监听消息:" + message);
			// message => map
			Map<String, String> resultMap = JSON.parseObject(message,Map.class);

			//return_code ->通信标识
			String return_code = resultMap.get("return_code");
			//订单号
			String outtradeno = resultMap.get("outtradeno");
			//自定义数据
			String attach = resultMap.get("attach");

			Map<String,String> attachMap = JSON.parseObject(attach, Map.class);
			if (return_code.equals("SUCCESS")) {
				//result_code->业务结果->SUCCESS->改订单状态
				String result_code = resultMap.get("result_code");
				if(result_code.equals("SUCCESS")){
					//改订单状态
					seckillOrderService.updataPayStatus(attachMap.get("username"),resultMap.get("transaction_id"),resultMap.get("time_end"));

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}

