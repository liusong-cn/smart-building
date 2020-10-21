package com.bz.service;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bz.common.entity.Result;
import com.bz.common.entity.TbElectricityDataEntity;
import com.bz.common.entity.TbWaterPressureEntity;
import com.bz.mapper.TbElectricityDataMapper;
import com.bz.mapper.TbWaterPressureMapper;
import com.bz.properties.JinShiLiProperties;
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

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class JinShiLiService {

    @Resource
    private TbWaterPressureMapper tbWaterPressureMapper;

    @Resource
    private TbElectricityDataMapper tbElectricityDataMapper;

    @Autowired
    private JinShiLiProperties jinShiLiProperties;

    public Result getPressureInfo(String deviceCode){
        Result result = new Result<>();
        if(deviceCode.isEmpty()){
            putMessage(result,-1,"缺少参数");
            return result;
        }
        LambdaQueryWrapper<TbWaterPressureEntity> wrapper = new QueryWrapper<TbWaterPressureEntity>().lambda();
        wrapper.eq(TbWaterPressureEntity::getDeviceCode,deviceCode);
        wrapper.orderByDesc(TbWaterPressureEntity::getSampleTime);
        wrapper.last("limit 1");
        List<TbWaterPressureEntity> list = tbWaterPressureMapper.selectList(wrapper);
        if(list.size()<1){
            putMessage(result,1,"该设备编码无数据");
            return result;
        }
        result.setData(list);
        result.setTotal(list.size());
        putMessage(result,0,"成功");
        return result;
    }

    public Result getElectricityInfo(String deviceCode){
        Result result = new Result<>();
        if(deviceCode.isEmpty()){
            putMessage(result,-1,"缺少参数");
            return result;
        }
        LambdaQueryWrapper<TbElectricityDataEntity> wrapper = new QueryWrapper<TbElectricityDataEntity>().lambda();
        wrapper.eq(TbElectricityDataEntity::getDeviceCode,deviceCode);
        wrapper.orderByDesc(TbElectricityDataEntity::getSampleTime);
        wrapper.last("limit 1");
        List<TbElectricityDataEntity> list = tbElectricityDataMapper.selectList(wrapper);
        if(list.size()<1){
            putMessage(result,1,"该设备编码无数据");
            return result;
        }
        result.setData(list);
        result.setTotal(list.size());
        putMessage(result,0,"成功");
        return result;
    }

    public Result putMessage(Result result,Integer code,String message){
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    /**
     * 获取车辆出入场信息
     * @param channel
     * @return
     */
    public Result<List> getCarInfo(String channel){
        Result<List> r = null;
        Map<String,String> paramMap = new HashMap<>();
        paramMap.put("channel",channel);
        String url = joinParams(jinShiLiProperties.getCarInfo(),paramMap);
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        CloseableHttpResponse httpResponse = null;
        try {
            HttpGet httpGet = new HttpGet(url);
            httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            String result = EntityUtils.toString(httpEntity);
            log.info("车辆出入场信息:" + result);
            r = replaceIp(format(result, new ArrayList<>()));
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

    private String joinParams(String url, Map<String,String> paramMap){
        if(paramMap == null || paramMap.size() == 0){
            return url;
        }
        StringBuffer sb = new StringBuffer();
        sb.append(url + "?");
        for (String key:paramMap.keySet()) {
            String value = paramMap.get(key);
            sb.append(key +"=" + value + "&");
        }

        String s = sb.substring(0,sb.length() - 1);
        //处理参数中yyyy-mm-dd hh:mm:ss日期格式中空格问题
        s = s.replaceAll(" ","+");
        return s;
    }

    private <E> Result<E> format(String s, E type){
        JSONObject j = new JSONObject(s);
        Result<E> result = new Result<E>();
        result.setCode(j.getInt("code"));
        result.setTotal(j.getInt("total"));
        result.setMessage(j.getStr("message"));
        E data = (E) j.get("data");
        result.setData(data);
        return result;
    }

    /**
     * 获取道闸信息
     * @param channel
     * @return
     */
    public Result<List> getBarrierGateInfo(String channel){
        Result<List> r = null;
        Map<String,String> paramMap = new HashMap<>();
        paramMap.put("channel",channel);
        String url = joinParams(jinShiLiProperties.getBarrierGateInfo(),paramMap);
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        CloseableHttpResponse httpResponse = null;
        try {
            HttpGet httpGet = new HttpGet(url);
            httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            String result = EntityUtils.toString(httpEntity);
            log.info("道闸信息:" +result);
            r = format(result,new ArrayList<>());
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

    private Result<List> replaceIp(Result<List> r) {
        List<Map<String, String>> dataList = r.getData();
        dataList.forEach(item -> {
            String imageUrl = item.get("image");
            String newImageUrl = imageUrl.replace("172.24.131.57", "47.108.29.69");
            item.put("image", newImageUrl);
        });
        return r;
    }
}
