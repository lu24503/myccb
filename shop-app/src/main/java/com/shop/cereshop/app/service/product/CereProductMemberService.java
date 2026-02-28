/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.product;

import com.shop.cereshop.app.page.index.Product;
import com.shop.cereshop.app.page.product.ProductDetail;
import com.shop.cereshop.app.param.member.MemberProductPageParam;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.domain.common.Page;
import com.shop.cereshop.commons.domain.product.CereProductMember;

import java.util.List;

public interface CereProductMemberService {

    Page getMemberProducts(MemberProductPageParam param);

    CereProductMember selectProductMember(Long memberLevelId, Long productId, Long skuId);

    ProductDetail setActivityInfo(CereBuyerUser user, ProductDetail detail);

    void batchSetActivityInfo(CereBuyerUser user, List<Product> normalProductList);
}
