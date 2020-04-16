package com.changgou.controller;

import com.changgou.goods.pojo.Brand;
import com.changgou.service.BrandService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName com.changgou.controller.BrandController
 * @Description
 * @Author Maid
 * @Date 2020/4/16 0016 19:06
 * @Version v1.0
 */
@RestController
@RequestMapping(value = "/brand")
@CrossOrigin
public class BrandController {
    @Autowired
    private BrandService brandService;

    /**
     * @return void
     * @auther Maid
     */
    @GetMapping
    public Result<List<Brand>> findAll(){
        //调用Service查询所有
        List<Brand> brands = brandService.findAll();
        return new Result<List<Brand>>(true, StatusCode.OK,"查询品牌集合成功！",brands);
    }

}
