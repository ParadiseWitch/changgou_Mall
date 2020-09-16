package com.changgou.goods.feign;

		import com.changgou.goods.pojo.Goods;
		import com.changgou.goods.pojo.Spu;
		import entity.Result;
		import org.springframework.cloud.openfeign.FeignClient;
		import org.springframework.web.bind.annotation.GetMapping;
		import org.springframework.web.bind.annotation.PathVariable;
		import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @ClassName com.changgou.goods.feign.SpuFeign
 * @Description
 * @Author Maid
 * @Date 2020/9/16 0016 15:00
 * @Version v1.0
 */
@FeignClient("goods")
@RequestMapping("/spu")
public interface SpuFeign {
	@GetMapping("/{id}")
	public Result<Spu> findById(@PathVariable String id);
}
