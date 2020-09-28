package com.bz.service;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.bz.common.entity.Result;
import com.bz.properties.CarMonitorProperties;
import jdk.nashorn.internal.runtime.regexp.JoniRegExp;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import springfox.documentation.schema.TypeNameExtractor;

import java.io.IOException;
import java.util.*;

/**
 * @author:ls
 * @date: 2020/9/28 14:09
 **/
@Service
@Slf4j
public class CarMonitorService {

//    @Value("${car.monitor.history}")
//    private String historyUrl;

    //采用properties文件集中管理
    @Autowired
    private CarMonitorProperties carMonitorProperties;

    public Result<String> historyMonitoringInfo(String carNo, String startDateTime, String endDateTime){
        try(CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        ){
            CloseableHttpResponse httpResponse = null;
            Map<String,String> params = new HashMap<>();
            params.put("carNo",carNo);
            params.put("startDateTime",startDateTime);
            params.put("endDateTime",endDateTime);
            String url = joinParams(carMonitorProperties.getHistory(),params);
            HttpGet httpGet = new HttpGet(url);
            httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            String result = EntityUtils.toString(httpEntity);
            log.info("取得车辆历史数据：" + result);
            return format(result,new String("a"));
        } catch (IOException e) {
            log.error("无法创建httpclient");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 车辆告警信息
     * @return
     */
    public Result<String> warningInfo(){
        return null;
    }

    /**
     * 车辆基本信息
     * @return
     */
    public Result<String> carsInfo(){
        return null;
    }

    /**
     * 车辆实时信息
     * @return
     */
    public Result<String> realtimeInfo(){
        return null;
    }

    /**
     * 添加参数到url中
     * @param paramMap
     * @return
     */
    private String joinParams(String url, Map<String,String> paramMap){
        if(paramMap == null || paramMap.size() == 0){
            return url;
        }
        StringBuffer sb = new StringBuffer();
        sb.append(url + "?");
        for (String key:paramMap.keySet()) {
            sb.append(key +"=" + paramMap.get(key) + "&");
        }
        String s = sb.substring(0,sb.length() - 1);
        return s;
    }

    private <E> Result<E> format(String s, E type){
        JSONObject j = new JSONObject(s);
        Result<E> result = new Result<E>();
        result.setCode(Integer.valueOf((String) j.get("code")));
        result.setTotal(Integer.valueOf((String) j.get("total")));
        result.setMessage((String) j.get("message"));
        E data = (E) j.get("data");
        result.setData(data);
        return result;
    }
}
