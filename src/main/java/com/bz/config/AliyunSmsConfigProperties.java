package com.bz.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "aliyun.sms")
public class AliyunSmsConfigProperties {
    /**
     * 短信签名
     */
    private String sign;
    /**
     * ACCESS_KEY_ID
     */
    private String accessKeyId;
    /**
     * ACCESS_KEY_SECRET
     */
    private String accessKeySecret;

    private String defaultTemplateCode;
}
