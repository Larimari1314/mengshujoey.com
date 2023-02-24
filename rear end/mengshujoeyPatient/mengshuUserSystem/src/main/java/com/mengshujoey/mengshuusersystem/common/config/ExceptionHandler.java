package com.mengshujoey.mengshuusersystem.common.config;

import com.alibaba.fastjson.JSON;
import com.mengCommon.common.common.http.ResponseResult;
import com.mengCommon.common.common.http.StatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

/**
 * application describing:
 * copyright:
 * company:
 * time:2023-01-14
 *
 * @author liujingwen
 * @version ver 1.0
 * @since 1.8
 */
@ControllerAdvice
@Component
public class ExceptionHandler {

    @ResponseStatus(code = HttpStatus.NOT_IMPLEMENTED)
    @org.springframework.web.bind.annotation.ExceptionHandler({MethodArgumentNotValidException.class, MissingServletRequestParameterException.class})
    @ResponseBody
    public String methodArgumentNotValidException(HttpServletRequest request, HttpServletResponse response, MethodArgumentNotValidException e) {
        //封装需要返回的错误信息
        Map<String, String> errorMsg = new HashMap<>(64);
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            errorMsg.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return JSON.toJSONString(ResponseResult.getErrorResult(StatusCode.NOT_IMPLEMENTED, "501", errorMsg));
    }

    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    @ResponseBody
    public String exceptionHandler(Exception e) {
        System.out.println(getExceptionAllInfo(e));
//        emailSending(e);
        return JSON.toJSONString(ResponseResult.getErrorResult(StatusCode.ERROR, "500", e.getMessage()));
    }


    public String getExceptionAllInfo(Exception ex) {
        ByteArrayOutputStream out = null;
        PrintStream pout = null;
        String ret = "";
        try {
            out = new ByteArrayOutputStream();
            pout = new PrintStream(out);
            ex.printStackTrace(pout);
            ret = new String(out.toByteArray());
            out.close();
        } catch (Exception e) {
            return ex.getMessage();
        } finally {
            if (pout != null) {
                pout.close();
            }
        }
        return ret;
    }
}
