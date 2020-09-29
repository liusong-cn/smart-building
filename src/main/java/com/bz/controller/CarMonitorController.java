package com.bz.controller;

import com.bz.common.entity.Result;
import com.bz.service.CarMonitorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 汽车监控
 * @author:ls
 * @date: 2020/9/28 14:02
 **/
@RestController
@Slf4j
@RequestMapping("carMonitor")
public class CarMonitorController {

    @Resource
    private CarMonitorService carMonitorService;

    @GetMapping("/historyInfo")
    public Result<String> historyMonitoringInfo(String carNo,String startDateTime,String endDateTime){
        log.info("查询车辆历史信息");
        return carMonitorService.historyMonitoringInfo(carNo,startDateTime,endDateTime);
    }

    @GetMapping("/realtimeInfo")
    public Result<String> realtimeInfo(){
        log.info("查询车辆实时信息");
        return carMonitorService.realtimeInfo();
    }

    @GetMapping("/carInfo")
    public Result<String> carInfo(){
        log.info("查询车辆基本信息");
        return carMonitorService.carsInfo();
    }

    @GetMapping("/warningInfo")
    public Result<String> warningInfo(){
        log.info("查询车辆告警信息");
        return carMonitorService.warningInfo();
    }
}
