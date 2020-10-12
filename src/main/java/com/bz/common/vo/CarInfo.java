package com.bz.common.vo;

import lombok.Data;

/**
 * @author:ls
 * @date: 2020/10/12 13:32
 **/
@Data
public class CarInfo {

    String plateNo;

    String lng;

    String lat;

    String assetType;

    String speed;

    String is_online;

    String height;

    //以下由中自车辆基本信息提供
    String carModel = "";

    String carOwnerName = "";

    String carOwnerTel = "";

    String proDate = "";

    String manufacturer = "";

    String carUse = "";

    String displacement = "";

    String vin = "";

    String road = "";

    //以下由中自车辆实时信息提供
    String t3 = "";

    String t4 = "";

    String vo = "";

    String intake = "";

    String noxBefore = "";

    String noxBehind = "";

    String ureaPumpStatus = "";

    String ureaInjectionVolume = "";

    String ureaLiquidHeight = "";

    String ureaSolutionTemperature = "";

    String ureaTankHeatingState = "";

    String exhaustFlow = "";

    String locate = "";

}
