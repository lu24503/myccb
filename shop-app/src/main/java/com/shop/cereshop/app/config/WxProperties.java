package com.shop.cereshop.app.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "weixin")
public class WxProperties {

    private String appid;

    private String secret;

    private String mchid;

    private String key;

    private String certurl;

    private String apiV3Key;

    private String privateKeyPath;

    private String privateCertPath;

    private String liveTemplateId;

    private String liveAppId;
}
