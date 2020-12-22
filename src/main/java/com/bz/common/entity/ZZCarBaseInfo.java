package com.bz.common.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author: ls
 * @date: 2020/12/22 14:41
 **/
@Data
@TableName("zz_car_base_info")
public class ZZCarBaseInfo {

    @TableId("id")
    private long id;

    @TableField("car_owner_name")
    private String carOwnerName;

    @TableField("car_type")
    private String carType;

    @TableField("car_no")
    private String carNo;

    @TableField("pro_date")
    private String proDate;

    @TableField("road")
    private String road;

    @TableField("car_owner_tel")
    private String carOwnerTel;

    @TableField("car_use")
    private String carUse;

    @TableField("vin")
    private String vin;

    @TableField("displacement")
    private String displacement;

    @TableField("car_model")
    private String carModel;

    @TableField("manufacturer")
    private String manufacturer;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof ZZCarBaseInfo){
            ZZCarBaseInfo o = (ZZCarBaseInfo) obj;
            return o.getCarNo().equals(carNo);
        }
        return false;
    }

}
