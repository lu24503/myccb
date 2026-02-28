/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.live.impl;


import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaSubscribeMessage;
import cn.binarywang.wx.miniapp.constant.WxMaConstants;
import com.alibaba.fastjson.JSON;
import com.shop.cereshop.app.config.WxProperties;
import com.shop.cereshop.app.dao.buyer.CereBuyerUserDAO;
import com.shop.cereshop.app.dao.live.CereLiveDAO;
import com.shop.cereshop.app.dao.live.CereLiveSubscribeDAO;
import com.shop.cereshop.app.redis.service.api.StringRedisService;
import com.shop.cereshop.app.service.live.CereLiveSubscribeService;
import com.shop.cereshop.commons.constant.StringEnum;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.domain.live.CereLive;
import com.shop.cereshop.commons.domain.live.CereLiveSubscribe;
import com.shop.cereshop.commons.utils.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxError;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 业务实现类
 * 直播订阅表
 * </p>
 *
 * @author JustArgo
 * @date 2021-12-25
 */
@Slf4j
@Service
public class CereLiveSubscribeServiceImpl implements CereLiveSubscribeService {

    @Autowired
    private CereLiveSubscribeDAO cereLiveSubscribeDAO;

    @Autowired
    private CereLiveDAO cereLiveDAO;

    @Autowired
    private StringRedisService stringRedisService;

    @Autowired
    private WxMaService wxMaService;

    @Autowired
    private CereBuyerUserDAO cereBuyerUserDAO;

    @Autowired
    private WxProperties wxProperties;

    @Override
    public boolean subscribe(Long buyerUserId, Long liveId) {
        CereLive cereLive = cereLiveDAO.selectById(liveId);
        String now = TimeUtils.yyMMddHHmmss();
        if (cereLive != null) {
            CereLiveSubscribe subscribe = new CereLiveSubscribe();
            subscribe.setBuyerUserId(buyerUserId);
            subscribe.setLiveId(liveId);
            subscribe.setCreateTime(now);
            subscribe.setUpdateTime(now);
            cereLiveSubscribeDAO.insert(subscribe);

            if (cereLive.getStartTime().compareTo(now) > 0) {
                try {
                    //新增延时任务,开播时间到发送消息提醒
                    stringRedisService.set(StringEnum.LIVE_START_NOTICE.getCode() + "-" + subscribe.getId(), 1,
                            TimeUtils.getCountDownByTime(now, cereLive.getStartTime()));
                    return true;
                } catch (Exception e) {
                    log.error("subscribe fail: buyerUserId = {}, liveId = {}" + e.getMessage(), buyerUserId, liveId, e);
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public void sendNotice(Long id) {
        log.info("sendNotice: {}", id);
        CereLiveSubscribe subscribe = cereLiveSubscribeDAO.selectById(id);
        log.info("subscribe: {}", JSON.toJSONString(subscribe));
        if (subscribe != null) {
            CereBuyerUser buyerUser = cereBuyerUserDAO.selectById(subscribe.getBuyerUserId());
            CereLive live = cereLiveDAO.selectById(subscribe.getLiveId());
            if (buyerUser != null && StringUtils.isNotBlank(buyerUser.getWechatOpenId()) && live.getRoomId() != null) {
                WxMaSubscribeMessage message = new WxMaSubscribeMessage();
                message.setTemplateId(wxProperties.getLiveTemplateId());
                message.setToUser(buyerUser.getWechatOpenId());
                message.setLang(WxMaConstants.MiniProgramLang.ZH_CN);
                message.setPage("plugin-private://" + wxProperties.getLiveAppId() + "/pages/live-player-plugin?room_id=" + live.getRoomId());
                message.setMiniprogramState(WxMaConstants.MiniProgramState.TRIAL);
                message.addData(new WxMaSubscribeMessage.MsgData("thing4", live.getTitle()));
                message.addData(new WxMaSubscribeMessage.MsgData("time2", live.getStartTime()));
                message.addData(new WxMaSubscribeMessage.MsgData("thing3", live.getAnchorNickname()));
                try {
                    wxMaService.getMsgService().sendSubscribeMsg(message);
                } catch (WxErrorException e) {
                    log.error("sendNotice wxErrorException: " + e.getMessage(), e);
                    WxError error = e.getError();
                    if (error != null) {
                        log.error("sendNotice: errorCode: {}, errorMsg: {}, json: {}", error.getErrorCode(), error.getErrorMsg(), error.getJson());
                    } else {
                        log.error("sendNotice: " + e.getLocalizedMessage(), e);
                    }
                } catch (Exception e) {
                    log.error("sendNotice exception:" + e.getMessage(), e);
                }
            }
        }
    }
}
