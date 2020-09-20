package com.itheima.weixin;

import com.github.wxpay.sdk.WXPayUtil;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName com.itheima.weixin.WeixinUtilTest
 * @Description
 * @Author Maid
 * @Date 2020/9/20 0020 15:54
 * @Version v1.0
 */
public class WeixinUtilTest {

	@Test
	public void testDemo() throws Exception {
		String str = WXPayUtil.generateNonceStr();
		System.out.println(str);

		Map<String, String> dataMap = new HashMap<>();
		dataMap.put("id","NO.1");
		dataMap.put("name","Maid");
		dataMap.put("moeny","我木有钱了(ノへ￣、)");
		String xmlStr = WXPayUtil.mapToXml(dataMap);
		System.out.println("XML:\n" + xmlStr);

		String signedXml = WXPayUtil.generateSignedXml(dataMap, "itcast");
		System.out.println("有签名的xml:\n" + signedXml);
	}
}
