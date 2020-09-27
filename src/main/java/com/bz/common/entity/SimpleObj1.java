package com.bz.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author:ls
 * @date: 2020/9/27 9:56
 * 测试使用
 **/
@Data
@AllArgsConstructor
public class SimpleObj1 {

    //validation注解描述https://www.cnblogs.com/sueyyyy/p/12865578.html
    //最小为10
    @Min(10)
    private int age;

    @NotBlank(message = "名称不能为空")
    @Length(min = 3,max = 10)
    private String name;

    //可通过自定义正则校验邮件格式
    //@Email(regexp = "")
    @Email
    private String email;


    //自定义正则校验格式
    @Pattern(regexp = "(13|14|15|17|18|19)[0-9]{9}",message = "手机号码格式不正确")
    private String phone;
}
