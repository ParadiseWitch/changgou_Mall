package com.itheima.httpclient;

import entity.HttpClient;
import org.junit.Test;

import java.io.IOException;

/**
 * @ClassName com.itheima.httpclient.HttpClientTest
 * @Description
 * @Author Maid
 * @Date 2020/9/20 0020 16:08
 * @Version v1.0
 */
public class HttpClientTest {

	@Test
	public void testHttpClient() throws IOException {
		String url = "https://api.mch.weixin.qq.com/pay/orderquery";

		HttpClient httpClient = new HttpClient(url);
		String xml = "<xml><name>Maid</name></xml>";
		httpClient.setXmlParam(xml);
		httpClient.setHttps(true);
		httpClient.post();

		String result = httpClient.getContent();
		System.out.println(result);
	}
}
