package com.bz.common.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author: ls
 * @date: 2020/12/21 20:17
 **/
@Data
@TableName("zz_car_realtime_info")
public class ZZCarRealtimeInfo {

    @TableId("id")
    private long id;

    @TableField("t4")
    private String t4;

    @TableField("nox_before")
    private String noxBefore;

    @TableField("nox_behind")
    private String noxBehind;

    @TableField("locate")
    private String locate;

    @TableField("urea_injection_volume")
    private String ureaInjectionVolume;

    @TableField("latitude")
    private String latitude;

    @TableField("urea_pump_status")
    private String ureaPumpStatus;

    @TableField("speed")
    private String speed;

    @TableField("urea_tank_heating_state")
    private String ureaTankHeatingState;

    @TableField("car_no")
    private String carNo;

    @TableField("urea_liquid_height")
    private String ureaLiquidHeight;

    @TableField("urea_solution_temperature")
    private String ureaSolutionTemperature;

    @TableField("vo")
    private String vo;

    @TableField("exhaust_flow")
    private String exhaustFlow;

    @TableField("time")
    private Date time;

    @TableField("intake")
    private String intake;

    @TableField("height")
    private String height;

    @TableField("longitude")
    private String longitude;

    @TableField("t3")
    private String t3;

    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private Date createTime;
}
