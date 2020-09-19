package com.changgou.user.feign;

import com.changgou.user.pojo.User;
import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @ClassName com.changgou.user.feign.UserFeign
 * @Description
 * @Author Maid
 * @Date 2020/9/16 0016 9:37
 * @Version v1.0
 */
@FeignClient(name="user")
@RequestMapping("/user")
public interface UserFeign {
	@GetMapping("/load/{id}")
	public Result<User> findById(@PathVariable(name="id") String id);

	/**
	 * 添加积分
	 * @param points
	 * @return
	 */
	@GetMapping("/points/add")
	Result addPoints(@RequestParam Integer points);
}
