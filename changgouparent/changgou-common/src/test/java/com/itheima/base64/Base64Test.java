package com.itheima.base64;

import org.junit.Test;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Base64;

/**
 * @ClassName com.itheima.base64.Base64Test
 * @Description
 * @Author Maid
 * @Date 2020/9/12 0012 10:21
 * @Version v1.0
 */
public class Base64Test {
	@Test
	public void testEncode() throws UnsupportedEncodingException {
		Base64.Encoder encoder = Base64.getEncoder();
		String str = "maid";
		System.out.println(new String(encoder.encode(str.getBytes("UTF-8"))));
	}

	@Test
	public void testDecode() throws IOException {
		Base64.Decoder decoder = Base64.getDecoder();
		String str = "bWFpZA==";
		System.out.println(new String(decoder.decode(str)));
	}


}
