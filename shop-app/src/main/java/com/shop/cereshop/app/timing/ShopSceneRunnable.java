/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.timing;

import com.alibaba.fastjson.JSON;
import com.shop.cereshop.app.param.coupon.CouponParam;
import com.shop.cereshop.app.service.activity.CereBuyerCouponService;
import com.shop.cereshop.app.service.coupon.CereShopCouponService;
import com.shop.cereshop.app.service.scene.CereShopSceneMemberCouponService;
import com.shop.cereshop.app.service.scene.CereShopSceneService;
import com.shop.cereshop.app.utils.SceneUtil;
import com.shop.cereshop.commons.constant.CoReturnFormat;
import com.shop.cereshop.commons.constant.IntegerEnum;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.domain.scene.CereShopScene;
import com.shop.cereshop.commons.domain.scene.CereShopSceneMemberCoupon;
import com.shop.cereshop.commons.domain.tool.CereShopCoupon;
import com.shop.cereshop.commons.exception.CoBusinessException;
import com.shop.cereshop.commons.utils.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 场景营销定时器
 */
@Slf4j(topic = "ShopSceneRunnable")
public class ShopSceneRunnable implements Runnable {

    private CereShopSceneService cereShopSceneService;

    private CereShopSceneMemberCouponService cereShopSceneMemberCouponService;

    private CereShopCouponService cereShopCouponService;

    private CereBuyerCouponService cereBuyerCouponService;

    public ShopSceneRunnable() {
        this.cereShopSceneService = SpringUtil.getBean(CereShopSceneService.class);
        this.cereShopSceneMemberCouponService = SpringUtil.getBean(CereShopSceneMemberCouponService.class);
        this.cereShopCouponService = SpringUtil.getBean(CereShopCouponService.class);
        this.cereBuyerCouponService = SpringUtil.getBean(CereBuyerCouponService.class);
    }

