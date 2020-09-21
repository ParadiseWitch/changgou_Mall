package com.changgou.pay.service.impl;

import com.changgou.WeixinPayApplication;
import com.changgou.pay.service.WeixinPayService;
import com.github.wxpay.sdk.WXPayUtil;
import entity.HttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName com.changgou.pay.service.impl.WeixinPayServiceImpl
 * @Description
 * @Author Maid
 * @Date 2020/9/20 0020 17:18
 * @Version v1.0
 */
@Service
public class WeixinPayServiceImpl implements WeixinPayService {

	@Value("${weixin.appid}")
	private String appid;

	@Value("${weixin.partner}")
	private String partner;

	@Value("${weixin.partnerkey}")
	private String partnerkey;

	@Value("${weixin.notifyurl}")
	private String notifyurl;


	/**
	 * 创建二维码
	 * @param parameterMap
	 * @return
	 */
	@Override
	public Map createnative(Map<String, String> parameterMap) {
		try {
			//参数
			Map<String, String> map = new HashMap<String, String>();
			map.put("appid", appid);
			map.put("mch_id", partner);
			map.put("nonce_str", WXPayUtil.generateNonceStr());
			map.put("body", "啦啦啦啦~爱兰恰~");
			//订单号
			map.put("out_trade_no", parameterMap.get("outtradeno"));
			//交易金额
			map.put("total_fee", parameterMap.get("totalfee"));
			//ip
			map.put("spbill_create_ip", "127.0.0.1");
			//交易回调地址
			map.put("notify_url", notifyurl);
			map.put("trade_type", "NATIVE");
			//签名, Map转XML字符串, 可以携带签名
			String xmlParameters = WXPayUtil.generateSignedXml(map, partnerkey);

			//url
			String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
			HttpClient httpClient = new HttpClient(url);
			httpClient.setHttps(true);
			httpClient.setXmlParam(xmlParameters);
			httpClient.post();

			//res
			String result = httpClient.getContent();

			//toMap
			Map<String, String> resultMap = WXPayUtil.xmlToMap(result);
			return resultMap;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 查询支付状态
	 * @param outtradeno
	 * @return
	 */
	@Override
	public Map queryStatus(String outtradeno) {
		try {
			//参数
			Map<String, String> map = new HashMap<String, String>();
			map.put("appid", appid);
			map.put("mch_id", partner);
			map.put("nonce_str", WXPayUtil.generateNonceStr());
			//订单号
			map.put("out_trade_no", outtradeno);
			//签名, Map转XML字符串, 可以携带签名
			String xmlParameters = WXPayUtil.generateSignedXml(map, partnerkey);

			//url
			String url = "https://api.mch.weixin.qq.com/pay/orderquery";
			HttpClient httpClient = new HttpClient(url);
			httpClient.setHttps(true);
			httpClient.setXmlParam(xmlParameters);
			httpClient.post();

			//res
			String result = httpClient.getContent();

			//toMap
			Map<String, String> resultMap = WXPayUtil.xmlToMap(result);
			return resultMap;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
