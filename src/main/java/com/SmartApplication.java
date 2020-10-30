package com;

import com.bz.config.AliyunSmsConfigProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author:ls
 * @date: 2020/9/24 17:26
 **/
@EnableScheduling
@SpringBootApplication
@MapperScan(value = "com.bz.mapper")
@EnableConfigurationProperties(AliyunSmsConfigProperties.class)
public class SmartApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartApplication.class, args);
    }
}
