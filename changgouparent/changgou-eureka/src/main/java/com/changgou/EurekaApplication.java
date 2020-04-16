package com.changgou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

import javax.swing.*;

/**
 * @ClassName: EurekaApplication
 * @Description: eureka app
 * @Author:
 * @Date: 2020/4/15 0015 19:14
 * @Version: v1.0
 */
@SpringBootApplication
@EnableEurekaServer
public class EurekaApplication {

    public static void main(String[] args) {
    /**
     * @Description main
     * @param [args]
     * @return void
     * @auther Administrator
     * @date 2020/4/15
     */
        SpringApplication.run(EurekaApplication.class,args);
    }






}
