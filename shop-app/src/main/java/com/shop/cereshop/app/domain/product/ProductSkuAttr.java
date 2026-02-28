/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.domain.product;

import com.shop.cereshop.app.domain.common.Entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;


/**
 * <p>
 * 实体类
 * sku的规格
 * </p>
 *
 * @author JustArgo
 * @since 2020-05-07
 */
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "ProductSkuAttr", description = "sku的规格")
@AllArgsConstructor
public class ProductSkuAttr extends Entity<Long> {

    private static final long serialVersionUID = 1L;

    /**
     * sku的id
     */
    @ApiModelProperty(value = "sku的id")
    private Long skuId;

    /**
     * 规格id
     */
    @ApiModelProperty(value = "规格id")
    private Long attrId;

    /**
     * 规格值id
     */
    @ApiModelProperty(value = "规格值id")
    private Long attrValueId;

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


    @Builder
    public ProductSkuAttr(Long id, Long createUser, LocalDateTime createTime, Long updateUser, LocalDateTime updateTime,
                    Long skuId, Long attrId, Long attrValueId, Integer sortOrder, Integer isDelete) {
        this.id = id;
        this.createUser = createUser;
        this.createTime = createTime;
        this.updateUser = updateUser;
        this.updateTime = updateTime;
        this.skuId = skuId;
        this.attrId = attrId;
        this.attrValueId = attrValueId;
        this.sortOrder = sortOrder;
        this.isDelete = isDelete;
    }

}
