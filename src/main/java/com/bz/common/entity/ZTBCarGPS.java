package com.bz.common.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author: ls
 * @date: 2020/12/24 15:14
 **/
@Data
@TableName("ztb_car_gps")
public class ZTBCarGPS {

    @TableId("id")
    private long id;

    @TableField("plate_no")
    private String plateNo;

    @TableField("lng")
    private String lng;

    @TableField("time")
    private long time;

    @TableField("lat")
    private String lat;

    @TableField("speed")
    private double speed;

    @TableField("direction")
    private double direction;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

}
