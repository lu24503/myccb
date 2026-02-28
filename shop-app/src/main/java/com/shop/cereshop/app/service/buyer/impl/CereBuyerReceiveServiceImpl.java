/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.buyer.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shop.cereshop.app.dao.buyer.CereBuyerReceiveDAO;
import com.shop.cereshop.app.service.buyer.CereBuyerReceiveService;
import com.shop.cereshop.commons.constant.IntegerEnum;
import com.shop.cereshop.commons.domain.buyer.CereBuyerReceive;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.domain.common.Page;
import com.shop.cereshop.commons.domain.common.PageParam;
import com.shop.cereshop.commons.exception.CoBusinessException;
import com.shop.cereshop.commons.utils.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CereBuyerReceiveServiceImpl implements CereBuyerReceiveService {

    @Autowired
    private CereBuyerReceiveDAO cereBuyerReceiveDAO;

    @Override
    public CereBuyerReceive findlatelyReceiveByUserId(Long buyerUserId) {
        return cereBuyerReceiveDAO.findlatelyReceiveByUserId(buyerUserId);
    }

    @Override
    @Transactional(isolation= Isolation.DEFAULT,propagation= Propagation.REQUIRED,rollbackFor = {CoBusinessException.class, Exception.class})
    public CereBuyerReceive save(CereBuyerReceive receive, CereBuyerUser user) throws CoBusinessException {
        String time = TimeUtils.yyMMddHHmmss();
        receive.setCreateTime(time);
        receive.setUpdateTime(time);
        receive.setBuyerUserId(user.getBuyerUserId());
        if(IntegerEnum.YES.getCode().equals(receive.getIfDefault())){
            //如果设置为默认,查询是否有其他默认地址
            CereBuyerReceive cereBuyerReceive=cereBuyerReceiveDAO.findDefaultByUserIdAndSelf(user.getBuyerUserId(),null);
            if(cereBuyerReceive!=null){
                //更新为非默认
                cereBuyerReceive.setIfDefault(IntegerEnum.NO.getCode());
                cereBuyerReceiveDAO.updateByPrimaryKeySelective(cereBuyerReceive);
            }
        }
        cereBuyerReceiveDAO.insert(receive);
        return receive;
    }

    @Override
    @Transactional(isolation= Isolation.DEFAULT,propagation= Propagation.REQUIRED,rollbackFor = {CoBusinessException.class, Exception.class})
    public void update(CereBuyerReceive receive, CereBuyerUser user) throws CoBusinessException {
        String time = TimeUtils.yyMMddHHmmss();
        receive.setUpdateTime(time);
        if(IntegerEnum.YES.getCode().equals(receive.getIfDefault())){
            //如果设置为默认,查询是否有其他默认地址
            CereBuyerReceive cereBuyerReceive=cereBuyerReceiveDAO.findDefaultByUserIdAndSelf(user.getBuyerUserId(),receive.getReceiveId());
            if(cereBuyerReceive!=null){
                //更新为非默认
                cereBuyerReceive.setIfDefault(IntegerEnum.NO.getCode());
                cereBuyerReceiveDAO.updateByPrimaryKeySelective(cereBuyerReceive);
            }
        }
        cereBuyerReceiveDAO.updateByPrimaryKeySelective(receive);
    }

    @Override
    @Transactional(isolation= Isolation.DEFAULT,propagation= Propagation.REQUIRED,rollbackFor = {CoBusinessException.class, Exception.class})
    public void delete(Long receiveId, CereBuyerUser user) throws CoBusinessException {
        cereBuyerReceiveDAO.deleteByPrimaryKey(receiveId);
    }

    @Override
    public CereBuyerReceive getById(Long receiveId) throws CoBusinessException {
        return cereBuyerReceiveDAO.getById(receiveId);
    }

    @Override
    public Page getAll(PageParam param, CereBuyerUser user) throws CoBusinessException {
        PageHelper.startPage(param.getPage(),param.getPageSize());
        List<CereBuyerReceive> list = cereBuyerReceiveDAO.getAll(user.getBuyerUserId());
        PageInfo<CereBuyerReceive> pageInfo=new PageInfo<>(list);
        Page page=new Page(pageInfo.getList(),pageInfo.getTotal());
        return page;
    }

    @Override
    public List<CereBuyerReceive> getSelect(Long buyerUserId) throws CoBusinessException {
        return cereBuyerReceiveDAO.getAll(buyerUserId);
    }

    @Override
    public CereBuyerReceive findById(Long receiveId) {
        return cereBuyerReceiveDAO.findById(receiveId);
    }

    @Override
    public void updateBuyerData(Long buyerUserId, Long id) throws CoBusinessException {
        cereBuyerReceiveDAO.updateBuyerData(buyerUserId,id);
    }
}
