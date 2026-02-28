package com.shop.cereshop.app.page.recommend;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 店铺信息
 *
 * @author
 */
@Data
@ApiModel(value = "BusinessShopInfo", description = "店铺信息")
public class BusinessShopInfo {
    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    private Long shopId;
    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String name;
    /**
     * 头像
     */
    @ApiModelProperty(value = "头像")
    private String avatar;
    /**
     * 是否关注
     */
    @ApiModelProperty(value = "是否关注：0、未关注，1、已关注")
    private Integer isCollect;

}
