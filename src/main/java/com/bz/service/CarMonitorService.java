package com.bz.service;

import cn.hutool.json.JSONObject;
import com.bz.common.entity.Result;
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
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

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
        return httpGet(carMonitorProperties.getWarning());
    }

    /**
     * 车辆基本信息
     * @return
     */
    public Result<String> carsInfo(){
        return httpGet(carMonitorProperties.getInfo());
    }

    /**
     * 车辆实时信息
     * @return
     */
    public Result<String> realtimeInfo(){
        return httpGet(carMonitorProperties.getRealtime());
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
//            if(value.contains(" ")){
//                value = URLEncoder.encode(value);
//            }
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
    @Deprecated
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

    /**
     * 通用httpget请求
     * @param url
     * @return
     */
    private Result<String> httpGet(String url){
        Result<String> r = null;
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        CloseableHttpResponse httpResponse = null;
        try {
            HttpGet httpGet = new HttpGet(url);
            httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            String result = EntityUtils.toString(httpEntity);
            log.info("接收中自的请求结果:" + result);
            r = format(result,new String("字符串类型"));
        } catch (ClientProtocolException e) {
            log.error("客户端协议不匹配:" + url);
            e.printStackTrace();
        } catch (IOException e) {
            log.error("连接失败：" + url);
            e.printStackTrace();
        }finally {
            if(httpClient != null){
                try {
                    httpClient.close();
                } catch (IOException e) {
                    log.error("客户端无法关闭：" + url);
                    e.printStackTrace();
                }
            }
        }
        return r;
    }
}
