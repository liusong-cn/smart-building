package com.bz.check;

import cn.hutool.json.JSONObject;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author:ls
 * @date: 2020/10/23 10:42
 **/
public class JsonChecker implements Checker {
    @Override
    public boolean check(Object obj) {
        if(obj instanceof JSONObject){
            JSONObject j = (JSONObject) obj;
            final Set<Map.Entry<String, Object>> entries = j.entrySet();
            Set<String> keys = new HashSet<>() ;
            entries.stream().forEach(o ->{
                keys.add(o.getKey());
            });
            Set<String> keysNeeded = new HashSet<>(Arrays.asList("data","total","code","message"));
            return keys.containsAll(keysNeeded);
        }
        return false;
    }
}
