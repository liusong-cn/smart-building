package com.bz.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.bz.common.entity.Result;
import com.bz.config.AliyunSmsConfigProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author:ls
 * @date: 2020/10/29 19:12
 **/
@Service
@Slf4j
public class SendSmsService {

    @Resource
    private AliyunSmsConfigProperties aliyunSmsConfigProperties;


    public Result sendSms(String param, String phoneNumbers, String templateCode) {


        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", aliyunSmsConfigProperties.getAccessKeyId(), aliyunSmsConfigProperties.getAccessKeySecret());
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        if (StrUtil.isBlank(phoneNumbers)) {
            return new Result(-1, "提供的手机号无效");
        }

        String validPhoneNumbers = Stream.of(phoneNumbers.split("[,，]]"))
                .map(StrUtil::trim)
                .filter(item -> item.matches("^1\\d{10}$"))
                .collect(Collectors.joining(","));

        if (StrUtil.isBlank(validPhoneNumbers)) {
            log.info("短信发送失败. {}", phoneNumbers);
            return new Result(-1, "提供的手机号无效");
        }
        request.putQueryParameter("PhoneNumbers", validPhoneNumbers);
        request.putQueryParameter("SignName", aliyunSmsConfigProperties.getSign());
        request.putQueryParameter("TemplateCode", StrUtil.isBlank(templateCode) ? aliyunSmsConfigProperties.getDefaultTemplateCode() : templateCode);
        request.putQueryParameter("TemplateParam", param);

        try {
            CommonResponse response = client.getCommonResponse(request);
            JSONObject responseData = new JSONObject(response.getData());
            if (responseData.get("Code").toString().equalsIgnoreCase("ok")) {
                log.info(String.format("发送短信成功，接收到返回：%s", response.getData()));
                return new Result(0, "发送短信成功");
            } else {
                log.info("短信发送失败，{}", responseData);
                return new Result(-1, responseData.getStr("Message"));
            }

        } catch (ServerException e) {
            e.printStackTrace();
            log.error(String.format("发送短信失败，ali短信服务端异常：%s", e.getMessage()));
            return new Result(-1, String.format("发送短信失败,ali服务端异常;%s", e.getMessage()));
        } catch (ClientException e) {
            e.printStackTrace();
            log.error(String.format("发送短信失败，客户端发送格式异常：%s", e.getMessage()));
            return new Result(-1, String.format("发送短信失败,客户端发送格式异常;%s", e.getMessage()));
        }
    }


}
