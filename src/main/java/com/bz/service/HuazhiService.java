package com.bz.service;

import cn.hutool.json.JSONObject;
import com.bz.common.entity.Result;
import com.bz.utils.AccessTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author:zy
 * @date: 2020/10/03 11:09
 **/
@Service
@Slf4j
public class HuazhiService {

    @Value("${huazhi.corpid}")
    private String corpid;

    @Value("${huazhi.corpsecret}")
    private String corpsecret;

    @Value("${huazhi.authTokenUrl}")
    private String authTokenUrl;

    public String getToken(String corpid, String corpsecret, String authTokenUrl){
        try(CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        ){
            CloseableHttpResponse httpResponse = null;
            Map<String,String> params = new HashMap<>();
            params.put("corpid", corpid);
            params.put("corpsecret", corpsecret);
            String url = joinParams(authTokenUrl, params);
            HttpGet httpGet = new HttpGet(url);
            httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            String result = EntityUtils.toString(httpEntity);
            JSONObject j = new JSONObject(result);
            log.info("取得token数据：" + result);
            if(j.get("success").toString().equals("true")){
                JSONObject taken = new JSONObject(j.get("data").toString());
                AccessTokenUtil.huazhiAccessToken.setAccess_token(taken.get("access_token").toString());
                return taken.get("access_token").toString();
            }
        } catch (IOException e) {
            log.error("无法创建httpclient");
            e.printStackTrace();
        }
        return null;
    }

    public Result<List> getEnvironmentalAirData(String stationId, String airDataUrl){
        /*if(token==null){
            token = this.getToken(corpid,corpsecret,authTokenUrl);
        }*/
        String result = getEnvironmentalAirDataByHttp(stationId,airDataUrl);
        /*JSONObject j = new JSONObject(result);
        if(j.get("message").toString().equals("token无效，请重新获取token")){
            token = this.getToken(corpid,corpsecret,authTokenUrl);
            result = getEnvironmentalAirDataByHttp(token,stationId,startTimeStr,endTimeStr,airDataUrl);
        }*/
        return format(result, new ArrayList<>());
    }

    public String getEnvironmentalAirDataByHttp(String wsid,String airDataUrl){
        /*List<Header> headers = new ArrayList<>();
        headers.add(new BasicHeader("Authorization",token));*/
        try(CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        ){

            CloseableHttpResponse httpResponse = null;
            Map<String,String> params = new HashMap<>();
            params.put("wsid", wsid);
            String url = joinParams(airDataUrl, params);
            HttpGet httpGet = new HttpGet(url);
            httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            String result = EntityUtils.toString(httpEntity);
            JSONObject j = new JSONObject(result);
            log.info("取得环境监测数据：" + result);
            return result;
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
    /*private <E> Result<E> format(String s, E type){
        JSONObject j = new JSONObject(s);
        Result<E> result = new Result<E>();
        result.setCode((Integer) j.get("code"));
        result.setMessage((String) j.get("message"));
        if(j.get("success").toString().equals("true")) {
            JSONObject d = new JSONObject(j.get("data").toString());
            result.setTotal((Integer) d.get("count"));
            E data = (E) d.get("list");
            result.setData(data);
        }
        return result;
    }*/
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

}
