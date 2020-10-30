package com.bz.controller;

import cn.hutool.json.JSONArray;
import com.bz.common.entity.R;
import com.bz.common.entity.Result;
import com.bz.common.entity.TbAirDataEntity;
import com.bz.mapper.TbAirDataMapper;
import com.bz.service.HuazhiService;
import com.bz.utils.AccessTokenUtil;
import com.bz.utils.WeatherUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@RestController
@Slf4j
@Validated
@RequestMapping("/HuaZhi")
public class HuaZhiController {
    @Value("${huazhi.environmentalAirDataUrl}")
    private String environmentalAirDataUrl;

    @Autowired
    private TbAirDataMapper tbAirDataMapper;

    @Autowired
    private HuazhiService huazhiService;

    @GetMapping("/getAirData")
    public Result<List> getAirData(@RequestParam(value = "wsid") String wsid,
                                   @RequestParam(value = "stationId",required = false) String stationId){
        log.info("查询环境监测数据");
        //String token = AccessTokenUtil.huazhiAccessToken.getAccess_token();
        Result r = huazhiService.getEnvironmentalAirData(wsid,environmentalAirDataUrl,stationId);
        storePm10(r);
        return r;
    }

    @PostMapping(value = "/environmentData", consumes = APPLICATION_JSON_VALUE)
    public Result environmentDataCollect(@RequestBody List<TbAirDataEntity> tbAirDataEntities) throws Exception {
        log.info("接收环境监测数据:"+tbAirDataEntities);
        int result = -1;
        for(TbAirDataEntity entity:tbAirDataEntities){
            try {
                result = tbAirDataMapper.insert(entity);
            }catch (Exception e){
                log.error("新增环境监测数据失败:"+entity);
                log.error("error",e);
            }
        }
        if (result != 1) {
            return new Result(R.FAILURE);
        }
        return new Result(R.SUCCESS);
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
