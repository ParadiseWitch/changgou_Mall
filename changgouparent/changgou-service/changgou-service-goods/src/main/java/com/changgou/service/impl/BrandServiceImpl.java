package com.changgou.service.impl;

import com.changgou.dao.BrandMapper;
import com.changgou.goods.pojo.Brand;
import com.changgou.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName
 * @Description
 * @Author Maid
 * @Date 2020/4/16 0016 19:04
 * @Version v1.0
 */
@Service
public class BrandServiceImpl implements BrandService {
    @Autowired
    private BrandMapper brandMapper;

    /***
     * 查询所有
     * @return
     */
    @Override
    public List<Brand> findAll() {
        //使用通用Mapper查询所有
        return brandMapper.selectAll();
    }

}
