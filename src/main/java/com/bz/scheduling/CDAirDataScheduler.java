package com.bz.scheduling;

import com.bz.service.ChengduAirDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author: ls
 * @date: 2020/12/21 19:19
 **/
@Component
@Slf4j
public class CDAirDataScheduler {

    @Autowired
    private ChengduAirDataService airDataService;

    /**
     * 保存市控站大气数据
     * cron 每天23:59执行
     */
    @Scheduled(cron = "0 59 23 * * ?")
    public void airDataSave(){
        log.info("通过定时器保存市控站大气数据");
        airDataService.saveAirData();
    }

}
