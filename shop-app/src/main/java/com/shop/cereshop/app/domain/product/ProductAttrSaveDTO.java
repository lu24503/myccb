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

/**
 * <p>
 * 实体类
 * 商品规格表
 * </p>
 *
 * @author JustArgo
 * @since 2020-05-31
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@Builder
@ApiModel(value = "ProductAttrSaveDTO", description = "商品规格表")
public class ProductAttrSaveDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 商品id
     */
    @ApiModelProperty(value = "商品id")
    private Long productId;
    /**
     * 属性名称
     */
    @ApiModelProperty(value = "属性名称")
    @Length(max = 30, message = "属性名称长度不能超过30")
    private String attrName;
    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Integer sortOrder;
    /**
     * 是否需要图片
     */
    @ApiModelProperty(value = "是否需要图片")
    @NotNull(message = "是否需要图片不能为空")
    private Boolean needImg;
    /**
     * 逻辑删除 1-删除 0-未删除
     */
    @ApiModelProperty(value = "逻辑删除 1-删除 0-未删除")
    @NotNull(message = "逻辑删除 1-删除 0-未删除不能为空")
    private Integer isDelete;

    /**
     * 规格的数据编码，新增时没有id，作为临时编号
     */
    @ApiModelProperty(value = "规格的数据编码，新增时没有id，作为临时编号")
    private String attrCode;

}
