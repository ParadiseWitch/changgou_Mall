package com.changgou.seckill.timer;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @ClassName com.changgou.seckill.timer.SeckillGoodsPushTask
 * @Description
 * @Author Maid
 * @Date 2020/9/24 0024 22:51
 * @Version v1.0
 */
@Component
public class SeckillGoodsPushTask {
	@Scheduled(cron = "0/5 * * * * ?")
	public void loadGoodsPushRedis(){
		System.out.println("task demo");
	}
}
