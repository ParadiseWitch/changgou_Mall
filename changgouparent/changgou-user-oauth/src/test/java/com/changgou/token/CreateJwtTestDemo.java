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
		payload.put("nikename","Maid");
		payload.put("address","sz");
		payload.put("authorities",new String[]{"admin","oauth"});

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
		String token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJhZGRyZXNzIjoic3oiLCJuaWtlbmFtZSI6Ik1haWQiLCJhdXRob3JpdGllcyI6WyJhZG1pbiIsIm9hdXRoIl19.QUzi8IETF9GgjRGd81GNx7qCGnD0g8qaVeyAZX_-J5Bdu4N7OUe9Ys0pkN9iU9gQTJAql0Nj2mGeq9zDk27dzW8RVnoHtg5nKBWtAfJETuISiAdsEpJWfTbJGCE2Fp44xv3cPjGnFjRpKp4UwkjkxLNAs6IIk0okIdGip5a_odw73aewsHHF1T9vmKricvLEG_PkW3FDJKCIGhHlwHWJuLs8Y8p9UNgEWWADGjuUT900dZG3DpFYFMOAwtHfBTFo2iP3ODd3OHQeKTXh9IpNlXIwER0soRg71BchNcALtaJGg7iz1Ihzi3RGALmFuZUkcgIpbEPb8z3aF_EusQVH3g";
		String publicKey = "-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAsCeOgh/AyvfMsT7XhqoY5+eInOUFzgBG9zsKz+g1K1YYe48SngZejUOHNiN40lZOoZCdMVaVYDQxrCYvhoJ6mAp6MWHY2CWjdrDCWg+BFjzY6KlVq4NSiypzhTqtaRHOK4god53Ryb718uJWMVpu+7f1sj/nOreRuNmgMxRcECprx7YzVPw7WhgnZjK3UQoDV9TEQMJ7FgZYTG/cKTAPLJFvVheeozzhxW5Ta5vC232v6lwOdhMjP6fAbKC2CURQlCCDxoln6P1VojvPqA6dkpyJhoFdbqG2aYdKUc7JB4YWsS1Z3NAqBtEib8XDFjNqedEH1tLUHKpXsGEkaBNCAwIDAQAB-----END PUBLIC KEY-----";
		Jwt jwt = JwtHelper.decodeAndVerify(
				token,
				new RsaVerifier(publicKey)
		);
		String claims = jwt.getClaims();
		System.out.println(claims);
	}
}
