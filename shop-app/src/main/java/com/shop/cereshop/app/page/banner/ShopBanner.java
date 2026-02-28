/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.page.banner;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 店铺banner
 */
@Data
@ApiModel(value = "ShopBanner", description = "店铺banner")
public class ShopBanner {

    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    private Long shopId;

    /**
     * 样式   1-留边  2-填充
     */
    @ApiModelProperty(value = "样式   1-留边  2-填充")
    private Integer bannerStyle;

    /**
     * 图片地址
     */
    @ApiModelProperty(value = "图片地址")
    private String bannerImage;

    /**
     * 图片数组
     */
    @ApiModelProperty(value = "图片数组")
    private List<String> images;

    /**
     * 跳转地址
     */
    @ApiModelProperty(value = "跳转地址")
    private String bannerUrl;
}
