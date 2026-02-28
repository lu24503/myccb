package com.shop.cereshop.app.param.product;

import com.shop.cereshop.commons.constant.RefreshSkuRealInfoSourceEnum;
import lombok.Data;

@Data
public class RefreshRealInfoDTO {

    /**
     * 商品id
     */
    private Long productId;

    /**
     * skuId
     */
    private Long skuId;

    /**
     * 刷新操作来源
     */
    private RefreshSkuRealInfoSourceEnum sourceEnum;

    /**
     * 虚拟销量
     */
    private Integer fictitiousNumber;

    /**
     * 活动id
     */
    private Long activityId;

    /**
     * 校验码
     */
    private String verifyCode;
}
