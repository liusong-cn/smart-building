package com.bz.common.entity;

import lombok.Data;

/**
 * @author:ls
 * @date: 2020/11/16 19:13
 **/
@Data
public class CarOut extends CarBase {

    //停车时长(分)
    private String _long;
    //停车费
    private String money;
    //出场通道
    private String leavechannel;
    //出场时间
    private String leavetime;
}
