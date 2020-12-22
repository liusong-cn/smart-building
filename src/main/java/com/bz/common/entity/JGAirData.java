package com.bz.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author:ls
 * @date: 2020/11/12 11:48
 * 聚光大气数据
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("jg_air_data")
public class JGAirData {

    @TableField("O3")
    @ApiModelProperty(value = "臭氧")
    private String O3;

    @TableField("CO")
    @ApiModelProperty(value = "一氧化碳")
    private String  CO;

    @TableField("NO2")
    @ApiModelProperty(value = "二氧化氮")
    private String NO2;

    @TableField("SO2")
    @ApiModelProperty(value = "二氧化硫")
    private String SO2;

    @TableField("AQI")
    @ApiModelProperty(value = "AQI")
    private String AQI;

    @TableField("WD")
    @ApiModelProperty(value = "风向")
    private String WD;

    @ApiModelProperty(value = "风速")
    @TableField("WS")
    private String WS;

    @ApiModelProperty(value = "PM10")
    @TableField("PM10")
    private String PM10;

    @ApiModelProperty(value = "PM25")
    @TableField("PM25")
    private String PM25;

    @ApiModelProperty(value = "RH")
    @TableField("RH")
    private String RH;

    @ApiModelProperty(value = "温度")
    @TableField("TEMP")
    private String TEMP;

    @ApiModelProperty(value = "纬度")
    @TableField("Latitude")
    private String Latitude;

    @ApiModelProperty(value = "经度")
    @TableField("Longitude")
    private String Longitude;

    @TableField("StationCode")
    @ApiModelProperty(value = "站点")
    private String StationCode;

    @TableField("PointName")
    @ApiModelProperty(value = "地点")
    private String PointName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "采集时间")
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("time")
    private Date time;

    @ApiModelProperty(value = "TVOC")
    @TableField("TVOC")
    private String TVOC;

//    @TableField("PRE")
//    @ApiModelProperty(value = "气压")
//    private String PRE;
}
