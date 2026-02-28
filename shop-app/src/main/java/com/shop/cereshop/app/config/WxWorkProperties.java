package com.shop.cereshop.app.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "wxwork")
public class WxWorkProperties {

    private String suiteId;

    private String suiteSecret;

    private String token;

    private String aesKey;

    private String corpId;

    private String corpSecret;
}
