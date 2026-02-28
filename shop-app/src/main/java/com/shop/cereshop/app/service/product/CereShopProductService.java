/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.product;

import com.shop.cereshop.app.page.index.Product;
import com.shop.cereshop.app.page.product.BroadCastDTO;
import com.shop.cereshop.app.page.product.ProductDetail;
import com.shop.cereshop.app.page.product.ProductSkus;
import com.shop.cereshop.app.page.product.ShareProduct;
import com.shop.cereshop.app.param.canvas.CanvasAppProductParam;
import com.shop.cereshop.app.param.index.SearchParam;
import com.shop.cereshop.app.param.product.PageProductParam;
import com.shop.cereshop.app.param.product.ProductParam;
import com.shop.cereshop.app.param.product.ShareParam;
import com.shop.cereshop.app.param.product.SkuParam;
import com.shop.cereshop.commons.cache.product.ProductBo;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.domain.common.Page;
import com.shop.cereshop.commons.domain.distributor.CereShopDistributor;
import com.shop.cereshop.commons.domain.product.CereShopProduct;
import com.shop.cereshop.commons.domain.product.Classify;
import com.shop.cereshop.commons.exception.CoBusinessException;

import java.util.List;
import java.util.Map;

public interface CereShopProductService {

    CereShopProduct selectByPrimaryKey(Long id);

    ProductDetail getById(ProductParam param, CereBuyerUser user) throws CoBusinessException,Exception;

    ProductSkus getProducts(SkuParam param) throws CoBusinessException,Exception;

    List<Product> getSearchProducts(SearchParam param) throws CoBusinessException;

    ShareProduct share(ShareParam param, CereBuyerUser user) throws CoBusinessException,Exception;

    Page getCanvasProducts(CanvasAppProductParam param) throws CoBusinessException,Exception;

    Page getProducts2(CanvasAppProductParam param) throws CoBusinessException,Exception;

    List<Classify> getClassify() throws CoBusinessException;

    CereShopDistributor check(Long shopId, CereBuyerUser user) throws CoBusinessException;

    void deleteAllSearch(CereBuyerUser user) throws CoBusinessException;

    List<String> findImages(Long productId);

    List<String> findSkuImages(Long productId);

    List<Product> findSimilarProducts(Long classifyId);

    List<Long> findClassifyNumber(Long shopId);

    Integer findPayNumber(Long skuId);

    String getSharePic(ProductParam param, CereBuyerUser user) throws CoBusinessException;

    Page getRandomSortProduct(PageProductParam param);

    ProductBo fetchProductCache(Long shopId, Long productId, Long skuId, CereBuyerUser user, boolean needRealtimeStock);

    Map<Long, Product> getSimpleActivityInfo(Long shopId, List<Long> skuIdList, CereBuyerUser user);

    int selectFictitiousNumber(Long productId);

    ProductSkus getSpecificSku(SkuParam param);

    List<BroadCastDTO> getBroadCastList(Long productId, Long shopGroupWorkId, Long buyerUserId);

    ShareProduct getShareProductParam(ProductParam param, CereBuyerUser user) throws CoBusinessException;
}
