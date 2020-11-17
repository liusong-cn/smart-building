package com.bz.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableField;
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
@TableName("tb_water_pressure")
public class TbWaterPressureEntity {
    @ApiModelProperty(value = "设备编码")
    @TableField("deviceCode")
    private String deviceCode;

    @ApiModelProperty(value = "采样值1")
    @TableField("sampleKPa1")
    private Float sampleKPa1;

    @ApiModelProperty(value = "采样值2")
    @TableField("sampleKPa2")
    private Float sampleKPa2;

    @ApiModelProperty(value = "采样值3")
    @TableField("sampleKPa3")
    private Float sampleKPa3;

    @ApiModelProperty(value = "采样值4")
    @TableField("sampleKPa4")
    private Float sampleKPa4;

    @ApiModelProperty(value = "采样值5")
    @TableField("sampleKPa5")
    private Float sampleKPa5;

    @ApiModelProperty(value = "采样值6")
    @TableField("sampleKPa6")
    private Float sampleKPa6;

    @ApiModelProperty(value = "采样值7")
    @TableField("sampleKPa7")
    private Float sampleKPa7;

    @ApiModelProperty(value = "采样值8")
    @TableField("sampleKPa8")
    private Float sampleKPa8;

    @ApiModelProperty(value = "采样值9")
    @TableField("sampleKPa9")
    private Float sampleKPa9;

    @ApiModelProperty(value = "采样值10")
    @TableField("sampleKPa10")
    private Float sampleKPa10;

    @ApiModelProperty(value = "采样值11")
    @TableField("sampleKPa11")
    private Float sampleKPa11;

    @ApiModelProperty(value = "采样值12")
    @TableField("sampleKPa12")
    private Float sampleKPa12;



    @ApiModelProperty(value = "采样时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @TableField("sampleTime")
    private Date sampleTime;

}