    @Override
    @Transactional(isolation= Isolation.DEFAULT,propagation= Propagation.REQUIRED,rollbackFor = {CoBusinessException.class, Exception.class})
    public void run() {
        try {
            List<CereShopScene> sceneList = cereShopSceneService.selectOnGoingMarketing();
            for (CereShopScene scene:sceneList) {
                log.info("onGoing sceneId {}", scene.getSceneId());
                // 节日营销和会员日营销，优先匹配，如果不符合继续下个场景营销
                Integer sceneType = scene.getSceneType();
                Integer sceneTimeType = scene.getSceneTimeType();
                String sceneTime = scene.getSceneTime();
                if (IntegerEnum.SCENE_TYPE_FESTIVAL.getCode().equals(sceneType) || IntegerEnum.SCENE_TYPE_MEMBER.getCode().equals(sceneType)) {
                    boolean matched = SceneUtil.matchScene(sceneType, sceneTimeType, sceneTime, null, false);
                    if (!matched) {
                        continue;
                    }
                }
                List<CereShopSceneMemberCoupon> couponList = cereShopSceneMemberCouponService.selectBySceneId(scene.getSceneId());
                log.info("sceneCoupon sceneId {}, couponList.size {}", scene.getSceneId(), couponList.size());
                if (CollectionUtils.isEmpty(couponList)) {
                    continue;
                }
                List<Long> couponIdList = couponList.stream().map(CereShopSceneMemberCoupon::getCouponId).distinct().collect(Collectors.toList());
                log.info("sceneCoupon sceneId {}, couponIdList {}", scene.getSceneId(), couponIdList);
                Map<Long, List<Long>> couponIdMemberLevelIdMap = couponList.stream().collect(Collectors.groupingBy(CereShopSceneMemberCoupon::getCouponId, Collectors.mapping(CereShopSceneMemberCoupon::getMemberLevelId, Collectors.toCollection(ArrayList::new))));
                log.info("sceneCoupon sceneId {}, couponIdMemberLevelIdMap {}", scene.getSceneId(), JSON.toJSONString(couponIdMemberLevelIdMap));
                List<CereShopCoupon> shopCouponList = cereShopCouponService.findOnGoingCouponByBatchId(couponIdList);
                if (CollectionUtils.isEmpty(shopCouponList)) {
                    continue;
                }
                for (CereShopCoupon shopCoupon:shopCouponList) {
                    Long couponId = shopCoupon.getShopCouponId();
                    log.info("sceneCoupon sceneId {}, for loop couponId {}", scene.getSceneId(), couponId);
                    List<CereBuyerUser> buyerUserList = cereShopCouponService.selectCanTakeCouponUser(scene.getShopId(), couponId,
                            shopCoupon.getReceiveType(), shopCoupon.getFrequency());
                    log.info("sceneCoupon sceneId {}, canTakeCouponBuyerUserList {}", scene.getSceneId(), JSON.toJSONString(buyerUserList));
                    if (CollectionUtils.isEmpty(buyerUserList)) {
                        continue;
                    }
                    Map<Long, List<CereBuyerUser>> buyerUserMap = buyerUserList.stream().filter(u -> u.getMemberLevelId() != null)
                            .collect(Collectors.groupingBy(CereBuyerUser::getMemberLevelId));
                    List<Long> memberLevelIdList= couponIdMemberLevelIdMap.get(couponId);
                    if (CollectionUtils.isNotEmpty(memberLevelIdList)) {
                        memberLevelIdList = memberLevelIdList.stream().distinct().collect(Collectors.toList());
                        currentCouponLabel:
                            for (Long memberLevelId:memberLevelIdList) {
                                List<CereBuyerUser> currentLevelUserList = buyerUserMap.get(memberLevelId);
                                if (CollectionUtils.isEmpty(currentLevelUserList)) {
                                    continue;
                                }
                                for (CereBuyerUser user:currentLevelUserList) {
                                    boolean matched = true;
                                    if (IntegerEnum.SCENE_TYPE_BIRTHDAY.getCode().equals(sceneType)) {
                                        matched = SceneUtil.matchScene(sceneType, sceneTimeType, sceneTime, user.getBirthday(), false);
                                    }
                                    if (!matched) {
                                        continue;
                                    }
                                    log.info("matchScene sceneId {}, couponId {}, buyerUserId {}, birthday {}", scene.getSceneId(), couponId, user.getBuyerUserId(), user.getBirthday());
                                    CouponParam couponParam = new CouponParam();
                                    couponParam.setShopCouponId(couponId);
                                    couponParam.setSource(IntegerEnum.COUPON_SOURCE_MARKET_SEND.getCode());
                                    try {
                                        cereBuyerCouponService.takeCoupon(couponParam, user);
                                    } catch(CoBusinessException coBusinessException) {
                                        // 如果报的错是该优惠券已领取完，则可以退出循环
                                        if (CoReturnFormat.COUPON_RECEIVE_FINISH.equals(coBusinessException.getCode())) {
                                            log.warn("sceneRunnable takeCoupon receive finish: shopId = {}, couponId = {}, buyerUserId = {}", scene.getShopId(), couponId, user.getBuyerUserId());
                                            break currentCouponLabel;
                                        }
                                        // 如果报的错是该用户领取达到上限，只打印日志即可
                                        if (CoReturnFormat.COUPON_TAKE_UPPER_LIMIT.equals(coBusinessException.getCode())) {
                                            log.warn("sceneRunnable takeCoupon take upper limit: shopId = {}, couponId = {}, buyerUserId = {}", scene.getShopId(), couponId, user.getBuyerUserId());
                                        }
                                    } catch (Exception e) {
                                        log.error("sceneRunnable takeCoupon failed: shopId = {}, couponId = {}, buyerUserId = {}", scene.getShopId(), couponId, user.getBuyerUserId(), e);
                                    }
                                }
                            }
                    }
                }

            }
        } catch (Exception e) {
            log.error("ShopSceneRunnable error: {}", e.getMessage(), e);
        }
        /*try {
            // 查询是否有匹配当前的节日营销
            // 根据营销的配置，查询各自商家的会员
            // 如果是按照等级的，则分不同等级
            // 发放优惠券

            //商家id-该商家下的客户
            Map<Long, Map<Long,List<CereBuyerUser>>> buyerMap = new HashMap<>();
            // 查询进行中的节日营销
            List<CereShopScene> festivalSceneList = cereShopSceneService.selectOnGoingFestivalMarketing();
            for (CereShopScene scene:festivalSceneList) {
                List<CereShopSceneMemberCoupon> couponList = cereShopSceneMemberCouponService.selectBySceneId(scene.getSceneId());
                if (CollectionUtils.isNotEmpty(couponList)) {
                    List<Long> couponIdList = couponList.stream().map(CereShopSceneMemberCoupon::getCouponId).distinct().collect(Collectors.toList());
                    List<CerePlatformActivity> platformCouponList = cerePlatformActivityService.findOnGoingCouponByBatchId(couponIdList);
                    if (CollectionUtils.isEmpty(platformCouponList)) {
                        continue;
                    }
                    *//*Map<Long, CerePlatformActivity> couponMap = new HashMap<>();
                    for (CerePlatformActivity activity:platformCouponList) {
                        couponMap.put(activity.getActivityId(), activity);
                    }*//*

                    //查询出商家的会员
                    Map<Long,List<CereBuyerUser>> shopMemberLevelBuyerMap = buyerMap.get(scene.getShopId());
                    if (shopMemberLevelBuyerMap == null) {
                        List<CereBuyerUser> buyerUserList = cereBusinessBuyerUserService.selectBuyerUserByShopId(scene.getShopId());
                        if (CollectionUtils.isNotEmpty(buyerUserList)) {
                            shopMemberLevelBuyerMap = buyerUserList.stream().collect(Collectors.groupingBy(CereBuyerUser::getMemberLevelId));
                        } else {
                            //没有商家会员，放入一个空的map，这样下次循环就不需要再次循环了
                            shopMemberLevelBuyerMap = new HashMap<>();
                        }
                        buyerMap.put(scene.getShopId(), shopMemberLevelBuyerMap);
                    }
                    if (buyerMap.get(scene.getShopId()).size() != 0) {
                        for (CereShopSceneMemberCoupon coupon:couponList) {
                            Long memberLevelId = coupon.getMemberLevelId();
                            List<CereBuyerUser> userList = shopMemberLevelBuyerMap.get(memberLevelId);
                            //发放优惠券
                            for (CereBuyerUser user:userList) {
                                CouponParam couponParam = new CouponParam();
                                couponParam.setCouponId(coupon.getCouponId());
                                try {
                                    cereBuyerCouponService.takeCoupon(couponParam, user);
                                } catch(CoBusinessException coBusinessException) {
                                    // 如果报的错是该优惠券已领取完，则可以退出循环
                                    if (CoReturnFormat.COUPON_RECEIVE_FINISH.equals(coBusinessException.getCode())) {
                                        log.warn("sceneRunnable festival takeCoupon receive finish: shopId = {}, couponId = {}, buyerUserId = {}", scene.getShopId(), coupon.getCouponId(), user.getBuyerUserId());
                                        break;
                                    }
                                    // 如果报的错是该用户领取达到上限，只打印日志即可
                                    if (CoReturnFormat.COUPON_TAKE_UPPER_LIMIT.equals(coBusinessException.getCode())) {
                                        log.warn("sceneRunnable festival takeCoupon take upper limit: shopId = {}, couponId = {}, buyerUserId = {}", scene.getShopId(), coupon.getCouponId(), user.getBuyerUserId());
                                    }
                                } catch (Exception e) {
                                    log.error("sceneRunnable festival takeCoupon failed: shopId = {}, couponId = {}, buyerUserId = {}", scene.getShopId(), coupon.getCouponId(), user.getBuyerUserId(), e);
                                }
                            }
                        }
                    }
                }
            }

            //查询进行中的会员日营销
            List<CereShopScene> holidaySceneList = cereShopSceneService.selectOnGoingHolidayMarketing();
            if (CollectionUtils.isNotEmpty(holidaySceneList)) {
                for (CereShopScene scene:holidaySceneList) {
                    boolean matched = SceneUtil.matchScene(scene.getSceneType(), scene.getSceneTimeType(), scene.getSceneTime(), null, true);

                    //匹配时间条件，发放优惠券
                    if (matched) {
                        // 时间匹配之后，开始查出优惠券
                        List<CereShopSceneMemberCoupon> couponList = cereShopSceneMemberCouponService.selectBySceneId(scene.getSceneId());
                        if (CollectionUtils.isNotEmpty(couponList)) {
                            List<Long> couponIdList = couponList.stream().map(CereShopSceneMemberCoupon::getCouponId).distinct().collect(Collectors.toList());
                            List<CerePlatformActivity> platformCouponList = cerePlatformActivityService.findOnGoingCouponByBatchId(couponIdList);
                            if (CollectionUtils.isEmpty(platformCouponList)) {
                                continue;
                            }

                            //查询出商家的会员
                            Map<Long,List<CereBuyerUser>> shopMemberLevelBuyerMap = buyerMap.get(scene.getShopId());
                            if (shopMemberLevelBuyerMap == null) {
                                List<CereBuyerUser> buyerUserList = cereBusinessBuyerUserService.selectBuyerUserByShopId(scene.getShopId());
                                if (CollectionUtils.isNotEmpty(buyerUserList)) {
                                    shopMemberLevelBuyerMap = buyerUserList.stream().collect(Collectors.groupingBy(CereBuyerUser::getMemberLevelId));
                                } else {
                                    //没有商家会员，放入一个空的map，这样下次循环就不需要再次循环了
                                    shopMemberLevelBuyerMap = new HashMap<>();
                                }
                                buyerMap.put(scene.getShopId(), shopMemberLevelBuyerMap);
                            }
                            if (buyerMap.get(scene.getShopId()).size() != 0) {
                                for (CereShopSceneMemberCoupon coupon:couponList) {
                                    Long memberLevelId = coupon.getMemberLevelId();
                                    List<CereBuyerUser> userList = shopMemberLevelBuyerMap.get(memberLevelId);
                                    //发放优惠券
                                    for (CereBuyerUser user:userList) {
                                        CouponParam couponParam = new CouponParam();
                                        couponParam.setCouponId(coupon.getCouponId());
                                        try {
                                            cereBuyerCouponService.takeCoupon(couponParam, user);
                                        } catch(CoBusinessException coBusinessException) {
                                            // 如果报的错是该优惠券已领取完，则可以退出循环
                                            if (CoReturnFormat.COUPON_RECEIVE_FINISH.equals(coBusinessException.getCode())) {
                                                log.warn("sceneRunnable holiday takeCoupon receive finish: shopId = {}, couponId = {}, buyerUserId = {}", scene.getShopId(), coupon.getCouponId(), user.getBuyerUserId());
                                                break;
                                            }
                                            // 如果报的错是该用户领取达到上限，只打印日志即可
                                            if (CoReturnFormat.COUPON_TAKE_UPPER_LIMIT.equals(coBusinessException.getCode())) {
                                                log.warn("sceneRunnable holiday takeCoupon take upper limit: shopId = {}, couponId = {}, buyerUserId = {}", scene.getShopId(), coupon.getCouponId(), user.getBuyerUserId());
                                            }
                                        } catch (Exception e) {
                                            log.error("sceneRunnable holiday takeCoupon failed: shopId = {}, couponId = {}, buyerUserId = {}", scene.getShopId(), coupon.getCouponId(), user.getBuyerUserId(), e);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            //查询进行中的生日营销
            List<CereShopScene> birthDaySceneList = cereShopSceneService.selectOnGoingBirthDayMarketing();
            if (CollectionUtils.isNotEmpty(birthDaySceneList)) {
                for (CereShopScene scene:holidaySceneList) {
                    List<CereShopSceneMemberCoupon> couponList = cereShopSceneMemberCouponService.selectBySceneId(scene.getSceneId());
                    if (CollectionUtils.isNotEmpty(couponList)) {
                        List<Long> couponIdList = couponList.stream().map(CereShopSceneMemberCoupon::getCouponId).distinct().collect(Collectors.toList());
                        List<CerePlatformActivity> platformCouponList = cerePlatformActivityService.findOnGoingCouponByBatchId(couponIdList);
                        if (CollectionUtils.isEmpty(platformCouponList)) {
                            continue;
                        }

                        //查询出商家的会员
                        Map<Long,List<CereBuyerUser>> shopMemberLevelBuyerMap = buyerMap.get(scene.getShopId());
                        if (shopMemberLevelBuyerMap == null) {
                            List<CereBuyerUser> buyerUserList = cereBusinessBuyerUserService.selectBuyerUserByShopId(scene.getShopId());
                            if (CollectionUtils.isNotEmpty(buyerUserList)) {
                                shopMemberLevelBuyerMap = buyerUserList.stream().collect(Collectors.groupingBy(CereBuyerUser::getMemberLevelId));
                            } else {
                                //没有商家会员，放入一个空的map，这样下次循环就不需要再次循环了
                                shopMemberLevelBuyerMap = new HashMap<>();
                            }
                            buyerMap.put(scene.getShopId(), shopMemberLevelBuyerMap);
                        }

                        if (buyerMap.get(scene.getShopId()).size() != 0) {
                            for (CereShopSceneMemberCoupon coupon:couponList) {
                                Long memberLevelId = coupon.getMemberLevelId();
                                List<CereBuyerUser> userList = shopMemberLevelBuyerMap.get(memberLevelId);
                                //发放优惠券
                                for (CereBuyerUser user:userList) {
                                    if (StringUtils.isBlank(user.getBirthday())) {
                                        continue;
                                    }
                                    boolean matched = SceneUtil.matchScene(scene.getSceneType(), scene.getSceneTimeType(), scene.getSceneTime(), user.getBirthday(), true);

                                    if (matched) {
                                        CouponParam couponParam = new CouponParam();
                                        couponParam.setCouponId(coupon.getCouponId());
                                        try {
                                            cereBuyerCouponService.takeCoupon(couponParam, user);
                                        } catch(CoBusinessException coBusinessException) {
                                            // 如果报的错是该优惠券已领取完，则可以退出循环
                                            if (CoReturnFormat.COUPON_RECEIVE_FINISH.equals(coBusinessException.getCode())) {
                                                log.warn("sceneRunnable birthday takeCoupon receive finish: shopId = {}, couponId = {}, buyerUserId = {}", scene.getShopId(), coupon.getCouponId(), user.getBuyerUserId());
                                                break;
                                            }
                                            // 如果报的错是该用户领取达到上限，只打印日志即可
                                            if (CoReturnFormat.COUPON_TAKE_UPPER_LIMIT.equals(coBusinessException.getCode())) {
                                                log.warn("sceneRunnable birthday takeCoupon take upper limit: shopId = {}, couponId = {}, buyerUserId = {}", scene.getShopId(), coupon.getCouponId(), user.getBuyerUserId());
                                            }
                                        } catch (Exception e) {
                                            log.error("sceneRunnable birthday takeCoupon failed: shopId = {}, couponId = {}, buyerUserId = {}", scene.getShopId(), coupon.getCouponId(), user.getBuyerUserId(), e);
                                        }
                                    }
                                }
                            }
                        }
                    }


                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

}
