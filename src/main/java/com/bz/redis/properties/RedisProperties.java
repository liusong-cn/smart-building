package com.bz.redis.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author: ls
 * @date: 2020/12/24 9:56
 **/
@ConfigurationProperties(prefix = "custom.lettuce.redis")
@Component
public class RedisProperties {

    /**
     * 是否开启Lettuce Redis
     */
    private Boolean enable = true;

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    @Override
    public String toString() {
        return "LettuceRedisProperties{" +
                "enable=" + enable +
                '}';
    }
}
