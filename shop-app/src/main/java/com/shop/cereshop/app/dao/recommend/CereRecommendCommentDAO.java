/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.dao.recommend;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shop.cereshop.app.page.recommend.CereRecommendCommentPageVO;
import com.shop.cereshop.app.page.recommend.MyCommentPageVO;
import com.shop.cereshop.app.param.recommend.MyCommentPageParam;
import com.shop.cereshop.app.param.recommend.RecommendCommentPageParam;
import com.shop.cereshop.commons.domain.recommend.CereRecommendComment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CereRecommendCommentDAO extends BaseMapper<CereRecommendComment> {

    List<CereRecommendCommentPageVO> commentPage(RecommendCommentPageParam param);

    CereRecommendCommentPageVO getCommentPageInfo(@Param("recommendCommentId") Long recommendCommentId);

    CereRecommendComment getCommentById(@Param("recommendCommentId") Long recommendCommentId);

    void addReplyCount(@Param("recommendCommentId") Long recommendCommentId);

    void subReplyCount(@Param("recommendCommentId") Long recommendCommentId, @Param("count") Integer count);

    void saveComment(CereRecommendComment comment);

    void deleteComment(@Param("recommendCommentId") Long recommendCommentId);

    Integer deleteCommentByRootCommentId(@Param("rootCommentId") Long rootCommentId);

    Integer deleteCommentByParentCommentId(@Param("parentCommentId") Long parentCommentId);

    Integer getUnreadCount(@Param("userId") Long userId);

    void read(@Param("userId") Long userId);

    List<MyCommentPageVO> myMessage(MyCommentPageParam param);
}
