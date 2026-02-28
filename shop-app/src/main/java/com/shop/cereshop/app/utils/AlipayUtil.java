/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.utils;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayOpenAppQrcodeCreateModel;
import com.alipay.api.request.AlipayOpenAppQrcodeCreateRequest;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.request.AlipayUserInfoShareRequest;
import com.alipay.api.response.AlipayOpenAppQrcodeCreateResponse;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.alipay.api.response.AlipayUserInfoShareResponse;
import com.shop.cereshop.commons.config.AlipayConfig;
import com.shop.cereshop.commons.constant.CoReturnFormat;
import com.shop.cereshop.commons.exception.CoBusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j(topic = "AlipayUtil")
@Component
public class AlipayUtil {

    public static AlipayUserInfoShareResponse getBuyerId(String authCode) {
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.APPID, AlipayConfig.RSA_PRIVATE_KEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.SIGNTYPE);
        AlipaySystemOauthTokenRequest request = new AlipaySystemOauthTokenRequest();
        request.setCode(authCode);//这个就是第一步获取的auth_code
        request.setGrantType("authorization_code");//这个固定值,参考https://docs.open.alipay.com/api_9/alipay.system.oauth.token
        try {
            AlipaySystemOauthTokenResponse oauthTokenResponse = alipayClient.execute(request);
            String accessToken = oauthTokenResponse.getAccessToken();
            AlipayUserInfoShareRequest infoShareRequest = new AlipayUserInfoShareRequest();
            //AlipayUserInfoShareResponse response = new AlipayUserInfoShareResponse();
            //response.setUserId(oauthTokenResponse.getUserId());
            //return response;
            return alipayClient.execute(infoShareRequest, accessToken);
        } catch (AlipayApiException e) {
            //处理异常
            log.error("getBuyerId failed: auth_code = {}", authCode, e);
        }
        return null;
    }

    public static String generateQrCode(String url, String queryParam) throws CoBusinessException {
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.APPID, AlipayConfig.RSA_PRIVATE_KEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.SIGNTYPE);
        AlipayOpenAppQrcodeCreateRequest request = new AlipayOpenAppQrcodeCreateRequest();
        AlipayOpenAppQrcodeCreateModel model = new AlipayOpenAppQrcodeCreateModel();
        model.setUrlParam(url);
        model.setQueryParam(queryParam);
        model.setDescribe("支付宝小程序分享");
        request.setBizModel(model);
        try {
            AlipayOpenAppQrcodeCreateResponse response = alipayClient.execute(request);
            if (response.isSuccess()) {
                return response.getQrCodeUrl();
            } else if (response.getSubCode().equals("isv.insufficient-isv-permissions")) {
                throw new CoBusinessException(CoReturnFormat.ALIPAY_QRCODE_INSUF_PER);
            } else {
                throw new CoBusinessException(CoReturnFormat.SYS_ERROR);
            }
        } catch (AlipayApiException e) {
            //处理异常
            log.error("generateQrCode failed: url = {}, queryParam = {}", url, queryParam, e);
            throw new CoBusinessException(CoReturnFormat.SYS_ERROR);
        }
    }

}
