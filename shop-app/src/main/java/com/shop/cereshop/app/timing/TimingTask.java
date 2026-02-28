/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.timing;


import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

import java.util.Date;
import java.util.concurrent.ScheduledFuture;

/**
 * 注解方式生成日志对象，指定topic生成对象类名
 */
@Slf4j(topic = "TimingTask")
public class TimingTask {

    /**
     * 开启场景营销自动发放优惠券定时任务
     */
    public ScheduledFuture<?> startSceneMarketing(String cron, ShopSceneRunnable shopSceneRunnable){
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.initialize();
        ScheduledFuture<?> future=threadPoolTaskScheduler.schedule(shopSceneRunnable, triggerContext ->
                new CronTrigger(cron).nextExecutionTime(triggerContext));
        System.out.println("成功开启场景营销自动发放优惠券定时任务！！！！");
        return future;
    }

    /**
     * 关闭定时任务
     */
    public static void stop(ScheduledFuture<?> future){
        if(future!=null){
            future.cancel(true);
            System.out.println("成功关闭场景营销自动发放优惠券定时任务！！！！！");
        }
    }

}
