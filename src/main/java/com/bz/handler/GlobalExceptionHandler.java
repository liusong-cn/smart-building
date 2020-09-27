package com.bz.handler;

import com.bz.common.entity.Result;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

/**
 * @author:ls 自定义全局处理器
 * @date: 2020/9/27 11:39
 **/
@RestControllerAdvice("com.bz.controller")
public class GlobalExceptionHandler {

    /**
     * bind异常处理
     * @param b
     * @return
     */
    @ExceptionHandler(BindException.class)
    public Result paramValidationHandler(BindException b){
        BindingResult result = b.getBindingResult();
        List<String> msg = new ArrayList<>();
        result.getFieldErrors().stream()
                .forEach(fieldError -> msg.add(fieldError.getField()+ ":" + fieldError.getDefaultMessage()));
        return new Result(-1,msg.toString());
    }

    /**
     * 方法参数异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result paramExceptionHandler(MethodArgumentNotValidException e){
        List<String> messages = new ArrayList<>();
        e.getBindingResult().getFieldErrors()
                .stream()
                .forEach(f -> messages.add(f.getField() + ":" + f.getDefaultMessage()));
        return new Result(-1, messages.toString());
    }

    /**
     * 通用异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public Result ExceptionHandler(Exception e){
        return new Result(-1,"error");
    }
}
