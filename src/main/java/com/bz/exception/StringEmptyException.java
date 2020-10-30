package com.bz.exception;

/**
 * @author:ls
 * @date: 2020/10/23 9:47
 **/
public class StringEmptyException extends RuntimeException {
    public StringEmptyException(String message) {
        super(message);
    }
}
