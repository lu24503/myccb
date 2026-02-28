/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.dict.impl;

import com.shop.cereshop.app.dao.dict.CerePlatformDictDAO;
import com.shop.cereshop.app.service.dict.CerePlatformDictService;
import com.shop.cereshop.commons.constant.IntegerEnum;
import com.shop.cereshop.commons.constant.LongEnum;
import com.shop.cereshop.commons.domain.dict.CerePlatformDict;
import com.shop.cereshop.commons.exception.CoBusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CerePlatformDictServiceImpl implements CerePlatformDictService {

    @Autowired
    private CerePlatformDictDAO cerePlatformDictDAO;

    @Override
    public List<CerePlatformDict> getExpressSelect() throws CoBusinessException {
        List<CerePlatformDict> list=new ArrayList<>();
        //查询当前物流查询策略
        Integer express = cerePlatformDictDAO.findExpress();
        if(IntegerEnum.EXPRESS_100.getCode().equals(express)){
            //查询快递100所属快递公司
            list=cerePlatformDictDAO.findExpressSelect(LongEnum.EXPRESS_100.getCode());
        }else if(IntegerEnum.EXPRESS_NIAO.getCode().equals(express)){
            list=cerePlatformDictDAO.findExpressSelect(LongEnum.EXPRESS_NIAO.getCode());
        }
        return list;
    }

    @Override
    public List<String> getReasonSelect() throws CoBusinessException {
        return cerePlatformDictDAO.getReasonSelect();
    }

    @Override
    public String findByCompany(String express, Long dictPid) {
        return cerePlatformDictDAO.findByCompany(express,dictPid);
    }

    @Override
    public CerePlatformDict getByName(String name) {
        return cerePlatformDictDAO.getByName(name);
    }

    @Override
    public Map<String, CerePlatformDict> getDictMap(List<String> nameList) {
        List<CerePlatformDict> list = cerePlatformDictDAO.getByNameList(nameList);
        return list.stream().collect(Collectors.groupingBy(CerePlatformDict::getDictName, Collectors.collectingAndThen(Collectors.toList(), lst -> lst.get(0))));
    }

    @Override
    public List<CerePlatformDict> getSelect(String dictName) {
        return cerePlatformDictDAO.getSelect(dictName);
    }

    @Override
    public Map<Long, String> selectDictNameMap(List<Long> dictIdList) {
        List<CerePlatformDict> list = cerePlatformDictDAO.selectBatchIds(dictIdList);
        return list.stream().collect(Collectors.toMap(CerePlatformDict::getDictId, CerePlatformDict::getDictName));
    }
}
