package com.bz.scheduling;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.bz.cache.CarBaseInfoCache;
import com.bz.common.entity.Result;
import com.bz.common.vo.CarBaseInfo;
import com.bz.properties.CarMonitorProperties;
import com.bz.service.CarMonitorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.xml.crypto.Data;
import java.util.List;

/**
 * @author:ls
 * @date: 2020/10/12 14:52
 **/
@Component
@Slf4j
public class CarBaseInfoScheduler {

    @Autowired
    private CarMonitorProperties carMonitorProperties;

    @Scheduled(fixedRate = 86400000)
    public void carBaseInfoCache(){
        String result = HttpUtil.get(carMonitorProperties.getInfo());
        log.info("通过定时器取得中自车辆基本信息:" + result);
        JSONObject jsonObject = new JSONObject(result);
        List<CarBaseInfo> carBaseInfoList = JSONUtil.toList((JSONArray) jsonObject.get("data"), CarBaseInfo.class);
        CarBaseInfoCache.setCache(carBaseInfoList);
    }
}
