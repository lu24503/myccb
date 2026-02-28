/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.redis.impl;

import com.shop.cereshop.app.dao.redis.CereRedisKeyDAO;
import com.shop.cereshop.app.redis.service.api.StringRedisService;
import com.shop.cereshop.app.service.redis.CereRedisKeyServcice;
import com.shop.cereshop.commons.domain.redis.CereRedisKey;
import com.shop.cereshop.commons.exception.CoBusinessException;
import com.shop.cereshop.commons.utils.EmptyUtils;
import com.shop.cereshop.commons.utils.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CereRedisKeyServciceImpl implements CereRedisKeyServcice {

    @Autowired
    private CereRedisKeyDAO cereRedisKeyDAO;

    @Autowired
    private StringRedisService stringRedisService;

    @Override
    public void add(String key, String endTime) throws CoBusinessException {
        CereRedisKey cereRedisKey=new CereRedisKey();
        cereRedisKey.setRedisKey(key);
        cereRedisKey.setEndTime(endTime);
        cereRedisKeyDAO.insert(cereRedisKey);
    }

    @Override
    public void updateByKey(String key, String endTime) throws CoBusinessException {
        CereRedisKey cereRedisKey=new CereRedisKey();
        //查询是否存在数据
        String time = cereRedisKeyDAO.findByKey(key);
        if(!EmptyUtils.isEmpty(time)){
            //更新
            cereRedisKey.setRedisKey(key);
            cereRedisKey.setEndTime(endTime);
            cereRedisKeyDAO.updateByKey(cereRedisKey);
        }else {
            //新增
            cereRedisKey.setRedisKey(key);
            cereRedisKey.setEndTime(endTime);
            cereRedisKeyDAO.insert(cereRedisKey);
        }
    }

    @Override
    public void deleteByKey(String key) throws CoBusinessException {
        cereRedisKeyDAO.deleteByKey(key);
    }

    @Override
    @Transactional(isolation= Isolation.DEFAULT,propagation= Propagation.REQUIRED,rollbackFor = {CoBusinessException.class, Exception.class})
    public void initializationRedis() throws CoBusinessException,Exception {
        //清空所有redis的key
        stringRedisService.deleteAll();
        //查询所有任务
        List<CereRedisKey> list=cereRedisKeyDAO.findAll();
        if(!EmptyUtils.isEmpty(list)){
            for (CereRedisKey cereRedisKey:list) {
                if(TimeUtils.compareTo(TimeUtils.yyMMddHHmmss(),cereRedisKey.getEndTime())){
                    //如果当前时间在截至时间之后,需要重新设置延时任务,立即执行
                    stringRedisService.set(cereRedisKey.getRedisKey(),1000);
                }
                //设置延时任务
                stringRedisService.set(cereRedisKey.getRedisKey(),
                        TimeUtils.getCountDownByTime(TimeUtils.yyMMddHHmmss(),cereRedisKey.getEndTime()));
            }
        }
    }

    @Override
    public String findByKey(String key) {
        return cereRedisKeyDAO.findByKey(key);
    }
}
