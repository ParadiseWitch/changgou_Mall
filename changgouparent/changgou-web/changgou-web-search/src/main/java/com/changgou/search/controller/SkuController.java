package com.changgou.search.controller;

import com.changgou.search.feign.SkuFeign;
import com.changgou.search.pojo.SkuInfo;
import com.netflix.discovery.converters.Auto;
import entity.Page;
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

		Page<SkuInfo> pageInfo = new Page<SkuInfo>(
				Long.parseLong(resultMap.get("totalElements").toString()),
				Integer.parseInt(resultMap.get("pageNumber").toString()) + 1,
				Integer.parseInt(resultMap.get("pageSize").toString()),
				1
		);

		model.addAttribute("pageInfo",pageInfo);

		//获取请求地址
		String[] urls = url(searchMap);
		model.addAttribute("url",urls[0]);
		model.addAttribute("sortUrl",urls[1]);
		return "search";
	}

	public String[] url(Map<String,String> searchMap){
		String url = "/search/list";
		String sortUrl = "/search/list";
		if(searchMap!=null && searchMap.size()>0){
			url += "?";
			sortUrl += "?";
			for (Map.Entry<String, String> entry : searchMap.entrySet()) {
				//跳过分页参数
				if(entry.getKey().equalsIgnoreCase("pageNum")){
					continue;
				}
				url += entry.getKey() + "=" + entry.getValue() + "&";
				//跳过排序参数
				if (entry.getKey().equalsIgnoreCase("sortField") || entry.getKey().equalsIgnoreCase("sortRule")) {
					continue;
				}
				sortUrl += entry.getKey() + "=" + entry.getValue() + "&";
			}
			url = url.substring(0,url.length()-1);
		}
		return new String[]{url,sortUrl};
	}
}
