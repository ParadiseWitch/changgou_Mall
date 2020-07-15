package com.changgou.goods.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName com.changgou.goods.pojo.Goods
 * @Description
 * @Author Maid
 * @Date 2020/7/15 0015 23:11
 * @Version v1.0
 */
public class Goods implements Serializable {
    //SPU
    private Spu spu;
    //SKU集合
    private List<Sku> skuList;

    public Spu getSpu() {
        return spu;
    }

    public void setSpu(Spu spu) {
        this.spu = spu;
    }

    public List<Sku> getSkuList() {
        return skuList;
    }

    public void setSkuList(List<Sku> skuList) {
        this.skuList = skuList;
    }
}