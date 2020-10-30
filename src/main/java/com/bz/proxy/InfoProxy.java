package com.bz.proxy;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.http.HttpMethod;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * @author:ls
 * @date: 2020/10/27 16:55
 * 使用模板模式抽出公共部分,各组件部分交由具体实现类实现，同时在父类中提供公共部分的默认实现
 **/
@Slf4j
public abstract class InfoProxy {
    abstract String url();

    abstract String requestType();

    abstract String requestParam();

    abstract String header();

    public String getResult(){
        return requestType() == HttpMethod.GET.name().toLowerCase()?getResultByGet():getResultByPost();
    };

    public String getResultByGet(){
        String url = url();
        String param = requestParam();
        String urlConcat = HttpUtil.joinParamsWithStr(url,param);
        HttpGet httpGet = new HttpGet(urlConcat);
        if(header() != null && !header().equals(""))
            httpGet.setHeaders(HttpUtil.parseHeader(header()));
        String result = "";
        try(CloseableHttpClient httpClient = HttpClientBuilder.create().build()){
            HttpResponse httpResponse = httpClient.execute(httpGet);
            result = httpResponse.getEntity().toString();
            log.info(String.format("获取接口数据成功：%s,\n接口请求url：%s, 接口请求参数:%s",result,url,param));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String getResultByPost(){
        String param = requestParam();
        String url = url();
        HttpPost httpPost = new HttpPost(url);
        if(header() != null && !header().equals(""))
            httpPost.setHeaders(HttpUtil.parseHeader(header()));
        try {
            httpPost.setEntity(new StringEntity(param));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String result = "";
        try(CloseableHttpClient httpClient = HttpClientBuilder.create().build()){
            HttpResponse httpResponse = httpClient.execute(httpPost);
            result = httpResponse.getEntity().toString();
            log.info(String.format("获取接口数据成功：%s,\n接口请求url：%s, 接口请求参数:%s",result,url,param));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
