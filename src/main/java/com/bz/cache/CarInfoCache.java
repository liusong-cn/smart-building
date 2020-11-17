package com.bz.cache;

import lombok.Data;

/**
 * @author:ls
 * @date: 2020/11/16 20:04
 * 金时利车辆出入场车牌缓存
 **/
public class CarInfoCache {

    //入场车牌
    private static String plateNoIn = "";

    //出厂车牌
    private  static String plateNoOut = "";

    public static void StorePlateNoIn(String plateNoIn){
        CarInfoCache.plateNoIn = plateNoIn;
    }

    public static void StorePlateNoOut(String plateNoOut){
        CarInfoCache.plateNoOut = plateNoOut;
    }

    public static String getPlateNoIn(){
        return plateNoIn;
    }

    public static String getPlateNoOut(){
        return plateNoOut;
    }

}
