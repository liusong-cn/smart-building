package com.bz.controller;

import com.bz.common.entity.Result;
import com.bz.service.SendSmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author:ls
 * @date: 2020/10/29 19:09
 * alibaba短信服务
 **/
@RestController
@RequestMapping("/sms")
@Slf4j
public class SendSmsController {

    @Autowired
    private SendSmsService sendSmsService;

    @GetMapping("sendSms")
    public Result sendSms(@RequestParam(value = "param",required = false) String param,
                          @RequestParam(value = "phoneNumbers",required = false) String phoneNumbers,
                          @RequestParam(value = "templateCode",required = false) String templateCode){
        log.info("调用发送短信服务");
        return sendSmsService.sendSms(param,phoneNumbers,templateCode);
    }
}
