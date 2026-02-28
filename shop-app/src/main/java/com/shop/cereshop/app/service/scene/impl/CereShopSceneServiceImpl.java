/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.scene.impl;

import com.shop.cereshop.app.dao.product.CereSkuMemberRealInfoDAO;
import com.shop.cereshop.app.dao.scene.CereShopSceneDAO;
import com.shop.cereshop.app.dao.scene.CereShopSceneMemberDAO;
import com.shop.cereshop.app.page.index.Product;
import com.shop.cereshop.app.page.product.ProductDetail;
import com.shop.cereshop.app.service.member.CerePlatformMemberLevelService;
import com.shop.cereshop.app.service.product.CereProductSkuService;
import com.shop.cereshop.app.service.scene.CereShopSceneService;
import com.shop.cereshop.app.utils.SceneUtil;
import com.shop.cereshop.commons.constant.IntegerEnum;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.domain.member.CerePlatformMemberLevel;
import com.shop.cereshop.commons.domain.product.Sku;
import com.shop.cereshop.commons.domain.scene.CereShopScene;
import com.shop.cereshop.commons.domain.scene.CereShopSceneMember;
import com.shop.cereshop.commons.utils.EmptyUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CereShopSceneServiceImpl implements CereShopSceneService {

    @Autowired
    private CereShopSceneDAO cereShopSceneDAO;

    @Autowired
    private CerePlatformMemberLevelService cerePlatformMemberLevelService;

    @Autowired
    private CereShopSceneMemberDAO cereShopSceneMemberDAO;

    @Autowired
    private CereProductSkuService cereProductSkuService;

    @Autowired
    private CereSkuMemberRealInfoDAO cereSkuMemberRealInfoDAO;

    @Override
    public List<CereShopScene> selectOnGoingFestivalMarketing() {
        return cereShopSceneDAO.selectOnGoingFestivalMarketing();
    }

    @Override
    public List<CereShopScene> selectOnGoingHolidayMarketing() {
        return cereShopSceneDAO.selectOnGoingHolidayMarketing();
    }

    @Override
    public List<CereShopScene> selectOnGoingBirthDayMarketing() {
        return cereShopSceneDAO.selectOnGoingBirthDayMarketing();
    }

    @Override
    public List<CereShopScene> selectOnGoingMarketingByShopId(Long shopId) {
        return cereShopSceneDAO.selectOnGoingMarketingByShopId(shopId);
    }

    @Override
    public ProductDetail setActivityInfo(CereBuyerUser user, ProductDetail detail) {
        List<CereShopScene> sceneList = cereShopSceneDAO.selectOnGoingMarketingByShopId(detail.getShopId());
        if (CollectionUtils.isNotEmpty(sceneList)) {
            Long memberLevelId = null;
            if (user != null) {
                memberLevelId = user.getMemberLevelId();
            }
            //没登录的情况下默认第一等级会员
            if (memberLevelId == null) {
                CerePlatformMemberLevel firstLevel = cerePlatformMemberLevelService.selectFirstLevel();
                if (firstLevel != null) {
                    memberLevelId = firstLevel.getMemberLevelId();
                }
            }
            // 如果还没有初始化会员等级信息，直接返回商品详情
            if (memberLevelId == null) {
                return detail;
            }
            for (CereShopScene scene:sceneList) {
                CereShopSceneMember sceneMember = cereShopSceneMemberDAO.selectSceneMemberList(scene.getSceneId(), memberLevelId);
                //如果数据没维护好直接返回商品
                if (sceneMember == null || sceneMember.getDiscount() == null) {
                    return detail;
                }
                Integer freeShipping = sceneMember.getIfFreeShipping();
                BigDecimal discount = sceneMember.getDiscount();

                boolean matched = SceneUtil.matchScene(scene.getSceneType(), scene.getSceneTimeType(), scene.getSceneTime(), user != null ? user.getBirthday() : null, false);
                if (matched) {
                    detail.setSceneId(scene.getSceneId());
                    detail.setSceneName(scene.getSceneName());
                    detail.setSceneFreeShipping(freeShipping);
                    detail.setSceneDiscount(discount);
                    detail.setActivityType(IntegerEnum.ACTIVITY_TYPE_SCENE.getCode());
                    detail.setActivityName(IntegerEnum.ACTIVITY_TYPE_SCENE.getName());
                    //设置原价为销售价，设置价格为场景营销的折扣价
                    detail.setOriginalPrice(detail.getPrice());
                    detail.setPrice(detail.getOriginalPrice().multiply(discount).divide(BigDecimal.TEN, 2, BigDecimal.ROUND_HALF_UP));

                    //查询该商品所有组合规格数据封装到map
                    List<Sku> skus=cereProductSkuService.findSimpleSkuByProductId(detail.getProductId());
                    if(!EmptyUtils.isEmpty(skus)) {
                        Map<String, Sku> map = new HashMap<>();
                        String image=detail.getImage();
                        skus.forEach(sku -> {
                            sku.setActivityType(detail.getActivityType());

                            BigDecimal skuPrice = sku.getPrice();
                            sku.setOriginalPrice(skuPrice);
                            sku.setPrice(skuPrice.multiply(discount).divide(BigDecimal.TEN, 2, BigDecimal.ROUND_HALF_UP));

                            if (EmptyUtils.isEmpty(sku.getImage())) {
                                sku.setImage(image);
                            }
                            if (EmptyUtils.isEmpty(sku.getValueCodes())) {
                                sku.setValueCodes("单款项");
                            }
                            map.put(sku.getValueCodes(), sku);
                        });
                        detail.setMap(map);
                    }
                }
                break;
            }
        }
        return detail;
    }

    @Override
    public void batchSetActivityInfo(CereBuyerUser user, List<Product> detailList) {
        if (CollectionUtils.isEmpty(detailList)) {
            return;
        }
        List<CereShopScene> sceneList = cereShopSceneDAO.selectOnGoingMarketingByShopId(detailList.get(0).getShopId());
        if (CollectionUtils.isNotEmpty(sceneList)) {
            Long memberLevelId = null;
            if (user != null) {
                memberLevelId = user.getMemberLevelId();
            }
            //没登录的情况下默认第一等级会员
            if (memberLevelId == null) {
                CerePlatformMemberLevel firstLevel = cerePlatformMemberLevelService.selectFirstLevel();
                if (firstLevel != null) {
                    memberLevelId = firstLevel.getMemberLevelId();
                }
            }
            // 如果还没有初始化会员等级信息，直接返回商品详情
            if (memberLevelId == null) {
                return;
            }
            for (CereShopScene scene:sceneList) {
                CereShopSceneMember sceneMember = cereShopSceneMemberDAO.selectSceneMemberList(scene.getSceneId(), memberLevelId);
                //如果数据没维护好直接返回商品
                if (sceneMember == null) {
                    return;
                }
                Integer freeShipping = sceneMember.getIfFreeShipping();
                BigDecimal discount = sceneMember.getDiscount();

                boolean matched = SceneUtil.matchScene(scene.getSceneType(), scene.getSceneTimeType(), scene.getSceneTime(), user != null ? user.getBirthday() : null, false);
                if (matched) {
                    for (Product detail:detailList) {
                        detail.setActivityType(IntegerEnum.ACTIVITY_TYPE_SCENE.getCode());
                        //设置原价为销售价，设置价格为场景营销的折扣价
                        detail.setOriginalPrice(detail.getPrice());
                        detail.setPrice(detail.getOriginalPrice().multiply(discount).divide(BigDecimal.TEN, 2, BigDecimal.ROUND_HALF_UP));
                    }
                }
                break;
            }
        }
    }

    @Override
    public List<CereShopScene> selectOnGoingMarketing() {
        return cereShopSceneDAO.selectOnGoingMarketing();
    }

    @Override
    public ProductDetail setActivityInfoForRealInfo(CereBuyerUser user, ProductDetail detail) {
        List<CereShopScene> sceneList = cereShopSceneDAO.selectOnGoingMarketingByShopId(detail.getShopId());
        if (CollectionUtils.isNotEmpty(sceneList)) {
            Long memberLevelId = null;
            if (user != null) {
                memberLevelId = user.getMemberLevelId();
            }
            //没登录的情况下默认第一等级会员
            if (memberLevelId == null) {
                CerePlatformMemberLevel firstLevel = cerePlatformMemberLevelService.selectFirstLevel();
                if (firstLevel != null) {
                    memberLevelId = firstLevel.getMemberLevelId();
                }
            }
            // 如果还没有初始化会员等级信息，直接返回商品详情
            if (memberLevelId == null) {
                return detail;
            }
            for (CereShopScene scene : sceneList) {
                CereShopSceneMember sceneMember = cereShopSceneMemberDAO.selectSceneMemberList(scene.getSceneId(), memberLevelId);
                //如果数据没维护好直接返回商品
                if (sceneMember == null || sceneMember.getDiscount() == null) {
                    return detail;
                }
                Integer freeShipping = sceneMember.getIfFreeShipping();
                BigDecimal discount = sceneMember.getDiscount();

                boolean matched = SceneUtil.matchScene(scene.getSceneType(), scene.getSceneTimeType(), scene.getSceneTime(), user != null ? user.getBirthday() : null, false);
                if (matched) {
                    detail.setSceneId(scene.getSceneId());
                    detail.setSceneName(scene.getSceneName());
                    detail.setSceneFreeShipping(freeShipping);
                    detail.setSceneDiscount(discount);
                    detail.setActivityType(IntegerEnum.ACTIVITY_TYPE_SCENE.getCode());
                    detail.setActivityName(IntegerEnum.ACTIVITY_TYPE_SCENE.getName());

                    //前面的findProductDetailBySkuId查询已经匹配上的，就不再需要设置两个价格字段了
                    boolean sameScene = scene.getSceneId().equals(detail.getSceneId());
                    if (!sameScene) {
                        //设置原价为销售价，设置价格为场景营销的折扣价
                        detail.setOriginalPrice(detail.getPrice());
                        detail.setPrice(detail.getOriginalPrice().multiply(discount).divide(BigDecimal.TEN, 2, BigDecimal.ROUND_HALF_UP));
                    }

                    //查询该商品所有组合规格数据封装到map
                    List<Sku> skus = cereSkuMemberRealInfoDAO.findSkuListByProductId(detail.getProductId(), memberLevelId);
                    if (!EmptyUtils.isEmpty(skus)) {
                        Map<String, Sku> map = new HashMap<>();
                        String image = detail.getImage();
                        skus.forEach(sku -> {
                            //生日营销才会走到这个逻辑,因为场景营销在之前的查询中已经查出来了
                            if (!sameScene) {
                                BigDecimal skuPrice = sku.getPrice();
                                sku.setOriginalPrice(skuPrice);
                                sku.setPrice(skuPrice.multiply(discount).divide(BigDecimal.TEN, 2, BigDecimal.ROUND_HALF_UP));
                            }

                            sku.setActivityType(detail.getActivityType());

                            if (EmptyUtils.isEmpty(sku.getImage())) {
                                sku.setImage(image);
                            }
                            if (EmptyUtils.isEmpty(sku.getValueCodes())) {
                                sku.setValueCodes("单款项");
                            }
                            map.put(sku.getValueCodes(), sku);
                        });
                        detail.setMap(map);
                    }
                }
                break;
            }
        }
        return detail;
    }
}
