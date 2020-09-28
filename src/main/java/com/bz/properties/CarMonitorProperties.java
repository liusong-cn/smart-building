package com.bz.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author:ls
 * @date: 2020/9/28 17:38
 * 配置类
 **/
@ConfigurationProperties(prefix = "car.monitor")
@Component
@Data
public class CarMonitorProperties {

    //历史信息接口url
    private String history;

    //告警信息接口url
    private String warning;

    //车辆基本信息接口url
    private String info;

    //车辆实时信息接口url
    private String realtime;

}
