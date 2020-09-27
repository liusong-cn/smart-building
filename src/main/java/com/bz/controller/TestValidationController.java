package com.bz.controller;

import com.bz.common.entity.R;
import com.bz.common.entity.Result;
import com.bz.common.entity.SimpleObj;
import com.bz.common.entity.SimpleObj1;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author:ls
 * @date: 2020/9/27 10:01
 **/
@RestController
public class TestValidationController {

    /**
     * post方式传参并在entity中校验,该方式需要自行处理binding异常，不太方便
     * @param obj
     * @param bindingResult
     * @return
     */
    @PostMapping("/simple")
    public Result<String> postValidation(@Valid @RequestBody SimpleObj obj, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            String messages = bindingResult.getAllErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .reduce((m1, m2) -> m1 + "；" + m2)
                    .orElse("参数输入有误！");
            //这里可以抛出自定义异常,或者进行其他操作
            return new Result<String>(-1,messages);
        }
        return new Result<String>(R.SUCCESS,"good");
    }

    /**
     * 结合全局异常处理
     * @param obj
     * @return
     */
    @PostMapping("/simple1")
    public Result<String> postCheck(@RequestBody@Valid SimpleObj1 obj){
        return new Result<String>(R.SUCCESS,"good");
    }
}
