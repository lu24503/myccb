package com.shop.cereshop.app.page.recommend;

import com.shop.cereshop.app.page.product.RecommendProduct;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 种草明细数据
 *
 * @author
 */
@Data
@ApiModel(value = "CereRecommendTrendDetailVO", description = "种草明细数据")
public class CereRecommendTrendDetailVO {
    /**
     * 种草id
     */
    @ApiModelProperty(value = "种草id")
    private Long recommendId;
    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    private Long shopId;
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
     * 点赞状态：0、未点 1、已点
     */
    @ApiModelProperty(value = "点赞状态：0、未点 1、已点")
    private Integer likeStatus;
    /**
     * 评论数
     */
    @ApiModelProperty(value = "评论数")
    private Integer commentCount;
    /**
     * 分享数
     */
    @ApiModelProperty(value = "分享数")
    private Integer shareCount;
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
    /**
     * 是否关注
     */
    @ApiModelProperty(value = "是否关注：0、未关注，1、已关注")
    private Integer isCollect;
    /**
     * 发布时间
     */
    @ApiModelProperty(value = "发布时间")
    private String publishTime;
    /**
     * 发布天数
     */
    @ApiModelProperty(value = "发布天数")
    private String day;

    /**
     * 关联商品信息
     */
    @ApiModelProperty(value = "关联商品信息")
    private List<RecommendProduct> products;
}
