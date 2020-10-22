package com.bz.common.vo;

import lombok.Data;

/**
 * @Author lzq
 * @Date 2020/10/14 14:18
 */
@Data
public class WarningInfo {

    String alarm_grade = "";//报警等级
    String alarm_status= "";//报警状态，1：报警产生，2：报警消失
    String alarm_type= "";//报警类型
    String alarm_type_name= "";//报警类型名称
    String alarm_type_sub= "";//报警类型子类
    String alarm_type_sub_name= "";//报警类型子类名称
    String business_content= "";//业务内容
    String channel_name= "";//通道名
    String dev_address= "";//报警设备地址
    String device_id= "";//设备id
    String device_name= "";//设备名称
    String ga_code= "";//通道GaCode
    String handle_message= "";//处理信息
    String handle_time= "";//处理时间
    String id= "";//报警记录id
    String idx= "";//通道号
    String is_alarm= "";//是否是设备报警，0-否，1-是,默认是0
    String org_index= "";//报警源所在组织code
    String pic_address= "";//图片地址
    String pu_id= "";//设备编码
    String report_status= "";//误报状态
    String report_time= "";//上报时间
    String sensor_id= "";//传感器id
    String sensor_name= "";//传感器名称
    String status= "";//报警处理状态
    String sub_type_chinese_name= "";//报警子类型中文名
    String biz_map="";//查询结果说明:
    String orn_date="";// 生日
    String gender_code="";// 性别
    String ethic_code="";// 民族
    String target_feature="";// 布控目标
    String disposition_name="";// 布控任务
    String name="";// 姓名
    String similarity="";// 相似度
    String id_type="";// 证件种类
    String id_number="";// 证件号码
    String biz1 = "";//生日
    String biz2 = "";//性别 0 未知的性别 1 男性 2 女性 9 未说明的性别
    String biz3 = "";//民族
    String biz4 = "";//布控目标
    String biz5 = "";//布控任务
    String biz6 = "";//姓名
    String biz7 = "";//相似度
    String biz8 = "";//证件种类
    String biz9 = "";//证件号码
    String biz10 = "";//
    String biz11= "";//名单库
    String biz12 = "";//人脸id
    String biz13 = "";//省份

    String part_picture= "";
    String target_picture="";



}
