/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.member.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shop.cereshop.app.dao.member.CereMemberSigninRecordDAO;
import com.shop.cereshop.app.service.member.CereMemberSigninRecordService;
import com.shop.cereshop.commons.domain.common.Page;
import com.shop.cereshop.commons.domain.common.PageParam;
import com.shop.cereshop.commons.domain.member.CereMemberSigninRecord;
import com.shop.cereshop.commons.utils.TimeUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class CereMemberSigninRecordServiceImpl implements CereMemberSigninRecordService {

    @Autowired
    private CereMemberSigninRecordDAO cereMemberSigninRecordDAO;

    private final List<Integer> growthList = Arrays.asList(0,10,20,30,40,50,60,100);

    @Override
    public List<CereMemberSigninRecord> selectSigninRecordList(Long buyerUserId) {
        if (buyerUserId == null) {
            return Collections.emptyList();
        }
        PageHelper.startPage(1,7);
        List<CereMemberSigninRecord> list = cereMemberSigninRecordDAO.selectSigninRecordList(buyerUserId);
        if (CollectionUtils.isEmpty(list)) {
            return list;
        }
        Collections.reverse(list);
        int last = list.size() - 1;
        // 最新的一条记录是今天的签到记录
        if (list.get(last).getCreateTime().substring(0,10).equals(TimeUtils.today())) {
            return list.subList(list.size() - list.get(last).getTermId(), list.size());
        }
        // 最新的一条记录是昨天的签到记录
        if (list.get(last).getCreateTime().substring(0,10).equals(TimeUtils.yesterday())) {
            //说明昨天已经是第7天，今天开始需要重新循环了
            if (list.get(last).getTermId() == 7) {
                return Collections.emptyList();
            } else {
                return list.subList(list.size() - list.get(last).getTermId(), list.size());
            }
        }
        // 昨天没有签到记录，所以今天算第一天
        return Collections.emptyList();
    }

    @Override
    public Page selectSigninHistory(PageParam pageParam, Long buyerUserId) {
        PageHelper.startPage(pageParam.getPage(), pageParam.getPageSize());
        List<CereMemberSigninRecord> list = cereMemberSigninRecordDAO.selectSigninRecordList(buyerUserId);
        PageInfo<CereMemberSigninRecord> pageInfo = new PageInfo<>(list);
        Page page=new Page(pageInfo.getList(),pageInfo.getTotal());
        return page;
    }

    @Override
    public boolean signIn(Long buyerUserId) {
        if (buyerUserId == null) {
            return false;
        }
        PageHelper.startPage(1,1);
        List<CereMemberSigninRecord> list = cereMemberSigninRecordDAO.selectSigninRecordList(buyerUserId);
        if (CollectionUtils.isEmpty(list) || !TimeUtils.today().equals(list.get(0).getCreateTime().substring(0,10))) {
            int termId = 1;
            if (CollectionUtils.isNotEmpty(list) && TimeUtils.yesterday().equals(list.get(0).getCreateTime().substring(0,10))
                    && list.get(0).getTermId() < 7) {
                termId = list.get(0).getTermId() + 1;
            }
            CereMemberSigninRecord record = new CereMemberSigninRecord();
            record.setBuyerUserId(buyerUserId);
            record.setTermId(termId);
            record.setGrowth(growthList.get(termId));
            record.setCreateTime(TimeUtils.yyMMddHHmmss());
            record.setUpdateTime(record.getCreateTime());
            cereMemberSigninRecordDAO.insert(record);
            return true;
        }
        return false;
    }

}
