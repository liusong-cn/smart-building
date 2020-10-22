package com.bz.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author:ls
 * @date: 2020/10/19 13:14
 **/
@Component
@ConfigurationProperties(prefix = "huazhi.weather")
@Data
public class WeatherProperties {

    //pm10限制值
    private String pm10Limit;
}
