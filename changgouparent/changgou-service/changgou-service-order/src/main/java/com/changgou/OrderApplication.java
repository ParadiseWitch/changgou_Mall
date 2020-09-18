package com.changgou;

import entity.FeignInterceptor;
import entity.IdWorker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @ClassName com.changgou.OrderApplication
 * @Description
 * @Author Maid
 * @Date 2020/9/16 0016 11:05
 * @Version v1.0
 */
@EnableEurekaClient
@MapperScan(basePackages = "com.changgou.order.dao")
@EnableFeignClients("com.changgou.goods.feign")
@SpringBootApplication
public class OrderApplication {
	public static void main(String[] args) {
		SpringApplication.run(OrderApplication.class,args);
	}

	/**
	 * 将Feign拦截器注入容器
	 * @return
	 */
	@Bean
	public FeignInterceptor feignInterceptor(){
		return new FeignInterceptor();
	}

	@Bean
	public IdWorker idWorker(){
		return new IdWorker(0,0);
	}
}
