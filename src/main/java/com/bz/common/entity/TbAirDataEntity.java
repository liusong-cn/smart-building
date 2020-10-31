package com.bz.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author
 * @since 2020-10-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_air_data")
public class TbAirDataEntity {

    @ApiModelProperty(value = "工地id")
    @TableField("wsid")
    private String wsid;

    @ApiModelProperty(value = "设备id")
    @TableField("stationId")
    private String stationId;

    @ApiModelProperty(value = "设备时间")
    @TableField("createTime")
    private String createTime;

    @ApiModelProperty(value = "氮氧化物（μg/m³）")
    @TableField("noc")
    private String noc;

    @ApiModelProperty(value = "PM2.5（μg/m³）")
    @TableField("pm25")
    private String pm25;

    @ApiModelProperty(value = "PM10（μg/m³）")
    @TableField("pm10")
    private String pm10;

    @ApiModelProperty(value = "大气温度（℃）")
    @TableField("temperature")
    private String temperature;

    @ApiModelProperty(value = "空气湿度（%RH）")
    @TableField("humidity")
    private String humidity;

    @ApiModelProperty(value = "风速（m/s）")
    @TableField("windSpeed")
    private String windSpeed;

    @ApiModelProperty(value = "风向（°）")
    @TableField("windDirection")
    private String windDirection;

    @ApiModelProperty(value = "二氧化氮（ug/m3）")
    @TableField("no2")
    private String no2;

    @ApiModelProperty(value = "大气压力")
    @TableField("pressure")
    private String pressure;

    @ApiModelProperty(value = "一氧化碳")
    @TableField("co")
    private String co;

    @ApiModelProperty(value = "臭氧")
    @TableField("o3")
    private String o3;

    @ApiModelProperty(value = "三氧化硫")
    @TableField("so3")
    private String so3;

    @ApiModelProperty(value = "二氧化硫")
    @TableField("so2")
    private String so2;

    @ApiModelProperty(value = "aqi")
    @TableField("aqi")
    private String aqi;

    @ApiModelProperty(value = "primaryPollutants")
    @TableField("primary_pollutants")
    private String primaryPollutants;


}
