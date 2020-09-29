package com.changgou.pay.controller;

import com.alibaba.fastjson.JSON;
import com.changgou.pay.service.WeixinPayService;
import com.github.wxpay.sdk.WXPayUtil;
import entity.Result;
import entity.StatusCode;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName com.changgou.pay.controller.WeixinPayController
 * @Description
 * @Author Maid
 * @Date 2020/9/20 0020 18:04
 * @Version v1.0
 */
@RestController
@RequestMapping("/weixin/pay")
public class WeixinPayController {
	@Autowired
	private WeixinPayService weixinPayService;

	@Autowired
	private RabbitTemplate rabbitTemplate;

	/**
	 * 创建二维码
	 * @param parameterMap
	 * 	普通订单
	 * 		exchange: "exchange.order"
	 * 		routingkey:	"queue.order"
	 *  秒杀订单
	 * 		exchange: "exchange.seckillorder"
	 * 		routingkey:	"queue.seckillorder"
	 *
	 * 	exchange + routingkey => json => attach
	 * @return
	 */
	@RequestMapping("/create/native")
	public Result createNative(@RequestParam Map<String,String> parameterMap) {
		Map<String, String> resultMap = weixinPayService.createnative(parameterMap);
		return new Result(true, StatusCode.OK, "创建二维码预付订单成功!",resultMap);
	}

	@GetMapping("/status/query")
	public Result queryStatus(String outtradeno){
		Map map = weixinPayService.queryStatus(outtradeno);
		return new Result(true, StatusCode.OK, "查询支付状态成功!", map);
	}

	@RequestMapping("/notify/url")
	public String notifyurl(HttpServletRequest request) throws Exception {
		//获取网络输入流
		ServletInputStream is = request.getInputStream();

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while((len = is.read(buffer)) != -1){
			baos.write(buffer,0,len);
		}
		byte[] bytes = baos.toByteArray();
		String s = new String(bytes, "UTF-8");
		System.out.println(s);

		//xml 字符串=>map
		Map<String, String> map = WXPayUtil.xmlToMap(s);
		System.out.println(map);

		//获取自定义参数
		String attach = map.get("attach");
		Map<String, String> attachMap = JSON.parseObject(attach, Map.class);

		//发送支付结果给mq
		rabbitTemplate.convertAndSend(attachMap.get("exchange"),attachMap.get("routingkey"), JSON.toJSONString(map));

		String result = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
		return result;
	}

}
