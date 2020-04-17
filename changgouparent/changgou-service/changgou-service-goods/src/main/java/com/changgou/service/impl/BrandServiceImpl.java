package com.changgou.service.impl;

import com.changgou.dao.BrandMapper;
import com.changgou.goods.pojo.Brand;
import com.changgou.service.BrandService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

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
     * 分页条件查询
     * @param brand
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageInfo<Brand> findPage(Brand brand, Integer page, Integer size) {
        //分页
        PageHelper.startPage(page,size);
        //条件
        Example example = createExample(brand);
        List<Brand> brands = brandMapper.selectByExample(example);
        return new PageInfo<Brand>(brands);
    }

    /**
     * 分页查询
     * @param page
     * @param size
     */
    @Override
    public PageInfo<Brand> findPage(Integer page, Integer size) {
        //使用PageHelper
        PageHelper.startPage(page,size);
        //后面要紧跟查询集合
        List<Brand> brands = brandMapper.selectAll();
        return new PageInfo<Brand>(brands);
    }

    /**
     * 根据条件查询品牌
     * @param brand
     */
    @Override
    public List<Brand> findList(Brand brand) {
        Example example = createExample(brand);
        return brandMapper.selectByExample(example);
    }

    /**
     * 条件构建
     * @param brand
     */
    public Example createExample(Brand brand){
        //自定义条件搜索对象
        Example example = new Example(Brand.class);
        //创建一个条件构造器
        Example.Criteria criteria = example.createCriteria();
        if(brand!=null){
            //if(brand.name!=null) 根据名字模糊搜索
            if(!StringUtils.isEmpty(brand.getName())) {
                criteria.andLike("name","%"+brand.getName()+"%");
            }
            //if(brand.letter!=null) 根据首字母模糊搜索
            if (!StringUtils.isEmpty(brand.getLetter())) {
                criteria.andEqualTo("letter",brand.getLetter());
            }
        }
        return example;
    }

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
