package com.bz.controller;

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

    @Resource
    private ZhaTuBaoService zhaTuBaoService;

    @GetMapping("/getVehicleInfo")
    public Result<List> getVehicleInfo(@RequestParam(value = "plateNo", required = false) String plateNo){
        log.info("查询车辆基本信息");
        String token = AccessTokenUtil.accessToken.getAccess_token();
        return zhaTuBaoService.getVehicleInfoAndStatus(username, appkey, token, plateNo, vehicleInfoUrl);
    }

    @GetMapping("/getVehicleStatus")
    public Result<List> getVehicleStatus(@RequestParam(value = "plateNo", required = true) String plateNo){
        log.info("查询车辆告警信息");
        String token = AccessTokenUtil.accessToken.getAccess_token();
        return zhaTuBaoService.getVehicleInfoAndStatus(username, appkey, token, plateNo, vehicleStatusurl);
    }

    @GetMapping("/getVehicleRecentPic")
    public Result<List> getVehicleRecentPic(@RequestParam(value = "plateNo", required = true) String plateNo){
        log.info("查询车辆图片信息");
        String token = AccessTokenUtil.accessToken.getAccess_token();
        return zhaTuBaoService.getVehicleInfoAndStatus(username, appkey, token, plateNo, vehicleRecentPic);
    }

}
