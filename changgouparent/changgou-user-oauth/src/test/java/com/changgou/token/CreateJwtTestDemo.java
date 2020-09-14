package com.changgou.token;

import com.alibaba.fastjson.JSON;
import io.jsonwebtoken.JwtBuilder;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName com.changgou.token.CreateJwtTestDemo
 * @Description
 * @Author Maid
 * @Date 2020/9/13 0013 15:13
 * @Version v1.0
 */
public class CreateJwtTestDemo {
	/**
	 * 创建令牌
	 */
	@Test
	public void  testCreateToken(){
		//证书文件路径
		String key_location="changgou68.jks";
		//秘钥库密码
		String key_password="changgou68";
		//秘钥密码
		String keypwd = "changgou68";
		//秘钥别名
		String alias = "changgou68";

		//访问证书文件
		ClassPathResource resource = new ClassPathResource("changgou68.jks");

		//创建一个秘钥工厂
		KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(resource,key_password.toCharArray());

		//读取密钥对
		KeyPair keyPair = keyStoreKeyFactory.getKeyPair(alias, keypwd.toCharArray());

		//获取私钥
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

		//定义payload
		Map<String, Object> payload = new HashMap<>();
		payload.put("id","1");
		payload.put("name","Maid");
		payload.put("roles","ROLE_VIP,ROLE_USER");

		//使用私钥生成令牌
		Jwt jwt = JwtHelper.encode(JSON.toJSONString(payload), new RsaSigner(privateKey));

		String encoded = jwt.getEncoded();
		System.out.println(encoded);
	}

	/**
	 * 校验令牌
	 */
	@Test
	public void testParseToken(){
		String token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlcyI6IlJPTEVfVklQLFJPTEVfVVNFUiIsIm5hbWUiOiJNYWlkIiwiaWQiOiIxIn0.lWm5eVakOndBgk1LNxt7xXrXzVSPfNzV2qAfxN4dc92Ize0wEF13gqJ6F_E2j0xwDCKl2WEtyoBtRka6AGCFIN_ewJ4QoIw8ExLvzr6xOkGMX4ngaI2zAFktgjkaEJEdHzqmMTwPaUSfFwJnjXCuCiOLpS5ZR4zQ1OYnqm8UzwV-Uz1mlJUmzUyVATqwMDKISgVmGxQvKs6Va8rPO09xWbgXJ6EKm5QFXNutKxDk1_H8IpPP4QT_KPIET2lh5XcSiVySwYEBNw2xmlxF4Lpl-beuDfc3HKohd9a_A3wrIDDfrj5XGBdnLkKCtD3HPiUIwJT5PhAxAeNeG2KBb0lf9Q";
		String publicKey = "-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAsCeOgh/AyvfMsT7XhqoY5+eInOUFzgBG9zsKz+g1K1YYe48SngZejUOHNiN40lZOoZCdMVaVYDQxrCYvhoJ6mAp6MWHY2CWjdrDCWg+BFjzY6KlVq4NSiypzhTqtaRHOK4god53Ryb718uJWMVpu+7f1sj/nOreRuNmgMxRcECprx7YzVPw7WhgnZjK3UQoDV9TEQMJ7FgZYTG/cKTAPLJFvVheeozzhxW5Ta5vC232v6lwOdhMjP6fAbKC2CURQlCCDxoln6P1VojvPqA6dkpyJhoFdbqG2aYdKUc7JB4YWsS1Z3NAqBtEib8XDFjNqedEH1tLUHKpXsGEkaBNCAwIDAQAB-----END PUBLIC KEY-----";
		Jwt jwt = JwtHelper.decodeAndVerify(
				token,
				new RsaVerifier(publicKey)
		);
		String claims = jwt.getClaims();
		System.out.println(claims);
	}
}
