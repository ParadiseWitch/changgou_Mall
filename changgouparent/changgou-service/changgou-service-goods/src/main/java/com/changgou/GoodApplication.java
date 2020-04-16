package com.changgou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
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
@MapperScan(basePackages = "com.changgou.dao")
/*
 * 开启通用Mapper的包扫描
 * NOTE:有关通用Mapper的包都是`tk.mybatis`下的
 */
public class GoodApplication {
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
