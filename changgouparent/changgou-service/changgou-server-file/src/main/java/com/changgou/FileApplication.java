package com.changgou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @ClassName PACKAGE_NAME.FileApplication
 * @Description
 * @Author Maid
 * @Date 2020/7/9 0009 13:56
 * @Version v1.0
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class) //排除数据库自动加载
@EnableEurekaClient
public class FileApplication {
    public static void main(String[] args) {
        SpringApplication.run(FileApplication.class);
    }
}
