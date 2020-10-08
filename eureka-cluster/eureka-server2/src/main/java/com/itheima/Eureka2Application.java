package com.itheima;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @ClassName com.itheima.Eureka1Application
 * @Description
 * @Author Maid
 * @Date 2020/10/8 0008 10:44
 * @Version v1.0
 */
@SpringBootApplication
@EnableEurekaServer
public class Eureka2Application {

	public static void main(String[] args) {
		/**
		 * @Description main
		 * @param [args]
		 * @return void
		 * @auther Administrator
		 * @date 2020/4/15
		 */
		SpringApplication.run(Eureka2Application.class,args);
	}


}
