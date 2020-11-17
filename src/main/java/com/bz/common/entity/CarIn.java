package com.bz.common.entity;

import lombok.Data;

/**
 * @author:ls
 * @date: 2020/11/16 19:11
 * 车辆入场信息
 **/
@Data
public class CarIn extends CarBase {

    //入场通道
    private String enterchannel;
    //入场时间
    private String entertime;
}
