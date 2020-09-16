package entity;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;
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
public class FeignInterceptor implements RequestInterceptor {

	@Override
	public void apply(RequestTemplate requestTemplate) {
		//记录了当前用户请求的所有数据,包含请求头和请求参数等
		//当前用户请求的时候对应线程的数据
		//如果开启了熔断, 这部分在Feign调用在新的线程, 无法获取之前线程的数据, requestAttributes=null
		//解决办法  将熔断策略换成 信号量隔离  此时不会开启新线程
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		Enumeration<String> headerNames = requestAttributes.getRequest().getHeaderNames();

		while (headerNames.hasMoreElements()){
			String headerName = headerNames.nextElement();
			String headerValue = requestAttributes.getRequest().getHeader(headerName);

			requestTemplate.header(headerName,headerValue);
		}

	}
}
