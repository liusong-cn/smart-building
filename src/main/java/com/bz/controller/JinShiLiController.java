package com.bz.controller;

import com.bz.common.entity.R;
import com.bz.common.entity.Result;
import com.bz.common.entity.TbElectricityDataEntity;
import com.bz.common.entity.TbWaterPressureEntity;
import com.bz.mapper.TbElectricityDataMapper;
import com.bz.mapper.TbWaterPressureMapper;
import com.bz.properties.WeatherProperties;
import com.bz.service.JinShiLiService;
import com.bz.utils.WeatherUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * 汽车监控
 * @author:zy
 * @date: 2020/10/03 11:02
 **/
@RestController
@Slf4j
@Validated
@RequestMapping("/KIMSHL")
public class JinShiLiController {

    @Autowired
    private TbWaterPressureMapper tbWaterPressureMapper;

    @Autowired
    private TbElectricityDataMapper tbElectricityDataMapper;

    @Resource
    private JinShiLiService jinShiLiService;

    @Autowired
    private WeatherProperties weatherProperties;

    @PostMapping(value = "/pressureCollect", consumes = APPLICATION_JSON_VALUE)
    public Result pressureCollect(@RequestBody TbWaterPressureEntity entity) throws Exception {
        log.info("接收水压数据");
        int result = tbWaterPressureMapper.insert(entity);
        if (result != 1) {
            log.info("新增水压数据失败");
            return new Result(R.FAILURE);
        }
        return new Result(R.SUCCESS);
    }

    @PostMapping(value = "/electricityCollect", consumes = APPLICATION_JSON_VALUE)
    public Result electricityCollect(@RequestBody TbElectricityDataEntity entity) throws Exception {
        log.info("接收电流数据");
        int result = tbElectricityDataMapper.insert(entity);
        if (result != 1) {
            log.info("新增电流数据失败");
            return new Result(R.FAILURE);
        }
        return new Result(R.SUCCESS);
    }

    /**
     * 可视化侧获取水压采集设备信息
     * @param deviceCode
     * @return
     */
    @GetMapping("/getPressureInfo")
    public Result getPressureInfo(@RequestParam(value = "deviceCode", required = true) String deviceCode){
        log.info("查询水压采集设备信息");
        return jinShiLiService.getPressureInfo(deviceCode);
    }

    /**
     * 可视化侧获取电流采集设备信息
     * @param deviceCode
     * @return
     */
    @GetMapping("/getElectricityInfo")
    public Result<List> getElectricityInfo(@RequestParam(value = "deviceCode", required = true) String deviceCode){
        log.info("查询电流采集设备信息");
        return jinShiLiService.getElectricityInfo(deviceCode);
    }

    @GetMapping("/getCarInfo")
    public Result<List> getCarInfo(@RequestParam(value = "channel",required = true) String channel){
        if(channel.isEmpty()){
            return new Result<>(-1,"通道号不能为空");
        }
        log.info("查询车辆最近出入场信息");
        return jinShiLiService.getCarInfo(channel);
    }

    @GetMapping("/getBarrierGateInfo")
    public Result<List> getBarrierGateInfo(@RequestParam(value = "channel",required = true) String channel){
        if(channel.isEmpty()){
            return new Result<>(-1,"通道号不能为空");
        }
        log.info("查询停车场道闸信息");
        return jinShiLiService.getBarrierGateInfo(channel);
    }

    /**
     * 供金时利访问最新需要显示在大屏的消息
     * @return
     */
    @GetMapping("/getRecentMessage")
    public Result getMessage() throws UnsupportedEncodingException {
        log.info("金时利查询最新大屏消息");
        double pm10 = Double.parseDouble(WeatherUtil.pm10);
        double pm10Limit = Double.parseDouble(weatherProperties.getPm10Limit());
        System.out.println(pm10Limit);
        String s = "";
        if(pm10 >= pm10Limit){
            s = new String("扬尘超标风险，请立即启用应急措施".getBytes("gbk"),"gb2312");
        }
        return new Result(200,s);
    }
}
