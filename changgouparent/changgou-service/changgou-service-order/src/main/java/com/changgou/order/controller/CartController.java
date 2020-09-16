package com.changgou.order.controller;


import com.changgou.order.service.CartService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName com.changgou.controller.CartController
 * @Description
 * @Author Maid
 * @Date 2020/9/16 0016 14:54
 * @Version v1.0
 */
@RestController
@RequestMapping("cart")
public class CartController {
	@Autowired
	private CartService cartService;

	/**
	 * 加入购物车
	 * @param num
	 * @param goodsId
	 * @param userName
	 * @return
	 */
	@RequestMapping("/add")
	public Result add(Integer num,Long goodsId, String userName) {
		cartService.add(num,goodsId,"szitheima");
		return new Result(true, StatusCode.OK, "加入购物车成功!");
	}
}
