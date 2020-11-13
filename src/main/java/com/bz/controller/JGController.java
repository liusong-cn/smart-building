package com.bz.controller;

import com.bz.common.entity.JGAirData;
import com.bz.common.entity.R;
import com.bz.common.entity.Result;
import com.bz.service.JGAirDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @author:ls
 * @date: 2020/11/12 11:38
 * 聚光公司接口
 **/
@RestController
@RequestMapping("/jg")
@Slf4j
public class JGController {

    @Autowired
    private JGAirDataService airDataService;

    @GetMapping("/getRecentAirData")
    public Result getRecentAirData(@RequestParam(value = "stationCode",
            required = false) String stationCode){
        log.info("查询聚光大气最近数据");
        List<JGAirData> data =  airDataService.queryRecentAirData(stationCode);
        return new Result(R.SUCCESS,data,data.size());
    }

    @GetMapping("/getHistoryAirData")
    public Result getHistoryAirData(@RequestParam(value = "stationCode", required = true) String stationCode,
                                    @RequestParam(defaultValue = "100",required = false) int pageSize,
                                    @RequestParam(defaultValue = "1",required = false) int pageNo){
        log.info("查询聚光大气历史数据");
        List<JGAirData> data = airDataService.queryHistoryAirData(stationCode,pageSize,pageNo);
        return new Result(R.SUCCESS,data,data.size());
    }

    @GetMapping("/getHourAirData")
    public String getHourAirData(@RequestParam(value = "time",required = true) String time,
                                 @RequestParam(value = "stationCode",required = true)String stationCode){
        log.info("查询聚光单条小时数据");
        return airDataService.getHourAirData(stationCode,time);
    }

    @GetMapping("/getHourAirDatas")
    public String getHourAirDatas(@RequestParam(value = "startTime",required = true) String startTime,
                                  @RequestParam(value = "stationCode",required = true)String stationCode,
                                  @RequestParam(value = "endTime",required = true) String endTime){
        log.info("查询聚光多条小时数据");
        return airDataService.getHourAirDatas(stationCode,startTime,endTime);
    }
}
