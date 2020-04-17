package com.changgou.service.impl;

import com.changgou.dao.BrandMapper;
import com.changgou.goods.pojo.Brand;
import com.changgou.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName BrandServiceImpl
 * @Description
 * @Author Maid
 * @Date 2020/4/16 0016 19:04
 * @Version v1.0
 */
@Service
public class BrandServiceImpl implements BrandService {
    @Autowired
    private BrandMapper brandMapper;

    /**
     * 根据id删除品牌
     * @param id
     */
    @Override
    public void delete(Integer id) {
        brandMapper.deleteByPrimaryKey(id);
    }

    /**
     * 更改品牌数据
     * @param brand
     */
    @Override
    public void update(Brand brand) {
        brandMapper.updateByPrimaryKeySelective(brand);
    }

    /**
     * 增加品牌
     * @param brand
     */
    @Override
    public void add(Brand brand) {
        //NOTE:方法中凡是带有Selective的，执行（拼切sql）时会忽略值为空值的字段，insert方法不会忽略空值
        /**
         * insertSelective
         *      brand:name有值
         *           letter有值,其余字段为空
         * Mapper.insertSelective(brand)->SQL语句: insert into tb_brand(name, letter) values(?,?)
         * insert 不忽略空值
         * Mapper.insert(brand)->SQL语句: insert into tb_brand(id, name, image, letter, seq) values(?,?,?,?,?)
         */
        brandMapper.insertSelective(brand);
    }

    /**
     * @Description findById 通过id查询
     * @param [id]
     * @return com.changgou.goods.pojo.Brand
     */
    @Override
    public Brand findById(Integer id) {
        return brandMapper.selectByPrimaryKey(id);
    }

    /***
     * 查询所有
     * @return List<Brand>
     */
    @Override
    public List<Brand> findAll() {
        //使用通用Mapper查询所有
        return brandMapper.selectAll();
    }

}
