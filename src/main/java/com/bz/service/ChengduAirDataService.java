package com.bz.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.bz.common.entity.CDAirData;
import com.bz.mapper.CDAirDataMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.ConnectException;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: ls
 * @date: 2020/12/21 16:13
 **/
@Slf4j
@Service
public class ChengduAirDataService {

    @Resource
    private CDAirDataMapper airDataMapper;

    @Value("${chengdu.airdata.url}")
    private String airDataUrl;

    public void saveAirData(){
        try {
            String result = HttpUtil.get(airDataUrl);
            log.info("获取到市控站大气数据: {}", result );
            JSONObject j = new JSONObject(result);
            JSONArray array = j.getJSONArray("Head");
            List<CDAirData> airDataList = array.toList(CDAirData.class);
            for (CDAirData airData : airDataList) {
                airData.setCreateTime(Calendar.getInstance().getTime());
                airDataMapper.insert(airData);
            }
            log.info("新增大气数据成功");
        }catch (Exception e){
            log.error("获取或新增市控站大气数据失败: {}", e.getMessage());
        }



    }
}
