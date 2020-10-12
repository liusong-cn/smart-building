package com.bz.service;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import com.bz.common.entity.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author:zy
 * @date: 2020/10/03 11:09
 **/
@Service
@Slf4j
public class ZhaTuBaoService {
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

}
