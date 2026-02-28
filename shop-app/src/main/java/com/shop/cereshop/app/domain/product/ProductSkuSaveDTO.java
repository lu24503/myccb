/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.domain.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import lombok.Data;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 实体类
 * 商品的sku
 * </p>
 *
 * @author JustArgo
 * @since 2020-05-16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@Builder
@ApiModel(value = "ProductSkuSaveDTO", description = "商品的sku")
public class ProductSkuSaveDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    private Long id;

    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    @NotNull(message = "店铺id不能为空")
    private Long storeId;
    /**
     * sku编码
     */
    @ApiModelProperty(value = "sku编码")
    @Length(max = 255, message = "sku编码长度不能超过255")
    private String skuCode;
    /**
     * sku的规格值组合，例如：红色 43码
     */
    @ApiModelProperty(value = "sku的规格值组合，例如：红色 43码")
    @Length(max = 255, message = "sku的规格值组合，例如：红色 43码长度不能超过255")
    private String skuNameStr;
    /**
     * 商品id
     */
    @ApiModelProperty(value = "商品id")
    private Long productId;
    /**
     * 销售价
     */
    @ApiModelProperty(value = "销售价")
    private Long skuPrice;
    /**
     * 采购价
     */
    @ApiModelProperty(value = "采购价")
    private Long applyPrice;
    /**
     * sku的图片
     */
    @ApiModelProperty(value = "sku的图片")
    @Length(max = 200, message = "sku的图片长度不能超过200")
    private String skuImg;
    /**
     * sku的库存
     */
    @ApiModelProperty(value = "sku的库存")
    private Integer skuStock;
    /**
     * 重量，单位(千克)
     */
    @ApiModelProperty(value = "重量，单位(千克)")
    private Double weight;
    /**
     * 逻辑删除 1-删除 0-未删除
     */
    @ApiModelProperty(value = "逻辑删除 1-删除 0-未删除")
    @NotNull(message = "逻辑删除 1-删除 0-未删除不能为空")
    private Integer isDelete;

    @ApiModelProperty(value = "sku对应的规格编码")
    private List<SkuAttrCodeDTO> skuAttrCodeDTOList;

    @ApiModelProperty(value = "sku对应的规格列表，编辑商品时使用")
    private List<ProductSkuAttr> skuAttrList;
}
