package com.bz.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author:ls
 * @date: 2020/9/27 10:03
 **/
@Data
@AllArgsConstructor
public class SimpleObj {

    @NotBlank(message = "名称不能为空")
    private String s;

    //数字类型不能使用notblank
    @Min(value = 10,message = "参数p小于指定值10")
    private int p;
}
