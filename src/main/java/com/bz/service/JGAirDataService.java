package com.bz.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.bz.common.entity.JGAirData;
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

        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("UserName", airDataProperties.getUserName());
        paramMap.put("Password", airDataProperties.getPassword());
        paramMap.put("Time", time);
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

//    public static void main(String[] args) throws ParseException {
//        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmm");
//        String s = format.format(new Date());
//        System.out.println(format.parse("2020-20-20 41:33:22"));
//        HashMap<String, Object> paramMap = new HashMap<>();
//        paramMap.put("UserName", "%s");
//        String r = HttpUtil.joinParams("/abc", paramMap);
//        System.out.println(String.format(r, "xxx"));
//    }
}
