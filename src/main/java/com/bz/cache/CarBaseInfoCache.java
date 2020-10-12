package com.bz.cache;

import com.bz.common.vo.CarBaseInfo;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author:ls
 * @date: 2020/10/12 14:46
 * 中自车辆基本信息缓存
 **/
public class CarBaseInfoCache {

    private static List<CarBaseInfo> carBaseInfoCache = new ArrayList<>();

    public static void setCache(List<CarBaseInfo> carBaseInfoList){
        carBaseInfoCache = carBaseInfoList;
    }

    public static List<CarBaseInfo> getCache(){
        return carBaseInfoCache;
    }
}
