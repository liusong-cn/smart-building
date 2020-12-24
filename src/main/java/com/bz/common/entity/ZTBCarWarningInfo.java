package com.bz.common.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author: ls
 * @date: 2020/12/22 16:54
 **/
@Data
@TableName("ztb_car_warning_info")
public class ZTBCarWarningInfo {

    @TableId("id")
    private long id;

    @TableField("load_status")
    private String loadStatus;

    @TableField("load_img")
    private String loadImg;

    @TableField("load_time")
    private Date loadTime;

    @TableField("load_lng")
    private String loadLng;

    @TableField("load_lat")
    private String loadLat;


    @TableField("plate_no")
    private String plateNo;

    @TableField("pb_img")
    private String pbImg;

    @TableField("pb_time")
    private Date pbTime;

    @TableField("pb_status")
    private String pbStatus;

    @TableField("material_type")
    private String materialType;

    @TableField("material_time")
    private Date materialTime;

    @TableField("material_img")
    private String materialImg;

    @TableField("overspeed")
    private String overspeed;

    @TableField("last_speed")
    private String lastSpeed;

    @TableField("last_lat")
    private String lastLat;

    @TableField("last_lng")
    private String lastLng;

    @TableField("overspeed_time")
    private Date overspeedTime;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;
















}
