package com.itheima.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName com.itheima.jwt.jwtTest
 * @Description
 * @Author Maid
 * @Date 2020/9/12 0012 10:56
 * @Version v1.0
 */
public class jwtTest {
	@Test
	public void testCreateToken(){
		JwtBuilder builder = Jwts.builder()
				.setIssuer("Maiiiiiid")                                            //颁发者
				.setIssuedAt(new Date())                                            //颁发时间
				.setSubject("Test")                                                    //描述信息,可以是json格式
				.setId("114514")                                                    //id
				.setExpiration(new Date(System.currentTimeMillis() + 3600000))            //过期时间
				.signWith(SignatureAlgorithm.HS256, "maid114514");
		//自定义载荷信息
		Map<String,Object> userInfo = new HashMap<String,Object>();
		userInfo.put("name","Maid");
		userInfo.put("sex","xiaoluoli");
		userInfo.put("age",14);
		builder.addClaims(userInfo);
		String token = builder.compact();
		System.out.println(token);
	}

	@Test
	public void parseToken(){
		String token = "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJNYWlpaWlpaWQiLCJpYXQiOjE1OTk4ODEyOTEsInN1YiI6IlRlc3QiLCJqdGkiOiIxMTQ1MTQiLCJleHAiOjE1OTk4ODQ4OTEsInNleCI6InhpYW9sdW9saSIsIm5hbWUiOiJNYWlkIiwiYWdlIjoxNH0.hc5DfBDzDgraqTNk71vr63aQMymIWgeEuWP_PAb-IMA";
		Claims claims = Jwts.parser()
				.setSigningKey("maid114514")		//盐
				.parseClaimsJws(token)				//要解析的令牌对象
				.getBody();							//获取解析后的数据
		System.out.println(claims.toString());
	}


}
