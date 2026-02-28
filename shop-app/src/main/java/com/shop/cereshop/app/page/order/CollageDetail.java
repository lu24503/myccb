/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.page.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 拼团信息
 */
@Data
@ApiModel(value = "CollageDetail", description = "拼团信息")
public class CollageDetail {

    /**
     * 拼单id
     */
    @ApiModelProperty(value = "拼单id")
    private Long collageId;

    /**
     * 拼单状态 0-待成团，1-拼团成功，2-拼团失败
     */
    @ApiModelProperty(value = "拼单状态 0-待成团，1-拼团成功，2-拼团失败")
    private Integer state;

    /**
     * 成团人数
     */
    @ApiModelProperty(value = "成团人数")
    private Integer person;

    /**
     * 剩余拼团人数
     */
    @ApiModelProperty(value = "剩余拼团人数")
    private Integer people;

    /**
     * 成团倒计时
     */
    @ApiModelProperty(value = "成团倒计时")
    private long time;

    /**
     * 成团有效时间几（小时）
     */
    @ApiModelProperty(value = "成团有效时间几（小时）")
    private Integer effectiveTime;

    /**
     * 发起拼单时间
     */
    @ApiModelProperty(value = "发起拼单时间")
    private String createTime;

    /**
     * 参与拼团人员数据
     */
    @ApiModelProperty(value = "参与拼团人员数据")
    private List<CollagePerson> personList;
}
