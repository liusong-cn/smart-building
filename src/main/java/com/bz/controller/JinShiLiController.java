package com.bz.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.bz.cache.CarInfoCache;
import com.bz.cache.CommonCache;
import com.bz.common.entity.CarIn;
import com.bz.common.entity.CarOut;
import com.bz.common.entity.R;
import com.bz.common.entity.Result;
import com.bz.common.entity.TbElectricityDataEntity;
import com.bz.common.entity.TbWaterPressureEntity;
import com.bz.common.vo.ParkingInfo;
import com.bz.mapper.TbElectricityDataMapper;
import com.bz.mapper.TbWaterPressureMapper;
import com.bz.properties.WeatherProperties;
import com.bz.service.JinShiLiService;
import com.bz.utils.WeatherUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * 汽车监控
 *
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
     *
     * @param deviceCode
     * @return
     */
    @GetMapping("/getPressureInfo")
    public Result getPressureInfo(@RequestParam(value = "deviceCode", required = true) String deviceCode) {
        log.info("查询水压采集设备信息");
        return jinShiLiService.getPressureInfo(deviceCode);
    }

    /**
     * 可视化侧获取电流采集设备信息
     *
     * @param deviceCode
     * @return
     */
    @GetMapping("/getElectricityInfo")
    public Result<List> getElectricityInfo(@RequestParam(value = "deviceCode", required = true) String deviceCode) {
        log.info("查询电流采集设备信息");
        return jinShiLiService.getElectricityInfo(deviceCode);
    }

    @GetMapping("/getCarInfo")
    public Result<List> getCarInfo(@RequestParam(value = "channel", required = true) String channel) {
        if (channel.isEmpty()) {
            return new Result<>(-1, "通道号不能为空");
        }
        log.info("查询车辆最近出入场信息");
        return jinShiLiService.getCarInfo(channel);
    }

    @GetMapping("/getBarrierGateInfo")
    public Result<List> getBarrierGateInfo(@RequestParam(value = "channel", required = true) String channel) {
        if (channel.isEmpty()) {
            return new Result<>(-1, "通道号不能为空");
        }
        log.info("查询停车场道闸信息");
        return jinShiLiService.getBarrierGateInfo(channel);
    }

    /**
     * 供金时利访问最新需要显示在大屏的消息
     *
     * @return
     */
    @GetMapping("/getRecentMessage")
    public ResponseEntity getMessage() throws UnsupportedEncodingException {
        log.info("金时利查询最新大屏消息");
        double pm10 = Double.parseDouble(WeatherUtil.pm10);
        double pm10Limit = Double.parseDouble(weatherProperties.getPm10Limit());
        String s = pm10 >= pm10Limit ? "扬尘超标风险，请立即启用应急措施" : "";
        JSONObject j = new JSONObject();
        j.put("code", 200);
        j.put("message", s);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "text/plain; charset=gb2312");
        return ResponseEntity.status(200).headers(headers).body(j.toString());
    }

    /**
     * 金时利推送车辆入场
     *
     * @param carIn
     * @return
     */
    @PostMapping(value = "/carIn", consumes = APPLICATION_JSON_VALUE)
    public ParkingInfo carIn(@RequestBody CarIn carIn) {
        log.info("接收金时利车辆入场信息");
        String plateNumber = carIn.getPlatenumber();
        String enterTime = carIn.getEntertime();
        if (StrUtil.isNotEmpty(plateNumber) && StrUtil.isNotEmpty(enterTime)) {
//            CarInfoCache.StorePlateNoIn(plateNumber);
            CommonCache.getInstance().put("plateNoIn",plateNumber);
            //截取月日 时分
//            CarInfoCache.StoreEnterTime(enterTime.substring(5,16));
            CommonCache.getInstance().put("enterTime",enterTime.substring(5,16));
            return ParkingInfo.success();

        }
        return ParkingInfo.error().setMsg("车牌号或时间为空");
    }

    /**
     * 金时利推送车辆出场
     *
     * @param carOut
     * @return
     */
    @PostMapping(value = "/carOut", consumes = APPLICATION_JSON_VALUE)
    public ParkingInfo carOut(@RequestBody CarOut carOut) {
        log.info("接收金时利车辆出场信息");
        String plateNumber = carOut.getPlatenumber();
        String leaveTime = carOut.getLeavetime();
        if (StrUtil.isNotEmpty(plateNumber) && StrUtil.isNotEmpty(leaveTime)) {
//            CarInfoCache.StorePlateNoIn(plateNumber);
            CommonCache.getInstance().put("plateNoOut",plateNumber);
            //截取月日 时分
//            CarInfoCache.StoreLeaveTime(leaveTime.substring(5,16));
            CommonCache.getInstance().put("leaveTime",leaveTime.substring(5,16));
            return ParkingInfo.success();

        }
        return ParkingInfo.error().setMsg("车牌号或时间为空");
    }

    @GetMapping("/showCar")
    public ResponseEntity showCar(@RequestParam(value = "type", required = true) String type) {
        String s;
        if (type.equals("in")) {
//            s = String.format("入场车牌:%s,时间:%s", CarInfoCache.getPlateNoIn(),CarInfoCache.getEnterTime());
            s = String.format("入场车牌:%s,入场时间:%s",CommonCache.getInstance().get("plateNoIn"),
                    CommonCache.getInstance().get("enterTime"));
        } else {
//            s = String.format("出场车牌:%s,时间:%s", CarInfoCache.getPlateNoOut(),CarInfoCache.getLeaveTime());
            s = String.format("出场车牌:%s,出场时间:%s",CommonCache.getInstance().get("plateNoOut"),
                    CommonCache.getInstance().get("leaveTime"));
        }
        JSONObject j = new JSONObject();
        j.put("code", 200);
        j.put("message", s);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "text/plain; charset=gb2312");
        return ResponseEntity.status(200).headers(headers).body(j.toString());
    }

}
