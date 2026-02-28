/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.page.order;

import lombok.Data;

/**
 * 验证分销员数据实体
 */
@Data
public class ShopDistributor {

    /**
     * 分销员id
     */
    private Long distributorId;

    /**
     * 是否自购分佣
     */
    private Integer ifSelf;

    /**
     * 直接分佣比例
     */
    private Integer directProportion;

    /**
     * 间接分佣比例
     */
    private Integer indirectProportion;

    /**
     * 上级分销员id
     */
    private Long invitees;

    /**
     * 分销员名称
     */
    private String distributorName;

    /**
     * 分销员手机号
     */
    private String distributorPhone;

    /**
     * 上级分销员名称
     */
    private String InviteesName;

    /**
     * 上级分销员手机号
     */
    private String InviteesPhone;
}
