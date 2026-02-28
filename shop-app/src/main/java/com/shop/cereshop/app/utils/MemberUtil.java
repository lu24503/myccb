/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.utils;

import com.shop.cereshop.app.page.login.BuyerUser;
import com.shop.cereshop.app.service.member.CerePlatformMemberLevelService;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.domain.member.CerePlatformMemberLevel;
import com.shop.cereshop.commons.utils.SpringUtil;

public class MemberUtil {

    public static void setMemberInfo(CereBuyerUser cereBuyerUser) {
        if (cereBuyerUser != null) {
            CerePlatformMemberLevelService service = SpringUtil.getBean(CerePlatformMemberLevelService.class);
            if (cereBuyerUser.getMemberLevelId() != null) {
                CerePlatformMemberLevel level = service.selectByMemberLevelId(cereBuyerUser.getMemberLevelId());
                CerePlatformMemberLevel nextLevel = service.selectNextLevel(cereBuyerUser.getMemberLevelId());
                if (level != null) {
                    cereBuyerUser.setMemberLevelName(level.getMemberLevelName());
                }
                if (nextLevel != null) {
                    cereBuyerUser.setNextLevelGrowth(nextLevel.getGrowth());
                    cereBuyerUser.setNextLevelName(nextLevel.getMemberLevelName());
                }
            } else {
                CerePlatformMemberLevel level = service.selectFirstLevel();
                if (level != null) {
                    cereBuyerUser.setMemberLevelId(level.getMemberLevelId());
                    cereBuyerUser.setMemberLevelName(level.getMemberLevelName());
                    cereBuyerUser.setGrowth(0);

                    CerePlatformMemberLevel nextLevel = service.selectNextLevel(cereBuyerUser.getMemberLevelId());
                    if (nextLevel != null) {
                        cereBuyerUser.setNextLevelGrowth(nextLevel.getGrowth());
                        cereBuyerUser.setNextLevelName(nextLevel.getMemberLevelName());
                    }
                }
            }
        }
    }

    public static void setMemberInfo(BuyerUser buyerUser) {
        if (buyerUser != null) {
            CerePlatformMemberLevelService service = SpringUtil.getBean(CerePlatformMemberLevelService.class);
            if (buyerUser.getMemberLevelId() != null) {
                CerePlatformMemberLevel level = service.selectByMemberLevelId(buyerUser.getMemberLevelId());
                CerePlatformMemberLevel nextLevel = service.selectNextLevel(buyerUser.getMemberLevelId());
                if (level != null) {
                    buyerUser.setMemberLevelName(level.getMemberLevelName());
                }
                if (nextLevel != null) {
                    buyerUser.setNextLevelGrowth(nextLevel.getGrowth());
                    buyerUser.setNextLevelName(nextLevel.getMemberLevelName());
                }
            } else {
                CerePlatformMemberLevel level = service.selectFirstLevel();
                if (level != null) {
                    buyerUser.setMemberLevelId(level.getMemberLevelId());
                    buyerUser.setMemberLevelName(level.getMemberLevelName());
                    buyerUser.setGrowth(0);

                    CerePlatformMemberLevel nextLevel = service.selectNextLevel(buyerUser.getMemberLevelId());
                    if (nextLevel != null) {
                        buyerUser.setNextLevelGrowth(nextLevel.getGrowth());
                        buyerUser.setNextLevelName(nextLevel.getMemberLevelName());
                    }
                }
            }
        }
    }

    /**
     * 更新会员等级和成长值信息
     * @param cereBuyerUser
     * @param growth
     */
    public static void updateMemberInfo(CereBuyerUser cereBuyerUser, Integer growth) {
        if (cereBuyerUser == null || growth == null || growth <= 0) {
            return;
        }
        CerePlatformMemberLevelService service = SpringUtil.getBean(CerePlatformMemberLevelService.class);
        if (cereBuyerUser.getMemberLevelId() == null) {
            CerePlatformMemberLevel firstLevel = service.selectFirstLevel();
            if (firstLevel == null) {
                cereBuyerUser.setMemberLevelId(0L);
                cereBuyerUser.setGrowth(growth);
            } else {
                cereBuyerUser.setMemberLevelId(firstLevel.getMemberLevelId());
                cereBuyerUser.setGrowth(growth);
            }
        }
        if (cereBuyerUser.getMemberLevelId() != null) {
            CerePlatformMemberLevel nextLevel = service.selectNextLevel(cereBuyerUser.getMemberLevelId());
            cereBuyerUser.setGrowth(cereBuyerUser.getGrowth() + growth);
            //当前用户的成长值，达到了下一个级别的成长值
            if (nextLevel != null && cereBuyerUser.getGrowth() >= nextLevel.getGrowth()) {
                cereBuyerUser.setMemberLevelId(nextLevel.getMemberLevelId());
            }
        }
    }

}
