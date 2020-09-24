package com.changgou.seckill.timer;

import com.changgou.seckill.dao.SeckillGoodsMapper;
import com.changgou.seckill.pojo.SeckillGoods;
import entity.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

/**
 * @ClassName com.changgou.seckill.timer.SeckillGoodsPushTask
 * @Description
 * @Author Maid
 * @Date 2020/9/24 0024 22:51
 * @Version v1.0
 */
@Component
public class SeckillGoodsPushTask {
	@Autowired
	private SeckillGoodsMapper seckillGoodsMapper;

	@Autowired
	private RedisTemplate redisTemplate;

	@Scheduled(cron = "0/5 * * * * ?")
	public void loadGoodsPushRedis(){
		List<Date> dateMenus = DateUtil.getDateMenus();
		for (Date dateMenu : dateMenus) {
			String timespace = DateUtil.data2str(dateMenu, "yyyyMMddHH");
			/**
			 * 1.审核=1
			 * 2.库存>0
			 * 3.商品时间段被包含在时间菜单段
			 */
			Example example = new Example(SeckillGoods.class);
			Example.Criteria criteria = example.createCriteria();

			criteria.andEqualTo("status","1");
			criteria.andGreaterThan("stockCount",0);
			criteria.andGreaterThan("startTime",dateMenu);
			criteria.andLessThan("endTime",DateUtil.addDateHour(dateMenu,2));

			//查询数据
			List<SeckillGoods> seckillGoods = seckillGoodsMapper.selectByExample(example);

			//存入redis
			for (SeckillGoods seckillGood : seckillGoods) {
				redisTemplate.boundHashOps(timespace).put(seckillGood.getId(),seckillGood);
			}

		}
	}
}
