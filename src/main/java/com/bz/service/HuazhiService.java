package com.bz.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.bz.common.entity.Result;
import com.bz.common.vo.WarningInfo;
import com.bz.utils.AccessTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    @Value("${huazhi.warning.grant_type}")
    private String grantType;
    @Value("${huazhi.warning.client_id}")
    private String clientId;
    @Value("${huazhi.warning.client_secret}")
    private String clientSecret;
    @Value("${huazhi.warning.user}")
    private String user;
    @Value("${huazhi.warning.warningType}")
    private String warningType;
    @Value("${huazhi.warning.warningTokenUrl}")
    private String warningTokenUrl;
    @Value("${huazhi.warning.warningDataUrl}")
    private String warningDataUrl;

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

    public String getWarningToken(){
        try(CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        ){
            CloseableHttpResponse httpResponse = null;
            Map<String,String> params = new HashMap<>();
            params.put("grant_type", grantType);
            params.put("client_id", clientId);
            params.put("client_secret", clientSecret);
            String url = joinParams(warningTokenUrl, params);
            HttpGet httpGet = new HttpGet(url);
            httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            String result = EntityUtils.toString(httpEntity);
            String reg = "access_token=([\\w-]+)&expires_in=";
            Pattern patten = Pattern.compile(reg);
            Matcher matcher = patten.matcher(result);
            String token = null;
            while (matcher.find()) {
                token=matcher.group(1);
                AccessTokenUtil.huazhiAccessWarningToken.setAccess_token(token);
            }
            log.info("取得token数据：" + token);
            return token;
        } catch (IOException e) {
            log.error("无法创建httpclient");
            e.printStackTrace();
        }
        return null;
    }

    public Result<List> getEnvironmentalAirData(String wsid, String airDataUrl,String stationId){
        /*if(token==null){
            token = this.getToken(corpid,corpsecret,authTokenUrl);
        }*/
        String result = getEnvironmentalAirDataByHttp(wsid,airDataUrl,stationId);
        /*JSONObject j = new JSONObject(result);
        if(j.get("message").toString().equals("token无效，请重新获取token")){
            token = this.getToken(corpid,corpsecret,authTokenUrl);
            result = getEnvironmentalAirDataByHttp(token,stationId,startTimeStr,endTimeStr,airDataUrl);
        }*/
        return format(result, new ArrayList<>());
    }

    public String getEnvironmentalAirDataByHttp(String wsid,String airDataUrl,String stationId){
        try(CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        ){

            CloseableHttpResponse httpResponse = null;
            Map<String,String> params = new HashMap<>();
            params.put("wsid", wsid);
            if(stationId!=null) {
                params.put("stationId", stationId);
            }
            String url = joinParams(airDataUrl, params);
            HttpGet httpGet = new HttpGet(url);
            httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            String result = EntityUtils.toString(httpEntity);
            log.info("取得环境监测数据：" + result);
            return result;
        } catch (IOException e) {
            log.error("无法创建httpclient");
            e.printStackTrace();
        }
        return null;
    }

    public Result<List> getWarningData(String token,Integer time) throws Exception {
        if(token==null){
            token = this.getWarningToken();
        }
        String result = getWarningDataByHttp(token,time);
        JSONObject j = new JSONObject(result);
        if(j.get("message")!=null && j.get("message").toString().equals("[\"expired_accessToken\"]")){
            token = this.getWarningToken();
            result = getWarningDataByHttp(token,time);
        }
        return format2(result, new ArrayList<>());
    }


    public String getWarningDataByHttp(String token,Integer time){
        try(CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        ){

            if(time==null){
                time = -10;//默认当前时间减去10s
            }

            CloseableHttpResponse httpResponse = null;
            HttpPost httpPost = new HttpPost(warningDataUrl);
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setHeader("Authorization", token);
            httpPost.setHeader("User", user);

            Date now = DateUtil.date(System.currentTimeMillis());
            Date start = DateUtil.offsetSecond(now,time);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("alarm_type_subs",warningType);
            jsonObject.put("start_time",DateUtil.formatDateTime(start));
            jsonObject.put("end_time",DateUtil.formatDateTime(now));
            jsonObject.put("page_size",1000);
            httpPost.setEntity(new StringEntity(jsonObject.toString()));
            httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            String result = EntityUtils.toString(httpEntity);
            log.info("取得告警数据：" + result);
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

    private <E> Result<E> format2(String s, E type) throws Exception {
        JSONObject j = new JSONObject(s);
        Result<E> result = new Result<E>();
        result.setTotal((Integer) j.get("total"));
        List<WarningInfo> warningInfos = JSONUtil.toList(JSONUtil.parseArray(j.get("elements").toString()), WarningInfo.class);
        for(WarningInfo warningInfo : warningInfos){
            reflect(warningInfo);
        }
        result.setMessage("success");
        result.setData((E) warningInfos);
        log.info("返回可视化数据：" + result);
        return result;
    }

    public void reflect(WarningInfo e) throws Exception{
        Class cls = e.getClass();
        Field[] fields = cls.getDeclaredFields();
        for(int i=0; i<fields.length; i++){
            Field f = fields[i];
            f.setAccessible(true);
            if(f.get(e)==null){
                fields[i].set(e,"");
            }
        }
    }
}
