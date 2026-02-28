/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.buyer;

import com.shop.cereshop.app.page.buyer.MyUser;
import com.shop.cereshop.app.page.login.BuyerUser;
import com.shop.cereshop.app.param.buyer.UserParam;
import com.shop.cereshop.app.param.index.LoginParam;
import com.shop.cereshop.app.param.index.LoginPhoneParam;
import com.shop.cereshop.app.param.index.UpdateAliPhoneParam;
import com.shop.cereshop.app.param.index.UpdateWxPhoneParam;
import com.shop.cereshop.commons.constant.CreditOptTypeEnum;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.domain.canvas.CerePlatformCanvas;
import com.shop.cereshop.commons.domain.common.Page;
import com.shop.cereshop.commons.domain.common.PageParam;
import com.shop.cereshop.commons.exception.CoBusinessException;

public interface CereBuyerUserService {

    CereBuyerUser findByToken(String token);

    CereBuyerUser selectByBuyerUserId(Long buyerUserId);

    BuyerUser wxLogin(LoginParam param) throws CoBusinessException;

    BuyerUser alipayLogin(LoginParam param) throws CoBusinessException;

    BuyerUser login(LoginParam param) throws CoBusinessException;

    void insert(CereBuyerUser buyerUser) throws CoBusinessException;

    void update(CereBuyerUser buyerUser, CereBuyerUser user) throws CoBusinessException;

    void updatePhone(UserParam param, CereBuyerUser user) throws CoBusinessException;

    void relievePhone(UserParam param, CereBuyerUser user) throws CoBusinessException;

    void updatePassword(UserParam param, CereBuyerUser user) throws CoBusinessException;

    MyUser getUser(Long buyerUserId) throws CoBusinessException;

    CerePlatformCanvas getCanvas(CerePlatformCanvas canvas);

    Page getSelfProblems(PageParam param,Long buyerUserId) throws CoBusinessException;

    Page getSelfAnswers(PageParam param,Long buyerUserId) throws CoBusinessException;

    BuyerUser setWxPhone(LoginPhoneParam param) throws CoBusinessException;

    BuyerUser updateWxPhone(UpdateWxPhoneParam param) throws CoBusinessException;

    BuyerUser wxAppLogin(LoginPhoneParam param) throws CoBusinessException;

    BuyerUser updateAliPhone(UpdateAliPhoneParam param) throws CoBusinessException;

    void updateGrowth(Long buyerUserId, Integer growth);

    int increaseCredit(Long buyerUserId, Integer credit, CreditOptTypeEnum typeEnum);

    int decreaseCredit(Long buyerUserId, Integer credit, CreditOptTypeEnum typeEnum) throws CoBusinessException;

    void rollbackCreditByOrderId(Long orderId, Long buyerUserId);

    void delete(CereBuyerUser user) throws CoBusinessException;

    boolean trackReport(Long buyerUserId, Integer eventType, String productIds);

    void updateLastLoginIp(Long buyerUserId, String ip);
}
