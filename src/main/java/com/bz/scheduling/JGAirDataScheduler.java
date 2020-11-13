package com.bz.scheduling;

import com.bz.service.JGAirDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author:ls
 * @date: 2020/11/13 9:04
 **/
@Component
public class JGAirDataScheduler {

    @Autowired
    private JGAirDataService airDataService;

    /**
     * 获取每分钟大气数据
     * cron 每分钟执行一次
     */
    @Scheduled(cron = "0 */1 * * * ?")
    public void airDataMinute(){
        airDataService.httpAirData();
    }
}
