package com.bz.proxy;

/**
 * @author:ls
 * @date: 2020/10/27 17:06
 * 基础代理实现方式
 **/
public class BasicInfoProxy extends InfoProxy {
    //目标：通过注入一个service，该service提供interfaceid，通过id进行相应的url、requestType等获取
    //后期考虑对http获取到的结果进行redis缓存，从而降低请求次数


    @Override
    public String url() {
        return null;
    }

    @Override
    public String requestType() {
        return null;
    }

    @Override
    public String requestParam() {
        return null;
    }

    @Override
    public String header() {
        return null;
    }
}
