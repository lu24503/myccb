/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app;

import com.shop.cereshop.app.timing.ShopSceneRunnable;
import com.shop.cereshop.app.timing.TimingTask;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.concurrent.ScheduledFuture;

@SpringBootApplication
@EnableTransactionManagement
@EnableConfigurationProperties
@EnableAutoConfiguration(exclude = {
		org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class
})
@EnableAsync
@ComponentScan(value = "com.shop.cereshop.commons.utils")
@ComponentScan(value = "com.shop.cereshop.app")
@ComponentScan(value = "com.shop.cereshop.commons.config")
public class CereshopAppApplication extends SpringBootServletInitializer {

	/**
	 * 需要把web项目打成war包部署到外部tomcat运行时需要改变启动方式
	 */
	@Override  //重写configure方法，把springboot的入口给它
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(CereshopAppApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(CereshopAppApplication.class, args);

		TimingTask timingTask=new TimingTask();
		ScheduledFuture<?> labelFuture = timingTask.startSceneMarketing("0 0/1 * * * ?",
				new ShopSceneRunnable());

		System.out.println("------   app-server启动成功    ------\n");
	}

}
