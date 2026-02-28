/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.domain.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 * 实体类
 * 存储规格和规格值的组合
 * </p>
 *
 * @author JustArgo
 * @since 2020-05-03
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "ProductAttrCombineSaveDTO", description = "存储规格和规格值的组合")
public class ProductAttrCombineSaveDTO extends ProductAttrSaveDTO {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id")
    private Long id;

    /**
     * 规格对应的规格值
     */
    @ApiModelProperty("规格对应的规格值")
    private List<ProductAttrValueSaveDTO> attrValueList;

}
