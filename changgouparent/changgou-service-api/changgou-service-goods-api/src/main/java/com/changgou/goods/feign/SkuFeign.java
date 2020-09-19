package com.changgou.goods.feign;

import com.changgou.goods.pojo.Sku;
import com.github.pagehelper.PageInfo;
import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @ClassName com.changgou.goods.feign.SkuFeign
 * @Description
 * @Author Maid
 * @Date 2020/9/5 0005 15:16
 * @Version v1.0
 */
@FeignClient(value = "goods")
@RequestMapping("/sku")
public interface SkuFeign {
	@GetMapping
	Result<PageInfo> findAll();

	@GetMapping("/{id}")
	public Result<Sku> findById(@PathVariable String id);

	@GetMapping("/decr/count")
	Result decrCount(@RequestParam Map<String,Integer> decrmap);
}
