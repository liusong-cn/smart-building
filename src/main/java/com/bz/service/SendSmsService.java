package com.bz.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.bz.common.entity.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author:ls
 * @date: 2020/10/29 19:12
 **/
@Service
@Slf4j
public class SendSmsService {

    public Result sendSms(String param, String phoneNumbers, String templateCode){

        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", "LTAI4FiKSw1iDQS324qfp4mU", "i8Z59eTYEXVcfFVlt2i9lnwIb6aVwx");
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        List<String> legalPhoneNums = new ArrayList<>();
        if(!StrUtil.isBlank(phoneNumbers)){
            List<String> phoneNums = Arrays.asList(phoneNumbers.split(","));
            String pattern = "^1{1}\\d{10}$";
            legalPhoneNums = phoneNums.stream().filter((phoneNum)->{
                return phoneNum.matches(pattern);
            }).collect(Collectors.toList());
        }
        request.putQueryParameter("PhoneNumbers", legalPhoneNums.size()>0?StrUtil.join(",",legalPhoneNums):"18349220243");
        request.putQueryParameter("SignName", "倍智智能数据运营有限公司");
        request.putQueryParameter("TemplateCode", StrUtil.isBlank(templateCode)?"SMS_205075510":templateCode);
        String paramJson = String.format("{\"code\":\"%s\"}",StrUtil.isBlank(param)?"abc":param);
        request.putQueryParameter("TemplateParam", paramJson);
        try {
            CommonResponse response = client.getCommonResponse(request);
            JSONObject j = new JSONObject(response.getData());
            if(j.get("Code").toString().equalsIgnoreCase("ok")){
                log.info(String.format("发送短信成功，接收到返回：%s", response.getData()));
                return new Result(0,"发送短信成功");
            }

        } catch (ServerException e) {
            e.printStackTrace();
            log.error(String.format("发送短信失败，ali短信服务端异常：%s",e.getMessage()));
            return new Result(-1,String.format("发送短信失败,ali服务端异常;%s",e.getMessage()));
        } catch (ClientException e) {
            e.printStackTrace();
            log.error(String.format("发送短信失败，客户端发送格式异常：%s",e.getMessage()));
            return new Result(-1,String.format("发送短信失败,客户端发送格式异常;%s",e.getMessage()));
        }
        return null;
    }


}
