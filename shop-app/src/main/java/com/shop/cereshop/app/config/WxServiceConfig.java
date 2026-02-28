package com.shop.cereshop.app.config;

import cn.binarywang.wx.miniapp.api.WxMaQrcodeService;
import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaQrcodeServiceImpl;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceHttpClientImpl;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.MarketingMediaService;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.MarketingMediaServiceImpl;
import com.github.binarywang.wxpay.service.impl.WxPayServiceApacheHttpImpl;
import com.shop.cereshop.commons.constant.WxCpConstants;
import com.shop.cereshop.commons.utils.third.WxCardUtil;
import com.shop.cereshop.commons.utils.third.WxLiveUtil;
import me.chanjar.weixin.cp.api.WxCpMediaService;
import me.chanjar.weixin.cp.api.WxCpUserService;
import me.chanjar.weixin.cp.api.impl.WxCpMediaServiceImpl;
import me.chanjar.weixin.cp.api.impl.WxCpServiceImpl;
import me.chanjar.weixin.cp.api.impl.WxCpUserServiceImpl;
import me.chanjar.weixin.cp.config.WxCpConfigStorage;
import me.chanjar.weixin.cp.config.WxCpTpConfigStorage;
import me.chanjar.weixin.cp.config.impl.WxCpDefaultConfigImpl;
import me.chanjar.weixin.cp.config.impl.WxCpTpDefaultConfigImpl;
import me.chanjar.weixin.cp.tp.service.WxCpTpService;
import me.chanjar.weixin.cp.tp.service.impl.WxCpTpServiceApacheHttpClientImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WxServiceConfig {

    @Autowired
    private WxWorkProperties wxWorkProperties;

    @Autowired
    private WxProperties wxProperties;

    @Autowired
    private WxCpTpService wxCpTpService;

    @Autowired
    private WxCpServiceImpl wxCpService;

    @Autowired
    private WxCpMediaService wxCpMediaService;

    private WxMaQrcodeService wxMaQrcodeService;

    @Bean
    public WxCpTpConfigStorage wxCpTpConfigStorage() {
        WxCpTpDefaultConfigImpl config = new WxCpTpDefaultConfigImpl();
        config.setBaseApiUrl(WxCpConstants.QY_API);
        BeanUtils.copyProperties(wxWorkProperties, config);
        return config;
    }

    @Bean
    public WxCpConfigStorage wxCpConfigStorage() {
        WxCpDefaultConfigImpl config = new WxCpDefaultConfigImpl();
        config.setBaseApiUrl(WxCpConstants.QY_API);
        BeanUtils.copyProperties(wxWorkProperties, config);
        return config;
    }

    @Bean
    public WxCpTpService getWxCpTpService() {
        wxCpTpService = new WxCpTpServiceApacheHttpClientImpl();
        wxCpTpService.setWxCpTpConfigStorage(wxCpTpConfigStorage());
        return wxCpTpService;
    }

    @Bean
    public WxCpServiceImpl getWxCpServiceImpl(){
        wxCpService = new WxCpServiceImpl();
        wxCpService.setWxCpConfigStorage(wxCpConfigStorage());
        return wxCpService;
    }

    @Bean
    public WxCpMediaService getWxCpMediaService() {
        wxCpMediaService = new WxCpMediaServiceImpl(getWxCpServiceImpl());
        return wxCpMediaService;
    }

    @Bean
    public WxCpUserService getWxCpUserService() {
        return new WxCpUserServiceImpl(getWxCpServiceImpl());
    }

    @Bean
    public WxMaService getWxMaService() {
        WxMaService wxMaService = new WxMaServiceHttpClientImpl();
        WxMaDefaultConfigImpl wxMaConfig = new WxMaDefaultConfigImpl();
        wxMaConfig.setAppid(wxProperties.getAppid());
        wxMaConfig.setSecret(wxProperties.getSecret());
        wxMaService.setWxMaConfig(wxMaConfig);
        return wxMaService;
    }

    @Bean
    public WxLiveUtil getWxLiveUtil() {
        return new WxLiveUtil(getWxMaService());
    }

    @Bean
    public WxPayService getWxPayService() {
        WxPayService wxPayService = new WxPayServiceApacheHttpImpl();
        WxPayConfig payConfig = new WxPayConfig();
        payConfig.setAppId(wxProperties.getAppid());
        payConfig.setMchId(wxProperties.getMchid());
        payConfig.setMchKey(wxProperties.getKey());
        payConfig.setKeyPath(wxProperties.getCerturl());
        payConfig.setPrivateKeyPath(wxProperties.getPrivateKeyPath());
        payConfig.setPrivateCertPath(wxProperties.getPrivateCertPath());
        payConfig.setApiV3Key(wxProperties.getApiV3Key());
        // 可以指定是否使用沙箱环境
        payConfig.setUseSandboxEnv(false);
        wxPayService.setConfig(payConfig);
        return wxPayService;
    }

    @Bean
    public MarketingMediaService getMarketingMediaService() {
        return new MarketingMediaServiceImpl(getWxPayService());
    }

    @Bean
    public WxCardUtil getWxCardUtil() {
        return new WxCardUtil(getWxPayService(), getMarketingMediaService());
    }

    @Bean
    public WxMaQrcodeService getWxMaQrcodeService() {
        if (wxMaQrcodeService == null) {
            wxMaQrcodeService = new WxMaQrcodeServiceImpl(getWxMaService());
        }
        return wxMaQrcodeService;
    }
}
