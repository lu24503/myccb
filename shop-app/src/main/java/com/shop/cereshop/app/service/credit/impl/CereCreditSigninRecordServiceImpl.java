/*
* Copyright (C) 2017-2021
* All rights reserved, Designed By 深圳中科鑫智科技有限公司
* Copyright authorization contact 18814114118
*/
package com.shop.cereshop.app.service.credit.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.shop.cereshop.app.dao.credit.CereCreditSignSettingDAO;
import com.shop.cereshop.app.dao.credit.CereCreditSigninRecordDAO;
import com.shop.cereshop.app.service.buyer.CereBuyerUserService;
import com.shop.cereshop.app.service.credit.CereCreditSigninRecordService;
import com.shop.cereshop.commons.constant.CreditOptTypeEnum;
import com.shop.cereshop.commons.domain.credit.CereCreditSignSetting;
import com.shop.cereshop.commons.domain.credit.CereCreditSigninRecord;
import com.shop.cereshop.commons.exception.CoBusinessException;
import com.shop.cereshop.commons.utils.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 业务实现类
 * 积分签到表
 * </p>
 *
 * @author JustArgo
 * @date 2021-12-04
 */
@Slf4j
@Service
public class CereCreditSigninRecordServiceImpl implements CereCreditSigninRecordService {

    @Autowired
    private CereCreditSigninRecordDAO cereCreditSigninRecordDAO;

    @Autowired
    private CereCreditSignSettingDAO cereCreditSignSettingDAO;

    @Autowired
    private CereBuyerUserService cereBuyerUserService;

    @Override
    @Transactional(isolation= Isolation.DEFAULT,propagation= Propagation.REQUIRED,rollbackFor = {CoBusinessException.class, Exception.class})
    public boolean signIn(Long buyerUserId) {
        if (buyerUserId == null) {
            return false;
        }
        PageHelper.startPage(1,1);
        List<CereCreditSigninRecord> list = cereCreditSigninRecordDAO.selectSigninRecordList(buyerUserId);
        if (CollectionUtils.isEmpty(list) || !TimeUtils.today().equals(list.get(0).getCreateTime().substring(0,10))) {
            int termId = 1;
            int continueDays = 1;
            // 没有积分签到配置的情况下, 签到默认给10积分
            int credit = 10;
            LambdaQueryWrapper<CereCreditSignSetting> wrapper = new LambdaQueryWrapper<>();
            wrapper.orderByDesc(CereCreditSignSetting::getDay);
            List<CereCreditSignSetting> settingList = cereCreditSignSettingDAO.selectList(wrapper);
            Map<Integer, List<CereCreditSignSetting>> settingMap = settingList.stream().collect(Collectors.groupingBy(CereCreditSignSetting::getDay));
            int maxTermId = 1;
            if (!CollectionUtils.isEmpty(settingList)) {
                maxTermId = settingList.get(0).getDay();
            }
            if (CollectionUtils.isNotEmpty(list) && TimeUtils.yesterday().equals(list.get(0).getCreateTime().substring(0,10))) {
                if (list.get(0).getTermId() < maxTermId) {
                    termId = list.get(0).getTermId() + 1;
                }
                continueDays = list.get(0).getContinueDay() + 1;
            }
            List<CereCreditSignSetting> curSettingList = settingMap.get(termId);
            if (CollectionUtils.isNotEmpty(curSettingList)) {
                credit = curSettingList.get(0).getCredit();
            }

            CereCreditSigninRecord record = new CereCreditSigninRecord();
            record.setBuyerUserId(buyerUserId);
            record.setTermId(termId);
            record.setCredit(credit);
            record.setContinueDay(continueDays);
            record.setCreateTime(TimeUtils.yyMMddHHmmss());
            record.setUpdateTime(record.getCreateTime());
            cereCreditSigninRecordDAO.insert(record);
            //增加积分
            cereBuyerUserService.increaseCredit(buyerUserId, credit, CreditOptTypeEnum.DAILY_SIGN_IN);

            return true;
        }
        return false;
    }

    @Override
    public List<CereCreditSigninRecord> selectByMonth(Long buyerUserId, String month) {
        YearMonth ym = YearMonth.parse(month);
        Date startTime = Date.from(ym.atDay(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        Date endTime = Date.from(ym.atEndOfMonth().atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant());
        String startTimeStr = DateUtil.format(startTime, "yyyy-MM-dd HH:mm:ss");
        String endTimeStr = DateUtil.format(endTime, "yyyy-MM-dd HH:mm:ss");

        LambdaQueryWrapper<CereCreditSigninRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CereCreditSigninRecord::getBuyerUserId, buyerUserId)
                .ge(CereCreditSigninRecord::getCreateTime, startTimeStr)
                .lt(CereCreditSigninRecord::getCreateTime, endTimeStr);

        return cereCreditSigninRecordDAO.selectList(wrapper);
    }
}
