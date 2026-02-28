package com.shop.cereshop.app.controller.index;

import com.shop.cereshop.app.annotation.NoRepeatSubmit;
import com.shop.cereshop.app.page.login.BuyerUser;
import com.shop.cereshop.app.param.index.LoginParam;
import com.shop.cereshop.commons.exception.CoBusinessException;
import com.shop.cereshop.commons.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 建行生活账号登录请求
 *
 * @author <a href="mailto:xieb@ijia120.com">bintse</a>
 * @version 1.0
 * @since 2026/02/14 10:51
 **/
@RestController
@RequestMapping("ccblife")
@Slf4j(topic = "CCBLoginController")
@Api(value = "建行生活H5客户登录模块", tags = "建行生活H5客户登录模块")
public class CcbLoginController {

    /**
     * 手机号验证码登录
     * @param param 封装json对象
     * @return
     */
    @PostMapping("login")
    @NoRepeatSubmit
    @ApiOperation(value = "手机号验证码登录")
    public Result<BuyerUser> login(@RequestBody LoginParam param) throws CoBusinessException {
        return new Result<BuyerUser>();
    }
}
