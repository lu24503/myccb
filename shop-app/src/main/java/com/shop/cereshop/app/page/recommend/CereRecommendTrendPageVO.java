package com.shop.cereshop.app.page.recommend;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 种草分页数据
 *
 * @author
 */
@Data
@ApiModel(value = "CereRecommendTrendPageVO", description = "种草分页数据")
public class CereRecommendTrendPageVO {
    /**
     * 种草id
     */
    @ApiModelProperty(value = "种草id")
    private Long recommendId;
    /**
     * 文件类型
     */
    @ApiModelProperty(value = "文件类型")
    private Integer fileType;
    /**
     * 封面
     */
    @ApiModelProperty(value = "封面")
    private String cover;
    /**
     * 文件地址
     */
    @ApiModelProperty(value = "文件地址")
    private String fileUrl;
    /**
     * 文案
     */
    @ApiModelProperty(value = "文案")
    private String remark;
    /**
     * 点赞数
     */
    @ApiModelProperty(value = "点赞数")
    private Integer likeCount;
    /**
     * 点赞状态
     */
    @ApiModelProperty(value = "点赞状态：0-未点赞，1-已点赞")
    private Integer likeStatus;
    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    private Long shopId;
    /**
     * 店家名称
     */
    @ApiModelProperty(value = "店家名称")
    private String name;
    /**
     * 店家头像
     */
    @ApiModelProperty(value = "店家头像")
    private String avatar;
}
