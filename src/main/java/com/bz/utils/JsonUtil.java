package com.bz.utils;

import cn.hutool.json.JSONException;
import cn.hutool.json.JSONObject;
import com.bz.exception.StringEmptyException;

/**
 * @author:ls
 * @date: 2020/10/23 9:41
 **/
public class JsonUtil {

    public static JSONObject parseJson(String str) {
        if(str == null || str.length() ==0){
            throw new StringEmptyException("字符串不能为空");
        }
        if(!(str.startsWith("{") && str.endsWith("}"))){
            throw new JSONException("不满足json格式");
        }
        JSONObject jsonObject = new JSONObject(str);

        return jsonObject;
    }


}
