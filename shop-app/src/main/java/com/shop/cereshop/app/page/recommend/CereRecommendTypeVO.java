package com.shop.cereshop.app.page.recommend;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 种草类型分页数据
 *
 * @author
 */
@Data
@ApiModel(value = "CereRecommendTypePage", description = "种草类型分页数据")
public class CereRecommendTypeVO {
    /**
     * 种草类型id
     */
    @ApiModelProperty(value = "种草类型id")
    private Long recommendTypeId;
    /**
     * 类型名称
     */
    @ApiModelProperty(value = "类型名称")
    private String name;
    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Integer sort;

}
