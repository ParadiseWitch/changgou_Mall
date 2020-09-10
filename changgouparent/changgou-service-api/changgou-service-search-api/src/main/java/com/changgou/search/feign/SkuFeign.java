package com.changgou.search.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @ClassName com.changgou.search.feign.SkuFeign
 * @Description
 * @Author Maid
 * @Date 2020/9/10 0010 10:29
 * @Version v1.0
 */
@FeignClient(name = "search")
@RequestMapping(value = "/search")
public interface SkuFeign {
	@GetMapping
	Map search(@RequestParam(required = false) Map<String,String> searchMap);

}
