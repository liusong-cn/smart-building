package com.bz.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.bz.common.entity.JGAirData;
import com.bz.common.entity.R;
import com.bz.common.entity.Result;
import com.bz.exception.StringEmptyException;
import com.bz.mapper.JGAirDataMapper;
import com.bz.properties.JGAirDataProperties;
import com.bz.proxy.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author:ls
 * @date: 2020/11/12 14:00
 **/
@Service
@Slf4j
public class JGAirDataService {

    @Autowired
    private JGAirDataMapper airDataMapper;

    @Autowired
    private JGAirDataProperties airDataProperties;

    public void insertAirData(JGAirData airData) {
        int r = airDataMapper.insert(airData);
        if (r >= 1) {
            log.info("新增聚光大气数据成功：" + airData);
        } else {
            log.error("新增聚光大气数据失败：" + airData);
        }
    }

    public List<JGAirData> queryRecentAirData(String stationCode){
        return airDataMapper.recentAirData(stationCode);
    }

    public List<JGAirData> queryHistoryAirData(String stationCode,int pageSize,int pageNo){
        int offset = (pageNo-1)*pageSize;
        return airDataMapper.airDataHistory(stationCode,offset,pageSize);
    }

    public void httpAirData() {

        String[] stationCodes = airDataProperties.getStationCodes().split(",");
        String userName = airDataProperties.getUserName();
        String password = airDataProperties.getPassword();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmm");
        Date now = new Date();
        String time = format.format(now);
        String timeDelay = String.valueOf(Long.valueOf(time) -2);

        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("UserName", airDataProperties.getUserName());
        paramMap.put("Password", airDataProperties.getPassword());
        paramMap.put("Time", timeDelay);
        paramMap.put("StationCode", "%s");
        String url = HttpUtil.joinParams(airDataProperties.getDataMinUrl(), paramMap);

        String[] rs = new String[stationCodes.length];
        for (String stationCode : stationCodes) {
            try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
                HttpGet httpGet = new HttpGet(String.format(url, stationCode));
                CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                String result = EntityUtils.toString(httpEntity);
                JGAirData airData = transform(result);
                if(isValid(airData)){
                    airData.setTime(now);
                    insertAirData(airData);
                }
                log.info("取得聚光大气数据：" + result);
            } catch (IOException e) {
                e.printStackTrace();
                log.error("获取聚光大气数据报错：" + e.getMessage());
            }

        }

    }

    private JGAirData transform(String entityStr){
        String s = entityStr.replaceAll("PM2.5","PM25");
        JSONObject j = new JSONObject(s);
        if(!j.get("Code").toString().equals("200")){
            log.error("成功连接聚光大气数据，但发生错误：" + entityStr);
            throw new RuntimeException("成功连接聚光大气数据，但发生错误：" + entityStr);
        }
        return j.get("Content",JGAirData.class);
    }

    private boolean isValid(JGAirData airData){
        Map<String, Object> airDataMap = BeanUtil.beanToMap(airData);
        List<String> ignoredFields = Arrays.asList("Latitude","StationCode","PointName");
        boolean r = airDataMap.entrySet().stream()
                .filter((entry) ->{
                    return !ignoredFields.contains(entry.getKey());
                }).anyMatch((entry)->{
                    return (entry.getValue() != null && !entry.getValue().equals(""));
                });

        return r;
    }

    public String getHourAirData(String stationCode, String time){
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("UserName",airDataProperties.getUserName());
        paramMap.put("Password",airDataProperties.getPassword());
        paramMap.put("StationCode",stationCode);
        paramMap.put("Time",time);
        try {
            String r = HttpUtil.httpGet(airDataProperties.getDataHourUrl(),paramMap);
            log.info("获取到聚光小时大气数据：" + r);
            String s = transformHourData(r);
            return new Result<>(R.SUCCESS,s,1).toString();
        } catch (IOException e) {
            e.printStackTrace();
            log.info("获取聚光小时大气数据超时：" + e.getMessage());
            return new Result<>(-1,e.getMessage()).toString();
        }
    }

    public String getHourAirDatas(String stationCode, String startTime, String endTime){
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("UserName",airDataProperties.getUserName());
        paramMap.put("Password",airDataProperties.getPassword());
        paramMap.put("StationCode",stationCode);
        paramMap.put("StartTime",startTime);
        paramMap.put("EndTime",endTime);
        try {
            String r = HttpUtil.httpGet(airDataProperties.getDatasHourUrl(),paramMap);
            log.info("获取到聚光多条小时大气数据：" + r);
            String s = transformHourDatas(r);
            int total = Integer.valueOf(s.substring(0,s.indexOf("&")));
            String realData = s.substring(s.indexOf("&") + 1);
            return new Result<>(R.SUCCESS,realData,total).toString();
        } catch (IOException e) {
            e.printStackTrace();
            log.info("获取聚光多条小时大气数据超时：" + e.getMessage());
            return new Result<>(-1,e.getMessage()).toString();
        }
    }

    private String transformHourData(String str){
        JSONObject j = new JSONObject(str);
        JSONObject innerJ = new JSONObject(j.get("Content"));
        if(ObjectUtil.isEmpty(innerJ)){
            throw new StringEmptyException("聚光大气数据为空：" + str);
        }
        innerJ.remove("_id");
        innerJ.remove("MAIN_POLLUTANTS");
        return JSONUtil.toJsonStr(innerJ);
    }

    private String transformHourDatas(String str){
        JSONObject j = new JSONObject(str);
        JSONArray array = new JSONArray(j.get("Content"));
        if(ObjectUtil.isEmpty(array)){
            throw new StringEmptyException("聚光小时多条大气数据为空：" + str);
        }
        List array1 = array.stream().map((obj)->{
            JSONObject jo = new JSONObject(obj);
            jo.remove("_id");
            jo.remove("MAIN_POLLUTANTS");
            return jo;
        }).collect(Collectors.toList());
        return array.size() + "&" +JSONUtil.toJsonStr(array1);
    }

//    public static void main(String[] args) throws ParseException {
//        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmm");
//        Date now = new Date();
//        String time = format.format(now);
//        String timeDelay = String.valueOf(Long.valueOf(time) -2);
//        System.out.println(time + "----"+timeDelay);
//    }
}
