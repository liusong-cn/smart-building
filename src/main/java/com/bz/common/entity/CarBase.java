package com.bz.common.entity;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author:ls
 * @date: 2020/11/16 19:06
 * 金时利停车场base
 **/
@Data
public class CarBase {

    //停车场标识
    private String appkey;
    //10位时戳
    private String timestamp;
    //签名
    private String sign;
    //id
    private String id;
    //卡号
    private String cardid;
    //会员id
    private String memberid;
    //会员类型
    private String membertype;
    //会员名
    private String name;
    //车牌号
    private String platenumber;
    //车牌颜色
    private String platecolor;
    //是否库中库 0-否 1-是
    private String subplace;
    //抓拍图片
    private String image;
    //备注
    private String remark;
}
