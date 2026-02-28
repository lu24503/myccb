package com.shop.cereshop.app.service.live.impl;

import cn.binarywang.wx.miniapp.bean.live.WxMaLiveResult;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shop.cereshop.app.dao.live.CereLiveDAO;
import com.shop.cereshop.app.dao.live.CereLiveSubscribeDAO;
import com.shop.cereshop.app.page.live.CereLivePage;
import com.shop.cereshop.app.service.live.CereLiveService;
import com.shop.cereshop.commons.constant.IntegerEnum;
import com.shop.cereshop.commons.domain.common.Page;
import com.shop.cereshop.commons.domain.common.PageParam;
import com.shop.cereshop.commons.domain.live.CereLiveSubscribe;
import com.shop.cereshop.commons.utils.TimeUtils;
import com.shop.cereshop.commons.utils.third.WxLiveUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CereLiveServiceImpl implements CereLiveService {

    @Autowired
    private CereLiveDAO cereLiveDAO;

    @Autowired
    private WxLiveUtil wxLiveUtil;

    @Autowired
    private CereLiveSubscribeDAO cereLiveSubscribeDAO;

    @Override
    public Page<CereLivePage> selectLiveList(Long buyerUserId, PageParam param) {
        PageHelper.startPage(param.getPage(),param.getPageSize());
        List<CereLivePage> list = cereLiveDAO.selectLiveList(TimeUtils.yyMMddHHmmss());
        List<Integer> roomIdList = list.stream().filter(o->o.getRoomId() != null).map(CereLivePage::getRoomId).collect(Collectors.toList());
        List<Long> liveIdList = list.stream().map(CereLivePage::getId).collect(Collectors.toList());
        Map<Long, CereLiveSubscribe> subscribeMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(liveIdList) && buyerUserId != null) {
            LambdaQueryWrapper<CereLiveSubscribe> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(CereLiveSubscribe::getBuyerUserId, buyerUserId);
            wrapper.in(CereLiveSubscribe::getLiveId, liveIdList);
            List<CereLiveSubscribe> subscribeList = cereLiveSubscribeDAO.selectList(wrapper);
            subscribeMap = subscribeList.stream().collect(Collectors.toMap(CereLiveSubscribe::getLiveId, Function.identity()));
        }
        Map<Integer, WxMaLiveResult.RoomInfo> liveInfoMap = wxLiveUtil.queryLiveInfos(roomIdList);
        for (CereLivePage live:list) {
            if (live.getRoomId() != null) {
                WxMaLiveResult.RoomInfo roomInfo = liveInfoMap.get(live.getRoomId());
                live.setLiveStatus(roomInfo.getLiveStatus());
                live.setGoods(roomInfo.getGoods());
            }
            if (subscribeMap.get(live.getId()) != null) {
                live.setSubscribeStatus(IntegerEnum.YES.getCode());
            }
        }
        PageInfo<CereLivePage> pageInfo=new PageInfo<>(list);
        Page<CereLivePage> page=new Page(pageInfo.getList(),pageInfo.getTotal());
        return page;
    }
}
