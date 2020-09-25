package com.changgou;

import entity.IdWorker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @ClassName com.changgou.SeckillApplication
 * @Description
 * @Author Maid
 * @Date 2020/9/24 0024 22:38
 * @Version v1.0
 */
@EnableAsync
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@MapperScan(basePackages = {"com.changgou.seckill.dao"})
@EnableScheduling
public class SeckillApplication {


	public static void main(String[] args) {
		SpringApplication.run(SeckillApplication.class,args);
	}

	@Bean
	public IdWorker idWorker(){
		return new IdWorker(1,1);
	}
}