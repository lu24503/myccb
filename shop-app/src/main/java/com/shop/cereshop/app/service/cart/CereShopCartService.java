/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.cart;

import com.shop.cereshop.app.page.cart.CartSku;
import com.shop.cereshop.app.page.cart.ShopCart;
import com.shop.cereshop.app.param.cart.*;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.exception.CoBusinessException;

import java.util.List;

public interface CereShopCartService {
    void addCart(AddCartParam param, CereBuyerUser user) throws CoBusinessException;

    List<ShopCart> getCart(CereBuyerUser user) throws CoBusinessException;

    void delete(DeleteParam param, CereBuyerUser user) throws CoBusinessException;

    void deleteAll(Long buyerUserId) throws CoBusinessException;

    void selected(SelectedParam param,CereBuyerUser user) throws CoBusinessException;

    void updateNumber(CartUpdateParam param, CereBuyerUser user) throws CoBusinessException;

    void deleteSkus(List<CartSku> carts, Long buyerUserId);

    void addBatchCart(BatchCartParam param, CereBuyerUser user) throws CoBusinessException;

    void buyAgain(BuyerAgainParam param, CereBuyerUser user) throws CoBusinessException;

    void updateSku(UpdateSkuParam param, CereBuyerUser user) throws CoBusinessException;

    Integer findNumber(Long buyerUserId);

    void updateBuyerData(Long buyerUserId, Long id) throws CoBusinessException;

    Boolean clearInvalidSku(Long buyerUserId);
}
