/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.order;

import com.shop.cereshop.app.page.cart.CartSku;
import com.shop.cereshop.app.page.order.*;
import com.shop.cereshop.app.page.product.BroadCastDTO;
import com.shop.cereshop.app.page.settlement.Distribution;
import com.shop.cereshop.app.page.settlement.Settlement;
import com.shop.cereshop.app.page.settlement.SettlementShop;
import com.shop.cereshop.app.param.comment.CommentSaveParam;
import com.shop.cereshop.app.param.order.*;
import com.shop.cereshop.app.param.settlement.SettlementParam;
import com.shop.cereshop.commons.domain.after.CereAfterDilever;
import com.shop.cereshop.commons.domain.buyer.CereBuyerReceive;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.domain.common.Page;
import com.shop.cereshop.commons.domain.logistics.CereOrderLogistics;
import com.shop.cereshop.commons.domain.order.CereShopOrder;
import com.shop.cereshop.commons.domain.product.CereProductSku;
import com.shop.cereshop.commons.exception.CoBusinessException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface CereShopOrderService {
    Settlement getSettlement(SettlementParam param, CereBuyerUser user) throws CoBusinessException;

    PayUrl submit(OrderParam param, CereBuyerUser user, String ip) throws CoBusinessException,Exception;

    OrderPay gotoPay(PayParam param, CereBuyerUser user, String ip) throws Exception;

    void handleWxLog(String orderFormId, String transaction_id, String orderNo) throws Exception;

    Page getAll(OrderGetAllParam param,CereBuyerUser user) throws CoBusinessException,Exception;

    OrderDetail getById(OrderGetByIdParam param) throws CoBusinessException,Exception;

    void confirm(OrderGetByIdParam param, CereBuyerUser user) throws CoBusinessException;

    void update(CereShopOrder cereShopOrder) throws CoBusinessException;

    void cancel(OrderGetByIdParam param, CereBuyerUser user) throws CoBusinessException,Exception;

    void addComment(CommentSaveParam param, CereBuyerUser user) throws CoBusinessException;

    OrderPay gotoAppPay(PayParam param, CereBuyerUser user, String ip) throws CoBusinessException,Exception;

    OrderPay gotoH5Pay(PayParam param, CereBuyerUser user, String ip) throws CoBusinessException,Exception;

    void addToComment(CommentSaveParam param, CereBuyerUser user) throws CoBusinessException;

    void delete(OrderGetByIdParam param, CereBuyerUser user) throws CoBusinessException;

    void returnExpress(CereAfterDilever dilever, CereBuyerUser user) throws CoBusinessException;

    List<Dilevery> getDilevery(DileveryParam param) throws CoBusinessException,Exception;

    void updateBatchStock(List<CereProductSku> skus) throws CoBusinessException;

    void pay(PayParam param) throws CoBusinessException,Exception;

    PayUrl getUrl(OrderGetByIdParam param, CereBuyerUser user, String ip) throws CoBusinessException,Exception;

    PayUrl checkPay(PayParam param) throws CoBusinessException;

    List<CartSku> refund(OrderGetByIdParam param, CereBuyerUser user) throws CoBusinessException;

    void handleRefundWxLog(String orderFormId, String transaction_id, String orderNo) throws Exception;

    SettlementShop findSettlementShop(Long shopId);

    Distribution setLogisticPrice(List<CereOrderLogistics> logistics, List<CartSku> skus, CereBuyerReceive receive,
                                  Map<Long, Integer> map) throws CoBusinessException;

    void cancelBatch(List<Long> ids) throws CoBusinessException,Exception;

    CereShopOrder findById(Long orderId);

    void updateBuyerData(Long buyerUserId, Long id) throws CoBusinessException;

    void payGift(String orderFormId);

    OrderPoliteDTO getOrderPolite(Long buyerUserId, Long orderId);

    Integer getWaitPayNumByHours(Integer hours, Long buyerUserId);

    Integer getOrderNumByHours(Integer hours, Long buyerUserId);

    /**
     * 微信用户支付成功
     * @param param PayParam拼单id和订单id必传
     * @param user 用户信息
     * @param ip 用户ip
     */
    void paySuccess(PayParam param, CereBuyerUser user, String ip) throws CoBusinessException;

    /**
     * 查询付款人数，如果skuId不传，则只查询商品级别
     * @param productId
     * @param skuId
     * @return
     */
    Integer findPayUsers(Long productId, Long skuId);

    /**
     * 查询某个商品1小时内的下单
     * @param oneHourAgo
     * @param productId
     * @return
     */
    List<BroadCastDTO> findRecentOrder(String oneHourAgo, Long productId);

    int findUserShopOrderCount(Long shopId, Long buyerUserId);

    BigDecimal findUserShopOrderConsumption(Long shopId, Long buyerUserId);
}
