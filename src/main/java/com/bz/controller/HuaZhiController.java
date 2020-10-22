package com.bz.controller;

import cn.hutool.json.JSONArray;
import com.bz.common.entity.Result;
import com.bz.service.HuazhiService;
import com.bz.service.ZhaTuBaoService;
import com.bz.utils.AccessTokenUtil;
import com.bz.utils.WeatherUtil;
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
    public Result<List> getAirData(@RequestParam(value = "wsid") String wsid){
        log.info("查询环境监测数据");
        //String token = AccessTokenUtil.huazhiAccessToken.getAccess_token();
        Result r = huazhiService.getEnvironmentalAirData(wsid,environmentalAirDataUrl);
        storePm10(r);
        return r;
    }


    @GetMapping("/getWarningData")
    public Result<List> getWarningData(@RequestParam(value = "time",required = false)  Integer time) throws Exception {
        log.info("查询告警信息");
        String token = AccessTokenUtil.huazhiAccessWarningToken.getAccess_token();
        return huazhiService.getWarningData(token,time);
    }

    private void storePm10(Result r){
        if(r.getData() instanceof JSONArray){
            JSONArray jsonArray = (JSONArray) r.getData();
            if(jsonArray.size()>0){
                String pm10 = jsonArray.getByPath("[0].pm10",String.class);
                if(pm10 != null && !pm10.equals("")){
                    WeatherUtil.pm10 = pm10;
                }
            }
        }
    }

}
