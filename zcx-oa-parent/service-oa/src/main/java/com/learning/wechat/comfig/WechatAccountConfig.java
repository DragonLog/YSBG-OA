package com.learning.wechat.comfig;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("wechat")
public class WechatAccountConfig {

    private String mpAppId;

    private String mpAppSecret;

}
