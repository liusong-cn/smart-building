package com.bz.controller;

import com.bz.common.entity.Result;
import com.bz.service.HuazhiService;
import com.bz.service.ZhaTuBaoService;
import com.bz.utils.AccessTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;


@RestController
@Slf4j
@Validated
@RequestMapping("/HuaZhi")
public class HuaZhiController {
    @Value("${huazhi.environmentalAirDataUrl}")
    private String environmentalAirDataUrl;

    @Autowired
    private HuazhiService huazhiService;

    @GetMapping("/getAirData")
    public Result<List> getAirData(@RequestParam(value = "stationId") String stationId,
                                   @RequestParam(value = "startTimeStr") String startTimeStr,
                                   @RequestParam(value = "endTimeStr") String endTimeStr){
        log.info("查询环境监测数据");
        String token = AccessTokenUtil.huazhiAccessToken.getAccess_token();
        return huazhiService.getEnvironmentalAirData(token,stationId,startTimeStr,endTimeStr,environmentalAirDataUrl);
    }

}
