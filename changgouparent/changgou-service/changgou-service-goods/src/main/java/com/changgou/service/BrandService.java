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

    /**
     * 根据品牌信息多条件查询
     * @return
     */
    List<Brand> findList(Brand brand);

    /**
     * 根据id删除品牌
     * @param id
     */
    void delete(Integer id);

    /**
     * 根据ID修改品牌数据
     * @param brand
     */
    void update(Brand brand);

    /**
     * 增加品牌
     * @param brand
     */
    void add(Brand brand);

    /**
     * 根据ID查询
     * @param id
     * @return Brand
     */
    Brand findById(Integer id);

    /***
     * 查询所有
     * @return List<Brand>
     */
    List<Brand> findAll();
}
