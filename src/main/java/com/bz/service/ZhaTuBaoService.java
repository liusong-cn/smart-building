package com.bz.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.bz.cache.CarBaseInfoCache;
import com.bz.common.entity.Result;
import com.bz.common.vo.CarBaseInfo;
import com.bz.common.vo.CarInfo;
import com.bz.common.vo.CarRealtimeInfo;
import com.bz.properties.CarMonitorProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author:zy
 * @date: 2020/10/03 11:09
 **/
@Service
@Slf4j
public class ZhaTuBaoService {

    @Autowired
    private CarMonitorService carMonitorService;

    @Autowired
    private CarMonitorProperties carMonitorProperties;

    public String getToken(String username, String appsecret, String authTokenUrl){
        try(CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        ){
            CloseableHttpResponse httpResponse = null;
            Map<String,String> params = new HashMap<>();
            params.put("username", username);
            params.put("appsecret", appsecret);
            String url = joinParams(authTokenUrl, params);
            HttpGet httpGet = new HttpGet(url);
            httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            String result = EntityUtils.toString(httpEntity);
            JSONObject j = new JSONObject(result);
            log.info("取得token数据：" + result);
            return j.get("data").toString();
        } catch (IOException e) {
            log.error("无法创建httpclient");
            e.printStackTrace();
        }
        return null;
    }


    public Result<List> getVehicleInfoAndStatus(String username, String appkey, String token, String plateNo, String baseUrl){
        try(CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        ){
            CloseableHttpResponse httpResponse = null;
            Map<String,String> params = new HashMap<>();
            params.put("username", username);
            params.put("appkey", appkey);
            params.put("token", token);
            if (plateNo != null && !plateNo.isEmpty()) {
                params.put("plateNo", plateNo);
            }
            String url = joinParams(baseUrl, params);
            HttpGet httpGet = new HttpGet(url);
            httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            String result = EntityUtils.toString(httpEntity);
            log.info("取得车辆基本数据：" + result);
            return format(result, new ArrayList<>());
        } catch (IOException e) {
            log.error("无法创建httpclient");
            e.printStackTrace();
        }
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
            String value = paramMap.get(key);
            //根据约定以下方"+"号方式处理空格
            sb.append(key +"=" + value + "&");
        }

        String s = sb.substring(0,sb.length() - 1);
        //处理参数中yyyy-mm-dd hh:mm:ss日期格式中空格问题
        s = s.replaceAll(" ","+");
        return s;
    }

    /**
     * 根据参数类型返回对应包装对象
     * @param s
     * @param type
     * @param <E>
     * @return
     */
    private <E> Result<E> format(String s, E type){
        JSONObject j = new JSONObject(s);
        Result<E> result = new Result<E>();
        result.setCode((Integer) j.get("code"));
        result.setTotal((Integer) j.get("total"));
        result.setMessage((String) j.get("message"));
        E data = (E) j.get("data");
        result.setData(data);
        return result;
    }

    private Result httpGet(String url, Map<String, Object> paramMap){
        String s = HttpUtil.get(url,paramMap);
        return format(s,new String("字符类型"));
    }

    /**
     * 查询车辆最近一小时轨迹
     * @param plateNo
     * @param username
     * @param appkey
     * @param token
     * @param url
     * @return
     */
    public Result queryRecentVehicleGps(String plateNo, String username, String appkey, String token,String url){
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("username", username);
        paramMap.put("appkey", appkey);
        paramMap.put("token", token);
        paramMap.put("plateNo",plateNo);
        return httpGet(url,paramMap);
    }

    /**
     * 合并中自和渣土宝的车辆基本以及实时信息
     * @param plateNo
     * @param userName
     * @param appkey
     * @param token
     * @param url
     * @return
     */
    public Result queryCarInfoMerged(String plateNo, String userName, String appkey, String token, String url){
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("username",userName);
        paramMap.put("appkey",appkey);
        paramMap.put("token",token);
        if(!StrUtil.isBlank(plateNo))
            paramMap.put("plateNo",plateNo);
        String s = HttpUtil.get(url,paramMap);
        log.info("取得渣土宝车辆信息:" + s);
        JSONObject jsonObject = new JSONObject(s);
        //中自车辆实时信息
        String realtimeInfoStr = HttpUtil.get(carMonitorProperties.getRealtime());
        log.info("取得中自车辆实时信息" + realtimeInfoStr);
        JSONObject realtimeInfoJson = new JSONObject(realtimeInfoStr);
        List<CarRealtimeInfo> carRealtimeInfoList = JSONUtil.toList((JSONArray) realtimeInfoJson.get("data"),CarRealtimeInfo.class);

        int total = (int)jsonObject.get("total");
        if(total > 0){
            JSONArray array = new JSONArray(jsonObject.get("data"));
            List<CarInfo> carInfoList = JSONUtil.toList(array, CarInfo.class);
            //将中自和渣土宝的车辆信息合并
            carInfoList = carInfoList.stream().map(carInfo ->{
                String carNo = carInfo.getPlateNo();
                //合并中自车辆实时信息
                carRealtimeInfoList.stream().filter(carRealtimeInfo ->{
                    return carRealtimeInfo.getCarNo().equals(carNo);
                }).forEach(carRealtimeInfo -> {
                    carInfo.setT3(carRealtimeInfo.getT3());
                    carInfo.setT4(carRealtimeInfo.getT4());
                    carInfo.setVo(carRealtimeInfo.getVo());
                    carInfo.setIntake(carRealtimeInfo.getIntake());
                    carInfo.setNoxBefore(carRealtimeInfo.getNoxBefore());
                    carInfo.setNoxBehind(carRealtimeInfo.getNoxBehind());
                    carInfo.setUreaPumpStatus(carRealtimeInfo.getUreaPumpStatus());
                    carInfo.setUreaInjectionVolume(carRealtimeInfo.getUreaInjectionVolume());
                    carInfo.setUreaLiquidHeight(carRealtimeInfo.getUreaLiquidHeight());
                    carInfo.setUreaSolutionTemperature(carRealtimeInfo.getUreaSolutionTemperature());
                    carInfo.setUreaTankHeatingState(carRealtimeInfo.getUreaTankHeatingState());
                    carInfo.setExhaustFlow(carRealtimeInfo.getExhaustFlow());
                    carInfo.setLocate(carRealtimeInfo.getLocate());
                });

                //合并中自车辆基本信息
                CarBaseInfoCache.getCache().stream().filter(carBaseInfo -> {
                    return carBaseInfo.getCarNo().equals(carNo);
                }).forEach(carBaseInfo -> {
                    carInfo.setCarModel(carBaseInfo.getCarModel());
                    carInfo.setCarOwnerName(carBaseInfo.getCarOwnerName());
                    carInfo.setCarOwnerTel(carBaseInfo.getCarOwnerTel());
                    carInfo.setProDate(carBaseInfo.getProDate());
                    carInfo.setManufacturer(carBaseInfo.getManufacturer());
                    carInfo.setCarUse(carBaseInfo.getCarUse());
                    carInfo.setDisplacement(carBaseInfo.getDisplacement());
                    carInfo.setVin(carBaseInfo.getVin());
                    carInfo.setRoad(carBaseInfo.getRoad());
                });

                return carInfo;
            }).collect(Collectors.toList());
            jsonObject.put("data",JSONUtil.parse(carInfoList));

        }
        s = jsonObject.toString();
        return format(s,new String("字符类型"));
    }

}
