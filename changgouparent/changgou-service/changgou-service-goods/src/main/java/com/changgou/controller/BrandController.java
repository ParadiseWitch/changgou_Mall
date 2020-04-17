package com.changgou.controller;

import com.changgou.goods.pojo.Brand;
import com.changgou.service.BrandService;
import com.github.pagehelper.PageInfo;
import entity.Page;
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
     * 分页条件查询实现
     * @param brand
     * @param page 查询页数
     * @param size 每页条目数
     */
    @PostMapping(value = "/search/{page}/{size}")
    public Result<PageInfo<Brand>> findPage(@RequestBody Brand brand,
                                            @PathVariable("page")Integer page,
                                            @PathVariable("size")Integer size){
        PageInfo<Brand> pageInfo = brandService.findPage(brand,page, size);
        return new Result<PageInfo<Brand>>(true,StatusCode.OK,"分页条件查询成功!",pageInfo);
    }

    /**
     * 分页查询实现
     * @param page 查询页数
     * @param size 每页条目数
     */
    @GetMapping(value = "/search/{page}/{size}")
    public Result<PageInfo<Brand>> findPage(@PathVariable("page")Integer page,
                                    @PathVariable("size")Integer size){
        PageInfo<Brand> pageInfo = brandService.findPage(page, size);
        return new Result<PageInfo<Brand>>(true,StatusCode.OK,"分页查询成功!",pageInfo);
    }

    /**
     * 条件搜索查询品牌
     * @param brand
     * @return
     */
    @PostMapping(value = "/search")
    public Result<List<Brand>> findList(@RequestBody Brand brand){
        List<Brand> brands = brandService.findList(brand);
        return new Result<List<Brand>>(true,StatusCode.OK,"条件搜索查询成功",brands);
    }

    /**
     * 根据ID删除品牌
     * @param id
     * @return
     */
    @DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable("id")Integer id){
        brandService.delete(id);
        return new Result(true,StatusCode.OK,"删除品牌成功!");
    }

    /**
     * 品牌修改实现
     * @param id
     * @param brand
     * @return entity.Result
     */
    @PutMapping(value = "/{id}")
    public Result update(@PathVariable("id") Integer id, @RequestBody Brand brand) {
        brand.setId(id);
        brandService.update(brand);
        return new Result(true, StatusCode.OK, "更新品牌成功!");
    }


    /**
     * 增加品牌
     * @param brand
     */
    @PostMapping
    public Result add(@RequestBody Brand brand){
        brandService.add(brand);
        return new Result(true, StatusCode.OK,"增加品牌成功！");
    }

    /**
     * @Description findById
     * @return com.changgou.goods.pojo.Brand
     * @auther Maid
     * @date 2020/4/17 0017
     */
    @GetMapping(value = "/{id}")
    public Result<Brand> findById(@PathVariable(value = "id")Integer id){
        Brand brand = brandService.findById(id);
        return new Result<Brand>(true, StatusCode.OK,"根据ID查询品牌成功！",brand);
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
