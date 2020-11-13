package com.bz.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author:ls
 * @date: 2020/11/12 14:03
 * 聚光大气配置项
 **/
@Component
@Data
@ConfigurationProperties(prefix = "jg.air-data")
public class JGAirDataProperties {

    private String userName;

    private String password;

    //多站点以逗号分割
    private String stationCodes;

    //每分钟单条数据接口url
    private String dataMinUrl;

}
