package com.changgou.oauth.util;

import com.alibaba.fastjson.JSON;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName com.changgou.token.AdminToken
 * @Description
 * @Author Maid
 * @Date 2020/9/16 0016 20:14
 * @Version v1.0
 */
public class AdminToken {
	public static String adminToken(){
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

		String token = jwt.getEncoded();
		return token;
	}
}
