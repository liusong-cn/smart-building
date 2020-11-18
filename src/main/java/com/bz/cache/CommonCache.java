package com.bz.cache;

import java.util.HashMap;
import java.util.Map;

/**
 * @author:ls
 * @date: 2020/11/18 9:46
 * 通用缓存单例类
 * 使用静态内部类做到延迟加载和线程安全
 **/
public class CommonCache {
    static {
        System.out.println("外部类初始化");
    }

    private Map<String,Object> cache;

    private CommonCache(Map<String,Object> cache){
        this.cache = cache;
    }
//     用于测试外部类初始化是否加载内部类
//    public CommonCache(){
//    }

    //保证put线程安全,正常情况下是偏向锁，不会有性能消耗
    //建议value为字符串，没有使用redis数据类型支持
    public synchronized void put(String key,Object value){
        cache.put(key,value);
    }

    public Object get(String key){
        return cache.get(key);
    }

    //外部类初始化时不会立即加载该静态内部类
    private static class CacheHolder{
        static {
            System.out.println("静态内部类初始化");
        }
        private static CommonCache commonCache = new CommonCache(new HashMap<>());
    }

    public static CommonCache getInstance(){
        return CacheHolder.commonCache;
    }

//    public static void main(String[] args) {
//        CommonCache.getInstance();
//    }

}
