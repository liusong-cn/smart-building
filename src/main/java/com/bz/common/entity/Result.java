package com.bz.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author:ls
 * @date: 2020/9/27 9:29
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<V> implements Serializable {

    private static final long serialVersionUID = 2l;

    private int code;

    private String message;

    private int total;

    private V data;

    //通过枚举和自定义data返回result
    public Result(R r, V data,int total){
        code = r.getCode();
        message = r.getMsg();
        this.total = total;
        this.data = data;
    }

    //通过枚举定义返回
    public Result(R r){
        code = r.getCode();
        message = r.getMsg();
    }

    //允许自定义响应码和消息
    public Result(int code, String message){
        this.code = code;
        this.message = message;
    }

    //成功返回带数据
    public  static <V> Result<V> ok(R r, V data,int total){
        return new Result<V>(r.SUCCESS,data,total);
    }

    //失败默认返回
    public static Result error(R r){
        return new Result(r.FAILURE);
    }


}
