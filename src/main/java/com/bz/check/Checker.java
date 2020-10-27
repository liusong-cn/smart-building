package com.bz.check;

/**
 * @author:ls
 * @date: 2020/10/23 10:40
 **/
@FunctionalInterface
public interface Checker {
    /**
     * 对obj进行验证
     * @param obj
     * @return
     */
    boolean check(Object obj);
}
