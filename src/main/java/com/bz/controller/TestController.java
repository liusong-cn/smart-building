package com.bz.controller;

import cn.hutool.json.JSONObject;
import com.bz.common.entity.R;
import com.bz.common.entity.Result;
import com.bz.common.entity.SimpleObj1;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author:ls
 * @date: 2020/9/24 17:27
 **/
@RestController
@Slf4j
public class TestController {

    @GetMapping("/hello")
    public String hello(){
        log.info("你好");
        log.warn("warn");
        log.error("error");
        return "hello, world";
    }

    @GetMapping("/path")
    public JSONObject path(HttpServletRequest req){
        String contextPath = req.getContextPath();
        String servletPath = req.getServletPath();
        String pathInfo = req.getPathInfo();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("contextPath",contextPath);
        jsonObject.put("servletPath",servletPath);
        jsonObject.put("pathInfo",pathInfo);
        return jsonObject;
    }

    //测试以string形式返回
    @GetMapping("/res")
    public Result<String> responseTest(){
        //成功返回带数据
        Result<String> r = new Result<>(R.SUCCESS,"good",1);
        return r;
    }

    //测试返回list
    @GetMapping("/list")
    public Result<List> responseWithList(){
        List<SimpleObj1> simpleObj1s = new ArrayList<>();
        simpleObj1s.add(new SimpleObj1(13,"zhangsan","114@qq.com","13158392932"));
        simpleObj1s.add(new SimpleObj1(14,"lisi","114@qq.com","13158392932"));
        Result<List> r1 = new Result<>(R.SUCCESS, simpleObj1s,2);
        return r1;
    }


}
