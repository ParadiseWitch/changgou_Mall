package com.changgou.seckill.task;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @ClassName com.changgou.seckill.task.MultiThreadingCreateOrder
 * @Description
 * @Author Maid
 * @Date 2020/9/25 0025 12:44
 * @Version v1.0
 */
@Component
public class MultiThreadingCreateOrder {

	/***
	 * 多线程下单操作
	 */
	@Async
	public void createOrder(){
		try {
			System.out.println("准备执行....");
			Thread.sleep(20000);
			System.out.println("开始执行....");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}