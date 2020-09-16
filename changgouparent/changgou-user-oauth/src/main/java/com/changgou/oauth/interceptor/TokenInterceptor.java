package com.changgou.oauth.interceptor;

import com.changgou.oauth.util.AdminToken;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName com.changgou.oauth.interceptor.TokenInterceptor
 * @Description
 * @Author Maid
 * @Date 2020/9/16 0016 20:23
 * @Version v1.0
 */
@Configuration
public class TokenInterceptor implements RequestInterceptor {

	@Override
	public void apply(RequestTemplate requestTemplate) {
		String token = AdminToken.adminToken();

		requestTemplate.header("Authorization","bearer " + token);

	}
}
