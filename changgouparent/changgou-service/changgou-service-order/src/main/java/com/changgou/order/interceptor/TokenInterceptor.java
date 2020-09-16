package com.changgou.order.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Enumeration;

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
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		Enumeration<String> headerNames = requestAttributes.getRequest().getHeaderNames();

		while (headerNames.hasMoreElements()){
			String headerName = headerNames.nextElement();
			String headerValue = requestAttributes.getRequest().getHeader(headerName);

			requestTemplate.header(headerName,headerValue);
		}

	}
}
