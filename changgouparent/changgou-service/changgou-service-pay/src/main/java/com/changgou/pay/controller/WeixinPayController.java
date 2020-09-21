package com.changgou.pay.controller;

import com.changgou.pay.service.WeixinPayService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

	@RequestMapping("/create/native")
	public Result createNative(@RequestParam Map<String,String> parameterMap) {
		Map<String, String> resultMap = weixinPayService.createnative(parameterMap);
		return new Result(true, StatusCode.OK, "创建二维码预付订单成功!",resultMap);
	}
}
