package com.shop.cereshop.app.controller.live;

import com.shop.cereshop.app.service.live.CereLiveSubscribeService;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.domain.live.CereLive;
import com.shop.cereshop.commons.exception.CoBusinessException;
import com.shop.cereshop.commons.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 画布选择商品查询
 */
@RestController
@RequestMapping("live")
@Slf4j(topic = "LiveController")
@Api(value = "直播控制器", tags = "直播控制器")
public class LiveController {

    @Autowired
    private CereLiveSubscribeService cereLiveSubscribeService;

    /**
     * 订阅直播
     * @return
     */
    @PostMapping("subscribeLive")
    @ApiOperation(value = "订阅直播")
    public Result<Boolean> subscribeLive(@RequestBody CereLive param, HttpServletRequest request) throws CoBusinessException {
        CereBuyerUser user = (CereBuyerUser) request.getAttribute("user");
        boolean result = cereLiveSubscribeService.subscribe(user.getBuyerUserId(), param.getId());
        return new Result<>(result);
    }

}
