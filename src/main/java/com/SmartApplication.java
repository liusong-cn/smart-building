package com;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author:ls
 * @date: 2020/9/24 17:26
 **/
@SpringBootApplication
@MapperScan(value = "com.bz.mapper")
public class SmartApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartApplication.class,args);
    }
}
