/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.domain.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
 * 商品规格值表
 * </p>
 *
 * @author JustArgo
 * @since 2020-05-07
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@Builder
@ApiModel(value = "ProductAttrValueSaveDTO", description = "商品规格值表")
public class ProductAttrValueSaveDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    private Long id;

    /**
     * 对应属性的主键id
     */
    @ApiModelProperty(value = "对应属性的主键id")
    private Long attrId;
    /**
     * 属性值：红色
     */
    @ApiModelProperty(value = "属性值：红色")
    @Length(max = 50, message = "属性值：红色长度不能超过50")
    private String attrValue;
    /**
     * 图片的http访问路径
     */
    @ApiModelProperty(value = "图片的http访问路径")
    @Length(max = 500, message = "图片的http访问路径长度不能超过500")
    private String attrValueImg;
    /**
     * 排序值
     */
    @ApiModelProperty(value = "排序值")
    private Integer sortOrder;
    /**
     * 逻辑删除 1-删除 0-未删除
     */
    @ApiModelProperty(value = "逻辑删除 1-删除 0-未删除")
    private Integer isDelete;

    /**
     * 规格值的数据编码，新增时没有id，作为临时编号
     */
    @ApiModelProperty(value = "规格值的数据编码，新增时没有id，作为临时编号")
    private String attrValueCode;
}
