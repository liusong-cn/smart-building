package com.bz.common.entity;

import lombok.Getter;

/**
 * @author:ls
 * @date: 2020/9/27 9:21
 **/
@Getter
public enum R {

    SUCCESS(0,"成功"),
    FAILURE(-1,"失败");


    private int code;

    private String msg;

    R(int code, String msg){
        this.code = code;
        this.msg = msg;
    }

}
