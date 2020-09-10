package com.itheima.controller;

import com.itheima.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName com.itheima.controller.TestController
 * @Description
 * @Author Maid
 * @Date 2020/9/9 0009 17:17
 * @Version v1.0
 */
@Controller
@RequestMapping(value = "/test")
public class TestController {

	@GetMapping(value = "/hello")
	public String hello(Model model) {
		model.addAttribute("message","hello thymeleaf");
		List<User> users = new ArrayList<>();
		users.add(new User(1,"Maid","武汉"));
		users.add(new User(2,"Maiid","二次元"));
		users.add(new User(3,"Maiiid",""));
		model.addAttribute("users",users);
		return "demo1";
	}

}




