package com.shop.cereshop.app.controller.advert;

import com.shop.cereshop.app.annotation.NoRepeatSubmit;
import com.shop.cereshop.app.annotation.NoRepeatWebLog;
import com.shop.cereshop.app.param.advert.PopupAdvertParam;
import com.shop.cereshop.app.redis.service.api.UserRedisService;
import com.shop.cereshop.app.service.advert.CerePopupAdvertService;
import com.shop.cereshop.commons.domain.advert.CereCloseAdvert;
import com.shop.cereshop.commons.domain.advert.CerePopupAdvert;
import com.shop.cereshop.commons.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 广告模块
 */
@RestController
@RequestMapping("advert")
/**
 * 注解方式生成日志对象，指定topic生成对象类名
 */
@Slf4j(topic = "AdvertController")
@Api(value = "广告模块", tags = "广告模块")
public class AdvertController {

    @Autowired
    private CerePopupAdvertService cerePopupAdvertService;

    @Autowired
    private UserRedisService userRedisService;

    /**
     * 根据触发条件查询广告
     * @param param
     * @return
     */
    @PostMapping("selectByCondition")
    @NoRepeatSubmit
    @ApiOperation(value = "根据触发条件查询广告")
    @NoRepeatWebLog
    public Result<List<PopupAdvertParam>> selectByCondition(@RequestBody CerePopupAdvert param, HttpServletRequest request) {
        Long buyerUserId = null;
        String token = request.getHeader("Authorization");
        if (StringUtils.isNotEmpty(token)) {
            buyerUserId = userRedisService.getBuyerUserIdByToken(token);
        }
        List<PopupAdvertParam> list = cerePopupAdvertService.selectByCondition(buyerUserId, param.getTriggerCondition());
        return new Result<>(list);
    }

    /**
     * 关闭广告
     * @param param
     * @return
     */
    @PostMapping("closeAdvert")
    @NoRepeatSubmit
    @ApiOperation(value = "关闭广告")
    @NoRepeatWebLog
    public Result<Integer> closeAdvert(@RequestBody CereCloseAdvert param, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (StringUtils.isNotEmpty(token)) {
            Long buyerUserId = userRedisService.getBuyerUserIdByToken(token);
            if (buyerUserId != null) {
                param.setBuyerUserId(buyerUserId);
            }
        }
        Integer result = cerePopupAdvertService.closeAdvert(param);
        return new Result(result);
    }

}
