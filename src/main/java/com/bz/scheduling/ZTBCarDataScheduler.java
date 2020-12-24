package com.bz.scheduling;

import com.bz.service.ZhaTuBaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author: ls
 * @date: 2020/12/24 15:39
 **/
@Component
public class ZTBCarDataScheduler {

    @Autowired
    private ZhaTuBaoService zhaTuBaoService;

    /**
     * 每天零点执行一次
     */
    @Scheduled(cron = "0 0 0 * * ? ")
    public void saveCarBaseInfo(){
        zhaTuBaoService.saveCarBaseInfo();
    }

    /**
     * 每5分钟一次
     */
    @Scheduled(cron = "0 0/5 * * * ? ")
    public void saveWarningInfo(){
        zhaTuBaoService.saveWarningInfo();
    }

    /**
     * 每小时一次
     */
    @Scheduled(cron = "0 0 0/1 * * ?")
    public void saveCarGPS(){
        zhaTuBaoService.saveRecentGPS();
    }
}
