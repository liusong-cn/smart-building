package com.bz.common.vo;

import lombok.Data;

/**
 * @Author lzq
 * @Date 2020/10/14 14:18
 */
@Data
public class WarningInfo {

    Integer alarm_grade;//报警等级
    Integer alarm_status;//报警状态，1：报警产生，2：报警消失
    Integer alarm_type;//报警类型
    String alarm_type_name;//报警类型名称
    Integer alarm_type_sub;//报警类型子类
    String alarm_type_sub_name;//报警类型子类名称
    String business_content;//业务内容
    String channel_name;//通道名
    String dev_address;//报警设备地址
    Integer device_id;//设备id
    String device_name;//设备名称
    String ga_code;//通道GaCode
    String handle_message;//处理信息
    String handle_time;//处理时间
    Integer id;//报警记录id
    Integer idx;//通道号
    Integer is_alarm;//是否是设备报警，0-否，1-是,默认是0
    String org_index;//报警源所在组织code
    String pic_address;//图片地址
    String pu_id;//设备编码
    Integer report_status;//误报状态
    String report_time;//上报时间
    String sensor_id;//传感器id
    String sensor_name;//传感器名称
    String status;//报警处理状态
    String sub_type_chinese_name;//报警子类型中文名
}
