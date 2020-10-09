package com.bz.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author:ls
 * @date: 2020/10/9 9:19
 * 金时利配置
 **/
@Component
@ConfigurationProperties(prefix = "jinshili")
@Data
public class JinShiLiProperties {

    /**
     * 出入场信息url
     */
    private String carInfo;

    /**
     * 道闸信息url
     */
    private String barrierGateInfo;

}
