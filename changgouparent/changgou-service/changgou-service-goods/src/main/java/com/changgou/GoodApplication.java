package com.changgou;

import entity.IdWorker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import tk.mybatis.spring.annotation.MapperScan;

import javax.swing.*;

/**
 * @ClassName com.changgou.GoodApplication
 * @Description
 * @Author Maid
 * @Date 2020/4/15 0015 22:35
 * @Version v1.0
 */
@SpringBootApplication
@EnableEurekaClient  //开启eureka客户端
@MapperScan(basePackages = "com.changgou.goods.dao")
/*
 * 开启通用Mapper的包扫描
 * NOTE:有关通用Mapper的包都是`tk.mybatis`下的
 */
public class GoodApplication {

    /**
     * IdWorker
     * @return
     */
    @Bean
    public IdWorker idWorker(){
        return new IdWorker(0,0);
    }

    public static void main(String[] args) {
    /**
     * @Description main
     * @param [args]
     * @return void
     * @auther Maid
     * @date 2020/4/15 0015
     */
        SpringApplication.run(GoodApplication.class,args);
    }
}
