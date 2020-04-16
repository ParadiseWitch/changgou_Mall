package com.changgou.service;

import com.changgou.goods.pojo.Brand;

import java.util.List;

/**
 * @ClassName com.changgou.service.impl.BrandService
 * @Description
 * @Author Maid
 * @Date 2020/4/16 0016 20:27
 * @Version v1.0
 */
public interface BrandService {
    /***
     * 查询所有
     * @return List<Brand>
     */
    List<Brand> findAll();
}
