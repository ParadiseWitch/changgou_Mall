package com.changgou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @ClassName com.changgou.GatewayWebApplication
 * @Description
 * @Author Maid
 * @Date 2020/9/11 0011 14:55
 * @Version v1.0
 */
@SpringBootApplication
@EnableEurekaClient
public class GatewayWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayWebApplication.class,args);
	}
	/**
	 *
	 * 创建用户唯一标识使用IP作为用户唯一标识，来根据IP进行限流操作
	 */
	@Bean(name = "ipKeyResolver")
	public KeyResolver userKeyResovler(){
		return  new KeyResolver() {
			@Override
			public Mono<String> resolve(ServerWebExchange exchange) {
				//return Mono.just("需要使用的用户身份识别唯一标[IP]");
				String ip = exchange.getRequest().getRemoteAddress().getHostString();
				return Mono.just(ip);
			}
		};
	}
}
