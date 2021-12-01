package com.monforte.coworking.exceptions;

public class ApiErrorException extends Exception{

    public ApiErrorException(String message){
        super(message);
    }

}
