/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.distributor.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.shop.cereshop.app.dao.distributor.CereShopDistributorDAO;
import com.shop.cereshop.app.dao.order.CereShopOrderDAO;
import com.shop.cereshop.app.page.order.ShopDistributor;
import com.shop.cereshop.app.param.distributor.ShopDistributorParam;
import com.shop.cereshop.app.param.extension.ExtensionParam;
import com.shop.cereshop.app.redis.service.api.StringRedisService;
import com.shop.cereshop.app.service.distributor.CereDistributorBuyerService;
import com.shop.cereshop.app.service.distributor.CereShopDistributorService;
import com.shop.cereshop.app.service.order.CereShopOrderService;
import com.shop.cereshop.app.service.redis.CereRedisKeyServcice;
import com.shop.cereshop.commons.constant.CoReturnFormat;
import com.shop.cereshop.commons.constant.IntegerEnum;
import com.shop.cereshop.commons.constant.StringEnum;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.domain.distributor.CereDistributorBuyer;
import com.shop.cereshop.commons.domain.distributor.CereShopDistributor;
import com.shop.cereshop.commons.domain.shop.CereShopRecruit;
import com.shop.cereshop.commons.domain.shop.CereShopRelationship;
import com.shop.cereshop.commons.exception.CoBusinessException;
import com.shop.cereshop.commons.utils.EmptyUtils;
import com.shop.cereshop.commons.utils.RandomStringUtil;
import com.shop.cereshop.commons.utils.TimeUtils;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Objects;

@Service
public class CereShopDistributorServiceImpl implements CereShopDistributorService {

    @Autowired
    private CereShopDistributorDAO cereShopDistributorDAO;

    @Autowired
    private CereDistributorBuyerService cereDistributorBuyerService;

    @Autowired
    private StringRedisService stringRedisService;

    @Autowired
    private CereRedisKeyServcice cereRedisKeyServcice;

    @Autowired
    private CereShopOrderService cereShopOrderService;

    @Override
    public ShopDistributor  findByPhone(String phone,Long shopId) {
        return cereShopDistributorDAO.findByPhone(phone,shopId);
    }

    @Override
    @Transactional(isolation= Isolation.DEFAULT,propagation= Propagation.REQUIRED,rollbackFor = {CoBusinessException.class, Exception.class})
    public void addDistributor(ShopDistributorParam param) throws CoBusinessException {
        String time= TimeUtils.yyMMddHHmmss();
        //查询当前手机号是否已存在分销员数据
        CereShopDistributor cereShopDistributor=cereShopDistributorDAO.checkPhone(param.getDistributorPhone(),param.getShopId());
        if(cereShopDistributor!=null){
//            if(IntegerEnum.DISTRIBUTOR_ERROR.getCode().equals(cereShopDistributor.getState())){
//                throw new CoBusinessException(CoReturnFormat.DISTRIBUTOR_ERROR);
//            }
            if(IntegerEnum.NO.getCode().equals(cereShopDistributor.getIfLiquidation())){
                throw new CoBusinessException(CoReturnFormat.PHONE_NOT_DISTRIBUTOR);
            }
        }
        //查询最低等级分销员等级
        Long levelId=cereShopDistributorDAO.findMinLevel(param.getShopId());
        if(!EmptyUtils.isEmpty(levelId)){
            //默认新增
            boolean flag=true;
            if(cereShopDistributor!=null){
                //更新
                flag=false;
                cereShopDistributor.setUpdateTime(time);
            }else {
                //新增分销员数据
                cereShopDistributor=new CereShopDistributor();
                cereShopDistributor.setDistributorName(param.getDistributorName());
                cereShopDistributor.setDistributorPhone(param.getDistributorPhone());
                cereShopDistributor.setCreateTime(time);
            }
            cereShopDistributor.setDistributorLevelId(levelId);
            //根据邀请码查询上级分销员id
            CereShopDistributor distributor=cereShopDistributorDAO.findInvitees(param.getInvitationCode());
            if(distributor!=null){
                //查询该分销员所属店铺关系设置数据
                CereShopRelationship relationship=cereShopDistributorDAO.findRelation(distributor.getShopId());
                if(relationship!=null){
                    //校验是否允许邀请下级
                    if(IntegerEnum.YES.getCode().equals(relationship.getIfInvitation())){
                        //允许
                        cereShopDistributor.setInvitees(distributor.getDistributorId());
                    }
                }
            }
            cereShopDistributor.setShopId(param.getShopId());
            cereShopDistributor.setState(IntegerEnum.DISTRIBUTOR_STAY.getCode());
            //生成10位数随机邀请码
            cereShopDistributor.setInvitationCode(RandomStringUtil.getRandomCode(10,0));
            //默认未清退
            cereShopDistributor.setIfLiquidation(IntegerEnum.NO.getCode());
            //查询店铺招募设置信息
            CereShopRecruit cereShopRecruit=cereShopDistributorDAO.findShopRecruit(param.getShopId());
            if(cereShopRecruit!=null&&IntegerEnum.NO.getCode().equals(cereShopRecruit.getIfExamine())){
                //设置状态为审核通过
                cereShopDistributor.setState(IntegerEnum.DISTRIBUTOR_SUCCESS.getCode());
            }
            if(flag){
                cereShopDistributorDAO.insert(cereShopDistributor);
            }else {
                cereShopDistributorDAO.updateByPrimaryKeySelective(cereShopDistributor);
            }
        }else {
            throw new CoBusinessException(CoReturnFormat.NOT_HAVE_DISTRIBUTOR_LEVEL);
        }
    }

