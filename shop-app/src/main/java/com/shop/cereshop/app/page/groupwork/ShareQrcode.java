/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.page.groupwork;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 二维码
 */
@Data
@ApiModel(value = "ShareQrcode", description = "二维码")
public class ShareQrcode {

    /**
     * 小程序码图片地址
     */
    @ApiModelProperty(value = "小程序码图片地址")
    private String xcxQrcode;

    /**
     * 二维码图片地址
     */
    @ApiModelProperty(value = "二维码图片地址")
    private String qrcode;

    /**
     * 头像
     */
    private String headImage="https://guanjia-dev.oss-cn-shanghai.aliyuncs.com/idcard/2020-05-20/u=3247573607,3735002743&fm=26&gp=0.jpg";

    /**
     * 商品图片
     */
    private String image="https://cereshop.oss-cn-shenzhen.aliyuncs.com/test/2020-12-26/69fc5f67d914417aac3d70b2807ec4ac%E6%88%AA%E5%B1%8F2020-12-26%20%E4%B8%8B%E5%8D%884.09.38.png";
}
