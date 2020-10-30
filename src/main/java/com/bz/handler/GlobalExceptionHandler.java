package com.bz.handler;

import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.bz.common.entity.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author:ls 自定义全局处理器
 * @date: 2020/9/27 11:39
 **/
@Slf4j
@RestControllerAdvice("com.bz.controller")
public class GlobalExceptionHandler {

    /**
     * bind异常处理
     * @param b
     * @return
     */
    @ExceptionHandler(BindException.class)
    public Result paramValidationHandler(BindException b){
        log.info(b.getMessage(),b);
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
        log.info(e.getMessage(),e);
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
        log.info(e.getMessage(),e);
        return new Result(-1,"error");
    }

    /**
     * 方法缺少参数处理
     * @param e
     * @return
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Result missingParamExceptionHandler(MissingServletRequestParameterException e){
        log.info(e.getMessage(),e);
        return new Result(-1,e.getMessage());
    }

    @ExceptionHandler(UnsupportedEncodingException.class)
    public Result unsupportedEncodingHandler(UnsupportedEncodingException e){
        log.error("编码不支持:"+e.getMessage());
        return new Result(-1,"编码转换异常");
    }

    /**
     * ali短信服务端异常
     * @param e
     * @return
     */
    @ExceptionHandler(ServerException.class)
    public Result serverExceptionHandler(ServerException e){
        log.error("ali短信服务端异常" + e.getMessage());
        return new Result(-1,e.getMessage());
    }

    /**
     * ali短信服务客户端异常
     * @param e
     * @return
     */
    @ExceptionHandler(ClientException.class)
    public Result clientExceptionHandler(ClientException e){
        log.error("ali短信客户端端异常" + e.getMessage());
        return new Result(-1,e.getMessage());
    }

    /**
     * 运行异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(RuntimeException.class)
    public Result runtimeExceptionHandler(RuntimeException e){
        log.error(String.format("系统运行异常:%s",e.getMessage()));
        return new Result(-1,e.getMessage());
    }
}
