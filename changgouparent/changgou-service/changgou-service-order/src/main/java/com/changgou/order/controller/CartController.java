package com.changgou.order.controller;


import com.changgou.order.pojo.OrderItem;
import com.changgou.order.service.CartService;
import entity.Result;
import entity.StatusCode;
import entity.TokenDecode;
import org.omg.IOP.ServiceContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

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
	 * @return
	 */
	@RequestMapping("/add")
	public Result add(Integer num,Long goodsId) {
		Map<String, String> userInfo = TokenDecode.getUserInfo();
		String username = userInfo.get("username");
		cartService.add(num,goodsId,username);
		return new Result(true, StatusCode.OK, "加入购物车成功!");
	}

	/**
	 * 购物车列表
	 * @return
	 */
	@RequestMapping("/list")
	public Result<List<OrderItem>> list(){
		Map<String, String> userInfo = TokenDecode.getUserInfo();
		String username = userInfo.get("username");
		List<OrderItem> list = cartService.list(username);
		return new Result<List<OrderItem>>(true,StatusCode.OK,"购物车查询成功",list);
	}
}
