package com.changgou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
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
@SpringBootApplication
public class OrderApplication {
	public static void main(String[] args) {
		SpringApplication.run(OrderApplication.class,args);
	}
}
