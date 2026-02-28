package com.shop.cereshop.app.controller.kf;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.shop.cereshop.app.dao.customer_service.CereShopCustomerServiceDAO;
import com.shop.cereshop.app.param.common.CommonIdParam;
import com.shop.cereshop.commons.domain.customer_service.ShopCustomerService;
import com.shop.cereshop.commons.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.cp.api.WxCpKfService;
import me.chanjar.weixin.cp.api.impl.WxCpServiceImpl;
import me.chanjar.weixin.cp.bean.kf.WxCpKfAccountLink;
import me.chanjar.weixin.cp.bean.kf.WxCpKfAccountLinkResp;
import me.chanjar.weixin.cp.bean.kf.WxCpKfAccountListResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 客服模块
 */
@RestController
@RequestMapping("kf")
/**
 * 注解方式生成日志对象，指定topic生成对象类名
 */
@Slf4j(topic = "KfController")
@Api(value = "客服模块", tags = "客服模块")
public class KfController {

    @Autowired
    private WxCpServiceImpl wxCpServiceImpl;

    @Autowired
    private CereShopCustomerServiceDAO cereShopCustomerServiceDAO;

    @GetMapping("getH5Kf")
    @ApiOperation(value = "查询H5客服", notes = "id参数传店铺id, 不传则返回平台端的客服")
    public Result<Map<String, String>> getH5Kf(CommonIdParam param) {
        Map<String, String> map = new HashMap<>();
        map.put("url", "");
        try {
            map = getKfInner(param.getId());
        } catch (Exception e) {
            log.error("getH5Kf fail: " + e.getMessage(), e);
        }
        map.remove("corpId");
        return new Result<>(map);
    }

    @GetMapping("getPCKf")
    @ApiOperation(value = "查询PC客服", notes = "id参数传店铺id, 不传则返回平台端的客服")
    public Result<Map<String, String>> getPCKf(CommonIdParam param) {
        Map<String, String> map = new HashMap<>();
        map.put("url", "");
        try {
            map = getKfInner(param.getId());
        } catch (Exception e) {
            log.error("getPCKf fail: " + e.getMessage(), e);
        }
        map.remove("corpId");
        return new Result<>(map);
    }

    @GetMapping("getAppletKf")
    @ApiOperation(value = "查询小程序客服", notes = "id参数传店铺id, 不传则返回平台端的客服, 返回map key为corpId 和 url")
    public Result<Map<String, String>> getAppletKf(CommonIdParam param) {
        Map<String, String> map = new HashMap<>();
        map.put("url", "");
        log.info("getAppletKf {}", JSON.toJSONString(param));
        try {
            map = getKfInner(param.getId());
        } catch (Exception e) {
            log.error("getAppletKf fail: " + e.getMessage(), e);
        }
        log.info("getAppletKf map: {}", JSON.toJSONString(map));
        return new Result<>(map);
    }

    private Map<String, String> getKfInner(Long id) {
        Map<String, String> map = new HashMap<>();
        try {
            WxCpKfService kfService = wxCpServiceImpl.getKfService();
            if (id == null) {
                List<ShopCustomerService> serviceList = cereShopCustomerServiceDAO.selectList(Wrappers.emptyWrapper());
                List<String> openKfIdList = serviceList.stream().map(ShopCustomerService::getOpenKfid).collect(Collectors.toList());
                //这里直接去100个，如果100个中都没有一个正常的，则视为账号不正常
                WxCpKfAccountListResp listResp = kfService.listAccount(0, 100);
                if (listResp != null && listResp.getErrcode() == 0) {
                    List<WxCpKfAccountListResp.AccountListDTO> accountList = listResp.getAccountList();
                    Collections.shuffle(accountList);
                    for (WxCpKfAccountListResp.AccountListDTO dto:accountList) {
                        if (!openKfIdList.contains(dto.getOpenKfid())) {
                            String openKfId = dto.getOpenKfid();
                            getLinkByOpenKfIdO(kfService, map, openKfId);
                            return map;
                        }
                    }
                }
            } else {
                List<ShopCustomerService> serviceList = cereShopCustomerServiceDAO.selectList(Wrappers.<ShopCustomerService>lambdaQuery()
                        .eq(ShopCustomerService::getShopId, id));
                if (CollectionUtil.isNotEmpty(serviceList)) {
                    String openKfId = serviceList.get(0).getOpenKfid();
                    getLinkByOpenKfIdO(kfService, map, openKfId);
                    return map;
                }
            }
        } catch (Exception e) {
            log.error("getKfInner fail: " + e.getMessage(), e);
        }
        return map;
    }

    private void getLinkByOpenKfIdO(WxCpKfService kfService, Map<String, String> map, String openKfId) throws Exception {
        WxCpKfAccountLink link = new WxCpKfAccountLink();
        link.setOpenKfid(openKfId);
        WxCpKfAccountLinkResp linkResp = kfService.getAccountLink(link);
        if (linkResp != null && linkResp.getErrcode() == 0L) {
            map.put("corpId", wxCpServiceImpl.getWxCpConfigStorage().getCorpId());
            map.put("url", linkResp.getUrl());
            log.info("getAppletKf {}", JSON.toJSONString(map));
        }
    }
}
