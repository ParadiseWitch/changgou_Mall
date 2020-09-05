package com.changgou;

import com.xpand.starter.canal.annotation.EnableCanalClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @ClassName com.changgou.CanalApplication
 * @Description
 * @Author Maid
 * @Date 2020/9/2 0002 17:35
 * @Version v1.0
 */
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
@EnableEurekaClient
@EnableCanalClient
public class CanalApplication {

	public static void main(String[] args) {
		SpringApplication.run(CanalApplication.class,args);
	}
}
