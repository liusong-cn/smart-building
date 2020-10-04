package com.bz.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;

import java.time.LocalDateTime;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author zy
 * @since 2020-10-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_electricity_data")
public class TbElectricityDataEntity {
    @ApiModelProperty(value = "设备编码")
    @TableField("deviceCode")
    private String deviceCode;

    @ApiModelProperty(value = "采样值1")
    @TableField("sampleElectricity1")
    private Float sampleElectricity1;

    @ApiModelProperty(value = "采样值2")
    @TableField("sampleElectricity2")
    private Float sampleElectricity2;

    @ApiModelProperty(value = "采样值3")
    @TableField("sampleElectricity3")
    private Float sampleElectricity3;

    @ApiModelProperty(value = "采样值4")
    @TableField("sampleElectricity4")
    private Float sampleElectricity4;

    @ApiModelProperty(value = "采样值5")
    @TableField("sampleElectricity5")
    private Float sampleElectricity5;

    @ApiModelProperty(value = "采样值6")
    @TableField("sampleElectricity6")
    private Float sampleElectricity6;

    @ApiModelProperty(value = "采样值7")
    @TableField("sampleElectricity7")
    private Float sampleElectricity7;

    @ApiModelProperty(value = "采样值8")
    @TableField("sampleElectricity8")
    private Float sampleElectricity8;

    @ApiModelProperty(value = "采样值9")
    @TableField("sampleElectricity9")
    private Float sampleElectricity9;

    @ApiModelProperty(value = "采样时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @TableField("sampleTime")
    private Date sampleTime;

}
