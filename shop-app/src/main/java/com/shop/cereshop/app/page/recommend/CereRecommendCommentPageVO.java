package com.shop.cereshop.app.page.recommend;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 种草评论分页数据
 *
 * @author
 */
@Data
@ApiModel(value = "CereRecommendCommentPageVO", description = "种草评论分页数据")
public class CereRecommendCommentPageVO {
    /**
     * 种草评论id
     */
    @ApiModelProperty(value = "种草评论id")
    private Long recommendCommentId;
    /**
     * 父评论ID，若为一级评论则为NUL
     */
    @ApiModelProperty(value = "父评论ID，若为一级评论则为NUL")
    private Long parentCommentId;
    /**
     * 种草id
     */
    @ApiModelProperty(value = "种草id")
    private Long recommendId;
    /**
     * 评论用户id
     */
    @ApiModelProperty(value = "评论用户id")
    private Long userId;
    /**
     * 是否为自己的评论：0、否，1、是
     */
    @ApiModelProperty(value = "是否为自己的评论：0、否，1、是")
    private Integer isSelf;
    /**
     * 评论用户名称
     */
    @ApiModelProperty(value = "评论用户名称")
    private String userName;
    /**
     * 评论用户头像
     */
    @ApiModelProperty(value = "评论用户头像")
    private String avatar;
    /**
     * 目标评论用户id
     */
    @ApiModelProperty(value = "目标评论用户id")
    private Long targetUserId;
    /**
     * 目标评论用户名称
     */
    @ApiModelProperty(value = "目标评论用户名称")
    private String targetUserName;
    /**
     * 评论内容
     */
    @ApiModelProperty(value = "评论内容")
    private String content;
    /**
     * 回复数
     */
    @ApiModelProperty(value = "回复数")
    private Integer replyCount;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private String createTime;
}