    @Override
    public String findInvitationCode(Long distributorId) {
        return cereShopDistributorDAO.findInvitationCode(distributorId);
    }

    @Override
    public CereShopDistributor check(Long shopId, Long buyerUserId) throws CoBusinessException {
        CereShopDistributor dis = cereShopDistributorDAO.check(shopId,buyerUserId);
        if (dis != null) {
            return dis;
        }
        CereShopRecruit recruit = cereShopDistributorDAO.findShopRecruit(shopId);
        if (recruit != null) {
            if (Objects.equals(recruit.getPurchaseEverything(), 1)) {
                int count = cereShopOrderService.findUserShopOrderCount(shopId, buyerUserId);
                if (count == 0) {
                    throw new CoBusinessException("未满足购买任意商品的条件，无法申请成为分销员");
                }
            } else if (Objects.equals(recruit.getOrderFrequency(), 1)) {
                int count = cereShopOrderService.findUserShopOrderCount(shopId, buyerUserId);
                if (recruit.getFrequency() != null && count < recruit.getFrequency()) {
                    throw new CoBusinessException("下单未满" + recruit.getFrequency() + "单，无法申请成为分销员");
                }
            } else {
                BigDecimal consumption = cereShopOrderService.findUserShopOrderConsumption(shopId, buyerUserId);
                if (recruit.getMoney() != null && consumption != null && consumption.compareTo(recruit.getMoney()) < 0) {
                    throw new CoBusinessException("消费未满" + recruit.getMoney() + "元，无法申请成为分销员");
                }
            }
        }
        return null;
    }

