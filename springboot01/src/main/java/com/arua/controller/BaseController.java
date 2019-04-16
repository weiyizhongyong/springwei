package com.arua.controller;

import com.arua.error.BusinessException;
import com.arua.error.EmBusinessError;
import com.arua.response.CommonReturnType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class BaseController {
    public static final  String CONTENT_TYPE_FORMED="application/x-www-form-urlencoded";//"application/json;charset=UTF-8"
    //"application/json;charset=UTF-8" 是接受JSON数据，"application/x-www-form-urlencoded" 是接受字符串数据
    //定义exceptionhandler解决未被controller层接收的Exception
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object handlerException(HttpServletResponse request, Exception ex){
        Map<String,Object> map = new HashMap<>();
        if(ex instanceof BusinessException){
            BusinessException bu = (BusinessException)ex;

            map.put("errCode",bu.getErrCode());
            map.put("errMsg",bu.getErrMsg());

        }else {

            map.put("errCode", EmBusinessError.UNKNOWN_ERROR.getErrCode());
            map.put("errMsg",EmBusinessError.UNKNOWN_ERROR.getErrMsg());

        }

        return CommonReturnType.create(map,"fail");
    }
}
