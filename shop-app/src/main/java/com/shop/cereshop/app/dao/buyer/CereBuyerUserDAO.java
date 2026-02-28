/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.dao.buyer;

import com.shop.cereshop.app.page.buyer.MyUser;
import com.shop.cereshop.app.page.login.BuyerUser;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.domain.canvas.CerePlatformCanvas;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CereBuyerUserDAO extends BaseMapper<CereBuyerUser> {
    int deleteByPrimaryKey(@Param("buyerUserId") Long buyerUserId);

    int insertSelective(CereBuyerUser record);

    CereBuyerUser selectByPrimaryKey(Long buyerUserId);


    int updateByPrimaryKeySelective(CereBuyerUser record);

    int updateByPrimaryKey(CereBuyerUser record);

    CereBuyerUser findByToken(@Param("token") String token);

    BuyerUser findByOpenid(@Param("openid") String openid);

    BuyerUser findByAliUserId(@Param("aliUserId") String aliUserId);

    BuyerUser findByPhone(@Param("phone") String phone);

    void relievePhone(CereBuyerUser cereBuyerUser);

    MyUser getUser(@Param("buyerUserId") Long buyerUserId);

    CerePlatformCanvas getCanvas(CerePlatformCanvas canvas);

    CereBuyerUser getByOpenid(@Param("wechatOpenId") String wechatOpenId);

    CereBuyerUser checkPhoneWx(@Param("phone") String phone);

    CereBuyerUser checkPhoneAli(@Param("phone") String phone, @Param("buyerUserId") Long buyerUserId);

    CereBuyerUser selectByPhone(@Param("phone") String phone);

    void updateGrowth(@Param("buyerUserId") Long buyerUserId, @Param("growth") Integer growth);

    int increaseCredit(@Param("buyerUserId") Long buyerUserId, @Param("credit") int credit);

    int decreaseCredit(@Param("buyerUserId") Long buyerUserId, @Param("credit") int credit);
}
