package com.bz.common.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author: ls
 * @date: 2020/12/24 13:54
 **/
@Data
@TableName("ztb_car_base_info")
public class ZTBCarBaseInfo {

    @TableId("id")
    private long id;

    @TableField("lng")
    private String lng;

    @TableField("plate_no")
    private String plateNo;

    @TableField("time")
    private long time;


    @TableField("is_online")
    private String is_online;

    @TableField("lat")
    private String lat;

    @TableField("speed")
    private double speed;

    @TableField("height")
    private double height;

    @TableField("direction")
    private double direction;

    @TableField("asset_type")
    private String assetType;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

}
