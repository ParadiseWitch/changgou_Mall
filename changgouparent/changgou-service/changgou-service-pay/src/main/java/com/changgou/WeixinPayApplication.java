package com.changgou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @ClassName com.changgou.WeixinPayApplication
 * @Description
 * @Author Maid
 * @Date 2020/9/20 0020 16:31
 * @Version v1.0
 */
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
@EnableEurekaClient
public class WeixinPayApplication {

	public static void main(String[] args) {
		SpringApplication.run(WeixinPayApplication.class,args);
	}
}