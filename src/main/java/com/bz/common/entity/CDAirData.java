package com.bz.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author: ls
 * @date: 2020/12/21 16:32
 **/
@Data
@TableName("cd_airdata")
public class CDAirData {

    @TableField("id")
    private long id;

    @TableField("MN")
    private String MN;

    @TableField("O3_1H")
    private String O3_1H;

    @TableField("IPM10_24H")
    private String IPM10_24H;

    @TableField("PM10_24H_MV")
    private String PM10_24H_MV;

    @TableField("ICO_1H")
    private String ICO_1H;

    @TableField("PM25_1H")
    private String PM25_1H;

    @TableField("PRIMARY_POLLUTANTS")
    private String PRIMARY_POLLUTANTS;

    @TableField("IO3_1H")
    private String IO3_1H;

    @TableField("PointName")
    private String PointName;

    @TableField("PM25_24H_MV")
    private String PM25_24H_MV;

    @TableField("NO2_1H")
    private String NO2_1H;

    @TableField("SO2_1H")
    private String SO2_1H;

    @TableField("ISO2_1H")
    private String ISO2_1H;

    @TableField("O3_8H_MV")
    private String O3_8H_MV;

    @TableField("VideoSystemMN")
    private String VideoSystemMN;

    @TableField("Rec_Time")
    private String Rec_Time;

    @TableField("PointArea")
    private String PointArea;

    @TableField("IO3_8H_MV")
    private String IO3_8H_MV;

    @TableField("IPM25_24H")
    private String IPM25_24H;

    @TableField("PM10_1H")
    private String PM10_1H;

    @TableField("INO2_1H")
    private String INO2_1H;

    @TableField("AQI_1H")
    private String AQI_1H;

    @TableField("CO_1H")
    private String CO_1H;

    @TableField("create_time")
    private Date createTime;

}
