package com.atguigu.core.exception;

/**
 * @author shkstart
 * @create 2020-03-04 23:53
 */
public class MemberException extends RuntimeException {

    public MemberException(){}
    public MemberException(String message){
        super(message);
    }
}
