package com.bz.proxy;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author:ls
 * @date: 2020/10/27 15:02
 **/
public class HttpUtil {

    public static JSONObject parseJson(String jsonStr){
        JSONObject j = JSONUtil.parseObj(jsonStr);
        int size = j.keySet().size();
        if(size <= 0)
            return null;
        return j;
    }

    public static Header[] parseHeader(JSONObject jsonObject){
        if(jsonObject != null && jsonObject.size() >0){
            int size = jsonObject.size();
            Header[] headers = new Header[size];
            int i = 0;
            for (String key: jsonObject.keySet()) {
                headers[i++] = new BasicHeader(key,(String) jsonObject.get(key));
            }
            return headers;
        }
        return null;
    }

    public static Header[] parseHeader(String header){
        JSONObject j = parseJson(header);
        int size = j.keySet().size();
        if(size <= 0)
            return null;
        return parseHeader(j);
    }

    public static HttpEntity parseRequestEntity(String requestEntity) throws UnsupportedEncodingException {
        JSONObject j = JSONUtil.parseObj(requestEntity);
        int size = j.keySet().size();
        if(size <= 0)
            return null;
        return new StringEntity(j.toString());
    }

    public static String joinParams(String url, Map<String,Object> paramMap){
        if(paramMap == null || paramMap.size() == 0){
            return url;
        }
        StringBuffer sb = new StringBuffer();
        sb.append(url + "?");
        for (String key:paramMap.keySet()) {
            Object value = paramMap.get(key);
            sb.append(key +"=" + value + "&");
        }

        String s = sb.substring(0,sb.length() - 1);
        //处理参数中yyyy-mm-dd hh:mm:ss日期格式中空格问题
        s = s.replaceAll(" ","+");
        return s;
    }

    public static String joinParamsWithStr(String url,String param){
        JSONObject j = new JSONObject(param);
        int size = j.keySet().size();
        Map<String,Object> paramMap = new HashMap<>();
        if(size > 0){
            j.keySet().forEach(key ->{
                paramMap.put(key,j.get(key));
            });
        }
        return joinParams(url,paramMap);
    }

    public static String httpGet(String url) throws IOException {
        return httpGet(url,null);
    }

    public static String httpGet(String url,Map<String,Object> paramMap) throws IOException {
        String urlConcat = HttpUtil.joinParams(url,paramMap);
        try(CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpGet httpGet = new HttpGet(urlConcat);
            CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
            return EntityUtils.toString(httpResponse.getEntity());
        }
    }
}