    @Override
    @Transactional(isolation= Isolation.DEFAULT,propagation= Propagation.REQUIRED,rollbackFor = {CoBusinessException.class, Exception.class})
    public void bind(ExtensionParam param, CereBuyerUser user) throws CoBusinessException,Exception {
        //定义是否新增绑定关系(默认不新增)
        boolean flag=true;
        String time =TimeUtils.yyMMddHHmmss();
        //查询当前分销员是否已绑定过该用户
        CereDistributorBuyer cereDistributorBuyer=cereDistributorBuyerService.findByDisAndUser(param.getDistributorId(),user.getBuyerUserId());
        // 之前已绑定，且当前依旧保持着绑定关系，则直接返回
        if (cereDistributorBuyer != null && cereDistributorBuyer.getIfBind().equals(IntegerEnum.YES.getCode())) {
            return;
        }
        //查询当前客户是否已绑定其他分销员
        CereDistributorBuyer distributorBuyer=cereDistributorBuyerService.checkUser(param.getDistributorId(),user.getBuyerUserId());
        if(distributorBuyer!=null){
            //如果绑定了,查询该分销员所属店铺关系设置数据
            CereShopRelationship relationship=cereShopDistributorDAO.findRelation(distributorBuyer.getShopId());
            if(relationship!=null){
                if(IntegerEnum.CAN_GRAB_USER.getCode().equals(relationship.getIfRobbing())){
                    //随时可抢
                }else if(IntegerEnum.NOT_GRAB_USER.getCode().equals(relationship.getIfRobbing())){
                    //不允许抢客
                    flag=false;
                }else {
                    //保护期几天内不允许抢
                    if(!EmptyUtils.isEmpty(relationship.getRobbingDay())){
                        //计算关系绑定时间到当前时间有效天数
                        int day=TimeUtils.differentDaysByMillisecond(distributorBuyer.getUpdateTime(),time);
                        if(day<=relationship.getRobbingDay()){
                            //如果时间没有超出了保护期天数,不允许抢客
                            flag=false;
                        }
                    }
                }
            }
        }
        if(flag){
            //允许抢客 - 如果之前已绑定其它分销员,解除之前分销员绑定关系
            if (distributorBuyer != null) {
                distributorBuyer.setIfBind(IntegerEnum.NO.getCode());
                distributorBuyer.setUpdateTime(time);
                cereDistributorBuyerService.update(distributorBuyer);
            }
            if(cereDistributorBuyer!=null){
                //更新绑定状态
                cereDistributorBuyer.setIfBind(IntegerEnum.YES.getCode());
                cereDistributorBuyer.setUpdateTime(time);
                cereDistributorBuyerService.update(cereDistributorBuyer);
            }else {
                //新增绑定数据
                cereDistributorBuyer=new CereDistributorBuyer();
                cereDistributorBuyer.setDistributorId(param.getDistributorId());
                cereDistributorBuyer.setBuyerUserId(user.getBuyerUserId());
                cereDistributorBuyer.setShopId(param.getShopId());
                cereDistributorBuyer.setIfBind(IntegerEnum.YES.getCode());
                cereDistributorBuyer.setUpdateTime(time);
                cereDistributorBuyerService.insert(cereDistributorBuyer);
            }
            //根据店铺id查询关系设置数据
            CereShopRelationship relationship=cereShopDistributorDAO.findRelation(param.getShopId());
            if(relationship!=null){
                if(!EmptyUtils.isEmpty(relationship.getValidityDay())){
                    //如果设置了有效天数,在有效天数达到之后解除绑定关系
                    stringRedisService.set(StringEnum.SHOP_VALIDITY_DAT.getCode()+"-"+cereDistributorBuyer.getDistributorId()+
                                    "-"+cereDistributorBuyer.getBuyerUserId(),1,
                            relationship.getValidityDay()*24*60*60*1000);
                    //更新redis延时任务记录
                    cereRedisKeyServcice.updateByKey(StringEnum.SHOP_VALIDITY_DAT.getCode()+"-"+cereDistributorBuyer.getDistributorId()+
                                    "-"+cereDistributorBuyer.getBuyerUserId(),
                            TimeUtils.getMoreDayAfter(TimeUtils.yyMMddHHmmss(),relationship.getValidityDay()));
                }
            }
        } else {
            //不允许抢客,不需要报错
            //throw new CoBusinessException(CoReturnFormat.NOT_ROBBING);
        }
    }

    @Override
    public String getInvitationCode(Long shopId, String phone) {
        CereShopDistributor distributor = cereShopDistributorDAO.selectOne(Wrappers.<CereShopDistributor>lambdaQuery()
                .eq(CereShopDistributor::getShopId, shopId)
                .eq(CereShopDistributor::getDistributorPhone, phone));
        if (distributor != null) {
            return distributor.getInvitationCode();
        }
        return "";
    }
}
