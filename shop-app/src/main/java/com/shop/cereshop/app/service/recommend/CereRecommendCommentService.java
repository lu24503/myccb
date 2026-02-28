package com.shop.cereshop.app.service.recommend;


import com.shop.cereshop.app.page.recommend.CereRecommendCommentPageVO;
import com.shop.cereshop.app.page.recommend.MyCommentPageVO;
import com.shop.cereshop.app.param.recommend.MyCommentPageParam;
import com.shop.cereshop.app.param.recommend.RecommendCommentPageParam;
import com.shop.cereshop.app.param.recommend.RecommendCommentSaveParam;
import com.shop.cereshop.commons.domain.common.Page;

public interface CereRecommendCommentService {

    CereRecommendCommentPageVO saveComment(RecommendCommentSaveParam param);

    void deleteComment(Long recommendCommentId);

    Page<CereRecommendCommentPageVO> commentPage(RecommendCommentPageParam param);

    CereRecommendCommentPageVO commentTop(RecommendCommentPageParam param);

    Integer getUnreadCount(Long userId);

    Page<MyCommentPageVO> myMessage(MyCommentPageParam param);

    Boolean getCommentStatus(Long recommendCommentId);

}
