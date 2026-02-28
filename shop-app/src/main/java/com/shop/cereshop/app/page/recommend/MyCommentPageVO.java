package com.shop.cereshop.app.page.recommend;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 我的消息
 *
 * @author
 */
@Data
@ApiModel(value = "MyCommentPageVO", description = "我的消息")
public class MyCommentPageVO {
    /**
     * 种草评论id
     */
    @ApiModelProperty(value = "种草评论id")
    private Long recommendCommentId;
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
     * 种草文件地址
     */
    @ApiModelProperty(value = "种草文件地址")
    private String fileUrl;
    /**
     * 评论用户id
     */
    @ApiModelProperty(value = "评论用户id")
    private Long userId;
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
     * 我的头像
     */
    @ApiModelProperty(value = "我的头像")
    private String myAvatar;
    /**
     * 评论内容
     */
    @ApiModelProperty(value = "评论内容")
    private String content;

    /**
     * 评论时间
     */
    @ApiModelProperty(value = "评论时间")
    private String createTime;
}
