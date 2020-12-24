package com.bz.controller;

import cn.hutool.core.util.StrUtil;
import com.bz.common.entity.Result;
import com.bz.service.ZhaTuBaoService;
import com.bz.utils.AccessTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 汽车监控
 * @author:zy
 * @date: 2020/10/03 11:02
 **/
@RestController
@Slf4j
@Validated
@RequestMapping("zhaTuBao")
public class ZhaTuBaoController {
    @Value("${zhatubao.username}")
    private String username;

    @Value("${zhatubao.appkey}")
    private String appkey;

    @Value("${zhatubao.appsecret}")
    private String appsecret;

    @Value("${zhatubao.getAuthTokenUrl}")
    private String authTokenUrl;

    @Value("${zhatubao.getVehicleInfoUrl}")
    private String vehicleInfoUrl;

    @Value("${zhatubao.getVehicleStatusurl}")
    private String vehicleStatusurl;

    @Value("${zhatubao.getVehicleRecentPic}")
    private String vehicleRecentPic;

    @Value("${zhatubao.vehicleRecentGps}")
    private String vehicleRecentGps;

    @Resource
    private ZhaTuBaoService zhaTuBaoService;


    @GetMapping("/getVehicleInfo")
    public Result<List> getVehicleInfo(@RequestParam(value = "plateNo", required = false) String plateNo){
        log.info("查询车辆基本信息");
//        String token = AccessTokenUtil.accessToken.getAccess_token();
        String token = zhaTuBaoService.getTokenFromRedis();
        return zhaTuBaoService.getVehicleInfoAndStatus(username, appkey, token, plateNo, vehicleInfoUrl);
    }

    @GetMapping("/getVehicleStatus")
    public Result<List> getVehicleStatus(@RequestParam(value = "plateNo", required = true) String plateNo){
        log.info("查询车辆告警信息");
//        String token = AccessTokenUtil.accessToken.getAccess_token();
        String token = zhaTuBaoService.getTokenFromRedis();
        return zhaTuBaoService.getVehicleInfoAndStatus(username, appkey, token, plateNo, vehicleStatusurl);
    }

    @GetMapping("/getVehicleRecentPic")
    public Result<List> getVehicleRecentPic(@RequestParam(value = "plateNo", required = true) String plateNo){
        if(StrUtil.isBlank(plateNo)){
            return new Result(-1,"车牌号不能为空");
        }
        log.info("查询车辆图片信息");
//        String token = AccessTokenUtil.accessToken.getAccess_token();
        String token = zhaTuBaoService.getTokenFromRedis();
        return zhaTuBaoService.getVehicleInfoAndStatus(username, appkey, token, plateNo, vehicleRecentPic);
    }

    @GetMapping("/getVehicleRecentGps")
    public Result getVehicleRecentGps(@RequestParam(value = "plateNo",required = true) String plateNo){
        if(StrUtil.isBlank(plateNo)){
            return new Result(-1,"车牌号不能为空");
        }
        log.info("查询车辆最近一小时轨迹");
//        String token =  AccessTokenUtil.accessToken.getAccess_token();
        String token = zhaTuBaoService.getTokenFromRedis();
        return zhaTuBaoService.queryRecentVehicleGps(plateNo,username,appkey,token,vehicleRecentGps);
    }

    @GetMapping("/getCarInfoMerged")
    public Result getCarInfoMerged(@RequestParam(value = "plateNo", required = false) String plateNo){
        log.info("查询中自及渣土宝车辆信息合集");
//        String token = AccessTokenUtil.accessToken.getAccess_token();
        String token = zhaTuBaoService.getTokenFromRedis();
        return zhaTuBaoService.queryCarInfoMerged(plateNo,username,appkey,token,vehicleInfoUrl);
    }

}
