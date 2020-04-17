package com.changgou.controller;

import com.changgou.goods.pojo.Brand;
import com.changgou.service.BrandService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    //自动注入的其实是实现BrandService接口的BrandServiceImpl
    @Autowired
    private BrandService brandService;

    /**
     * @Description findById
     * @return com.changgou.goods.pojo.Brand
     * @auther Maid
     * @date 2020/4/17 0017
     */
    @GetMapping(value = "/{id}")
    public Result<Brand> findById(@PathVariable(value = "id")Integer id){
        Brand brand = brandService.findById(id);
        return new Result<Brand>(true, StatusCode.OK,"根据ID查询成功！",brand);
    }
    /**
     * @return void
     * @auther Maid
     */
    @GetMapping
    public Result<List<Brand>> findAll() {
        //调用Service查询所有
        List<Brand> brands = brandService.findAll();
        return new Result<List<Brand>>(true, StatusCode.OK,"查询品牌集合成功！",brands);
    }

}
