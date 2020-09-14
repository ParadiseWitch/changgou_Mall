package com.changgou.filter;

import com.changgou.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import springfox.documentation.service.OAuth;

/**
 * @ClassName com.changgou.filter.AuthorizeFilter
 * @Description
 * @Author Maid
 * @Date 2020/9/12 0012 12:15
 * @Version v1.0
 */
@Component
public class AuthorizeFilter implements GlobalFilter, Ordered {
	//令牌名
	private static final String AUTHORIZE_TOKEN = "Authorization";

	/**
	 * 全局拦截
	 * @param exchange
	 * @param chain
	 * @return
	 */
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		ServerHttpRequest request = exchange.getRequest();
		ServerHttpResponse response = exchange.getResponse();

		//获取请求的URI
		String path = request.getURI().getPath();

		//如果是登录、goods等开放的微服务[这里的goods部分开放],则直接放行,这里不做完整演示，完整演示需要设计一套权限系统
		if (path.startsWith("/api/user/login") || path.startsWith("/api/brand/search/")) {
			//放行
			Mono<Void> filter = chain.filter(exchange);
			return filter;
		}

		//获取用户令牌信息
		// - 头文件
		String token = request.getHeaders().getFirst(AUTHORIZE_TOKEN);
		//true: 令牌在头文件中  false: 不在头文件
		boolean hasToken = true;
		// - 参数获取令牌
		if (StringUtils.isEmpty(token)){
			token = request.getQueryParams().getFirst(AUTHORIZE_TOKEN);
			hasToken = false;
		}
		// - Cookie中
		if(StringUtils.isEmpty(token)){
			ResponseCookie cookie = response.getCookies().getFirst(AUTHORIZE_TOKEN);
			if (cookie != null) {
				token = cookie.getValue();
			}
		}
		//如果没有令牌 =>拦截
		if(StringUtils.isEmpty(token)){
			//	状态码: 401
			response.setStatusCode(HttpStatus.UNAUTHORIZED);
			// 响应空数据
			return response.setComplete();
		}else{
			if (!hasToken){
				//有令牌且令牌不在头信息
				if (!token.startsWith("bearer ") && !token.startsWith("Bearer")){
					//头信息中的令牌不以 bearer/Bearer 打头

					token = "breaer " + token;
					//将令牌封装到头
					request.mutate().header(AUTHORIZE_TOKEN,token);
				}
			}
		}

		//有令牌 => 检测是否有效
		//解析令牌数据
		//try {
		//	Claims claims = JwtUtil.parseJWT(token);
		//} catch (Exception e) {
		//	e.printStackTrace();
		//	//无效拦截
		//	//解析失败，响应401错误
		//	response.setStatusCode(HttpStatus.UNAUTHORIZED);
		//	return response.setComplete();
		//}


		//有效放行
		return chain.filter(exchange);
	}

	@Override
	public int getOrder() {
		return 0;
	}
}
