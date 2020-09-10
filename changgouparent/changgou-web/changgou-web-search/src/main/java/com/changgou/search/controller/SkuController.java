package com.changgou.search.controller;

import com.changgou.search.feign.SkuFeign;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @ClassName com.changgou.search.controller.SkuController
 * @Description
 * @Author Maid
 * @Date 2020/9/10 0010 10:37
 * @Version v1.0
 */
@Controller
@RequestMapping(value = "/search")
public class SkuController {
	@Autowired
	private SkuFeign skuFeign;

	@GetMapping(value = "/list")
	public String search(@RequestParam(required = false) Map<String,String> searchMap, Model model) {
		Map<String, Object> resultMap = skuFeign.search(searchMap);
		model.addAttribute("result",resultMap);

		//  将条件存储,用于页面回显
		model.addAttribute("searchMap", searchMap);
		return "search";
	}
}
