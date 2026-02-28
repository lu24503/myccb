/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.platformtool;

import com.shop.cereshop.app.page.canvas.CanvasPlatformSeckill;
import com.shop.cereshop.app.page.product.ProductDetail;
import com.shop.cereshop.app.page.seckill.PlatformSeckillProduct;
import com.shop.cereshop.app.param.canvas.RenovationParam;
import com.shop.cereshop.app.param.seckill.PlatformSeckillParam;
import com.shop.cereshop.app.param.seckill.ShopPlatformSeckill;
import com.shop.cereshop.commons.domain.buyer.CereBuyerPlatformSeckillVisit;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.domain.common.Page;
import com.shop.cereshop.commons.domain.platformtool.CerePlatformSeckill;

import java.util.List;

public interface CerePlatformSeckillService {

    /**
     * 查询平台秒杀今日场次
     * @return
     */
    List<String> querySession();

    /**
     * 根据场次查询秒杀商品列表
     * @param platformSeckillParam
     * @return
     */
    Page<PlatformSeckillProduct> queryProductListBySession(PlatformSeckillParam platformSeckillParam);

    /**
     * 查询平台端秒杀商品
     * @param param
     * @return
     */
    List<CanvasPlatformSeckill> getPlatformSeckills(RenovationParam param);

    /**
     * 根据id查询
     * @param platformSeckillId
     * @return
     */
    CerePlatformSeckill findById(Long platformSeckillId);

    /**
     * 插入平台秒杀访问记录
     * @param cereBuyerPlatformSeckillVisit
     */
    void insertVisit(CereBuyerPlatformSeckillVisit cereBuyerPlatformSeckillVisit);

    /**
     * 根据秒杀id, 查询所有的报名成功的商品
     * @param seckillId
     * @return
     */
    List<Long> queryProductIdListBySeckillId(Long seckillId);

    List<ShopPlatformSeckill> selectPlatformSeckillsByShopIdList(List<Long> shopIdList);

    ProductDetail setActivityInfo(Long platformSeckillId, CereBuyerUser user, ProductDetail detail);

    List<CerePlatformSeckill> queryPlatformSeckillList();

    ProductDetail setActivityInfoForRealInfo(Long platformSeckillId, CereBuyerUser user, ProductDetail detail);
}
