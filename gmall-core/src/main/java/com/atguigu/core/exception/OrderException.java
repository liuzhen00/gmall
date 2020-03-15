package com.atguigu.core.exception;

/**
 * @author shkstart
 * @create 2020-03-10 17:54
 */
public class OrderException extends RuntimeException {

    public OrderException(){}

    public OrderException(String message){
        super(message);
    }
}
