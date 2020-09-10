package com.changgou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

/**
 * @ClassName com.changgou.WebSearchApplication
 * @Description
 * @Author Maid
 * @Date 2020/9/10 0010 10:19
 * @Version v1.0
 */
@SpringBootApplication
//@EnableElasticsearchRepositories
@EnableEurekaClient
// BM: why map -> changogu-service-search-api  ?
@EnableFeignClients(basePackages = "com.changgou.search.feign")
public class SearchWebApplication {
	public static void main(String[] args) {
		SpringApplication.run(SearchWebApplication.class,args);
	}
}
