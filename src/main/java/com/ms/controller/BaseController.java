package com.ms.controller;

import com.ms.error.BusinessException;
import com.ms.error.EmBusinessError;
import com.ms.response.CommomRetrunType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class BaseController {

    public static final String CONTENT_TYPE_FORMED = "application/x-www-form-urlencoded";

//    /**
//     * 定义exception handler解决未被controller层吸收的exception
//     * @param request
//     * @param e
//     * @return
//     */
//    @ExceptionHandler(Exception.class)
//    @ResponseStatus(HttpStatus.OK)
//    @ResponseBody
//    public Object handlerException(HttpServletRequest request, Exception e){
//        Map<String,Object> responseMap = new HashMap<>();
//        if (e instanceof BusinessException){
//            BusinessException businessException = (BusinessException)e;
//            responseMap.put("errorCode",businessException.getErrorCode());
//            responseMap.put("errorMsg",businessException.getErrorMsg());
//        }else{
//            responseMap.put("errorCode", EmBusinessError.UNKNOWN_ERROR.getErrorCode());
//            responseMap.put("errorMsg",EmBusinessError.UNKNOWN_ERROR.getErrorMsg());
//        }
//        return CommomRetrunType.create(responseMap,"fail");
//    }
}
