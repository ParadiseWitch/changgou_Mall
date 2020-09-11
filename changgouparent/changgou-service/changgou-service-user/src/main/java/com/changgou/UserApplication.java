package com.changgou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @ClassName com.changgou.UserApplication
 * @Description
 * @Author Maid
 * @Date 2020/9/11 0011 21:29
 * @Version v1.0
 */
@SpringBootApplication
@EnableEurekaClient
@MapperScan("com.changgou.user.dao")
public class UserApplication {
	public static void main(String[] args) {
		SpringApplication.run(UserApplication.class,args);
	}
}
