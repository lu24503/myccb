/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.domain.product;


import com.shop.cereshop.app.domain.promotion.PromotionShowVO;
import com.shop.cereshop.app.domain.shop.StoreDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("商详对象")
@Data
public class ProductVO extends Product {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("商品图片列表")
    private List<String> imgs;

    @ApiModelProperty("邮费")
    private Long postFee;

    @ApiModelProperty
    private StoreDTO storeDTO;

    @ApiModelProperty("规格列表")
    private List<ProductAttrCombineSaveDTO> attrList;

    @ApiModelProperty("商品sku列表")
    private List<ProductSkuVO> productSkuVOList;

    @ApiModelProperty("发货地址")
    private String shipAddress;

    @ApiModelProperty("付款人数")
    private Integer buyCount;

    @ApiModelProperty("参加的活动列表")
    private List<PromotionShowVO> promotionShowVOList;

}
