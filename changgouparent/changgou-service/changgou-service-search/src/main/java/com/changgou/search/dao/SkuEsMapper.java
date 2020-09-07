package com.changgou.search.dao;

import com.changgou.search.pojo.SkuInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;

/**
 * @ClassName com.changgou.search.dao.SkuEsMapper
 * @Description
 * @Author Maid
 * @Date 2020/9/5 0005 15:30
 * @Version v1.0
 */
public interface SkuEsMapper extends ElasticsearchCrudRepository<SkuInfo,Long> {

}
