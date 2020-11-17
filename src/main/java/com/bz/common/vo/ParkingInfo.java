package com.bz.common.vo;

import lombok.Data;

/**
 * @author:ls
 * @date: 2020/11/16 16:54
 **/
@Data
public class ParkingInfo {

    private Integer code;

    private String msg;

    private Integer takeover;

    private Integer opendoor;

    private String[] led;

    private String sound;

    private boolean isfree;

    public static ParkingInfo success(){
        return builder().setCode(0).setMsg("成功").setTakeover(0);
    }

    public static ParkingInfo error(){
        return builder().setCode(-1).setTakeover(0).setMsg("失败");
    }

    public static ParkingInfo builder(){
        return new ParkingInfo();
    }

    public ParkingInfo setCode(Integer code){
        this.code = code;
        return this;
    }

    public ParkingInfo setMsg(String msg){
        this.msg = msg;
        return this;
    }

    public ParkingInfo setTakeover(Integer takeover){
        this.takeover = takeover;
        return this;
    }

    public ParkingInfo setOpendoor(Integer opendoor){
        this.opendoor = opendoor;
        return this;
    }

    public ParkingInfo setLed(String[] led){
        this.led = led;
        return this;
    }

    public ParkingInfo setSound(String sound){
        this.sound = sound;
        return this;
    }

    public ParkingInfo setIsfree(boolean isfree){
        this.isfree = isfree;
        return this;
    }

}
