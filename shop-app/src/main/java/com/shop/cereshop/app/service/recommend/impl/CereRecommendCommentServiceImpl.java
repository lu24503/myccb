package com.shop.cereshop.app.service.recommend.impl;

import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shop.cereshop.app.dao.recommend.CereRecommendCommentDAO;
import com.shop.cereshop.app.dao.recommend.CereRecommendTrendsDAO;
import com.shop.cereshop.app.page.recommend.CereRecommendCommentPageVO;
import com.shop.cereshop.app.page.recommend.CereRecommendTrendDetailVO;
import com.shop.cereshop.app.page.recommend.MyCommentPageVO;
import com.shop.cereshop.app.param.recommend.MyCommentPageParam;
import com.shop.cereshop.app.param.recommend.RecommendCommentPageParam;
import com.shop.cereshop.app.param.recommend.RecommendCommentSaveParam;
import com.shop.cereshop.app.service.recommend.CereRecommendCommentService;
import com.shop.cereshop.commons.domain.common.Page;
import com.shop.cereshop.commons.domain.recommend.CereRecommendComment;
import com.shop.cereshop.commons.domain.recommend.CereRecommendTrends;
import com.shop.cereshop.commons.exception.CoBusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CereRecommendCommentServiceImpl implements CereRecommendCommentService {

    @Autowired
    private CereRecommendCommentDAO cereRecommendCommentDAO;

    @Autowired
    private CereRecommendTrendsDAO cereRecommendTrendsDAO;

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = {CoBusinessException.class, Exception.class})
    public CereRecommendCommentPageVO saveComment(RecommendCommentSaveParam param) {
        CereRecommendComment saveComment = new CereRecommendComment();
        saveComment.setRecommendId(param.getRecommendId());
        saveComment.setContent(param.getContent());
        saveComment.setUserId(param.getUserId());
        saveComment.setUserType(1);
        if (ObjectUtil.isNotNull(param.getRecommendCommentId())) {
            CereRecommendComment parentComment = cereRecommendCommentDAO.getCommentById(param.getRecommendCommentId());
            if (ObjectUtil.isNotNull(parentComment)) {
                saveComment.setParentCommentId(param.getRecommendCommentId());
                saveComment.setRootCommentId(ObjectUtil.isNotNull(parentComment.getRootCommentId()) ?
                        parentComment.getRootCommentId() : param.getRecommendCommentId());
                saveComment.setTargetUserId(parentComment.getUserId());
                saveComment.setTargetUserType(parentComment.getUserType());
                if (ObjectUtil.isNotNull(parentComment.getRootCommentId())) {
                    // 增加根评论回复数
                    cereRecommendCommentDAO.addReplyCount(saveComment.getRootCommentId());
                }
                // 增加回复数
                cereRecommendCommentDAO.addReplyCount(param.getRecommendCommentId());
            }
        }
        cereRecommendCommentDAO.saveComment(saveComment);
        cereRecommendTrendsDAO.addCommentCount(param.getRecommendId());
        return cereRecommendCommentDAO.getCommentPageInfo(saveComment.getRecommendCommentId());
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = {CoBusinessException.class, Exception.class})
    public void deleteComment(Long recommendCommentId) {
        CereRecommendComment comment = cereRecommendCommentDAO.getCommentById(recommendCommentId);
        CereRecommendTrendDetailVO trends = cereRecommendTrendsDAO.getTrendById(comment.getRecommendId());
        Integer count;
        // 当前评论为根评论，删除包含自己的下级所有评论
        if (ObjectUtil.isNull(comment.getRootCommentId())) {
            count = cereRecommendCommentDAO.deleteCommentByRootCommentId(comment.getRecommendCommentId());
        } else {
            count = cereRecommendCommentDAO.deleteCommentByParentCommentId(comment.getRecommendCommentId());
            // 减少根评论回复数
            cereRecommendCommentDAO.subReplyCount(comment.getRootCommentId(), count);
            if (!comment.getRootCommentId().equals(comment.getParentCommentId())) {
                // 减少父评论回复数
                cereRecommendCommentDAO.subReplyCount(comment.getParentCommentId(), 1);
            }
        }
        cereRecommendTrendsDAO.subCommentCount(comment.getRecommendId(), trends.getCommentCount() - count >= 0 ? count : 0);
    }

    @Override
    public Page<CereRecommendCommentPageVO> commentPage(RecommendCommentPageParam param) {
        PageHelper.startPage(param.getPage(), param.getPageSize());
        PageInfo<CereRecommendCommentPageVO> pageInfo = new PageInfo<>(cereRecommendCommentDAO.commentPage(param));
        Page<CereRecommendCommentPageVO> page = new Page(pageInfo.getList(), pageInfo.getTotal());
        return page;
    }

    @Override
    public CereRecommendCommentPageVO commentTop(RecommendCommentPageParam param) {
        CereRecommendCommentPageVO vo = cereRecommendCommentDAO.getCommentPageInfo(param.getReplyCommentId());
        if ((ObjectUtil.isNull(vo.getParentCommentId()) && ObjectUtil.isNull(param.getRecommendCommentId())) ||
                (ObjectUtil.isNotNull(vo.getParentCommentId()) && ObjectUtil.isNotNull(param.getRecommendCommentId()))) {
            return vo;
        }
        return null;
    }

    @Override
    public Integer getUnreadCount(Long userId) {
        return cereRecommendCommentDAO.getUnreadCount(userId);
    }

    @Override
    public Page<MyCommentPageVO> myMessage(MyCommentPageParam param) {
        PageHelper.startPage(param.getPage(), param.getPageSize());
        PageInfo<MyCommentPageVO> pageInfo = new PageInfo<>(cereRecommendCommentDAO.myMessage(param));
        Page<MyCommentPageVO> page = new Page(pageInfo.getList(), pageInfo.getTotal());
        cereRecommendCommentDAO.read(param.getUserId());
        return page;
    }

    @Override
    public Boolean getCommentStatus(Long recommendCommentId) {
        CereRecommendComment comment = cereRecommendCommentDAO.getCommentById(recommendCommentId);
        boolean status = ObjectUtil.isNotNull(comment);
        if (!status) {
            return status;
        }
        CereRecommendTrendDetailVO trends = cereRecommendTrendsDAO.getTrendById(comment.getRecommendId());
        status = ObjectUtil.isNotNull(trends);
        return status;
    }
}
