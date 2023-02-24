package com.mengshujoey.mengshuadminsystem.common.config.springSecurity.handle;

import com.alibaba.fastjson.JSON;
import com.mengCommon.common.common.http.ResponseResult;
import com.mengCommon.common.common.http.StatusCode;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>application name：librarySystemPatient - LoginFailHandle</p>
 * <p>application describing： </p>
 * <p>copyright： </p>
 * <p>company： </p>
 * <p>time：2022-12-14 16:11:56</p>
 *
 * @author liujingwen
 * @version ver 1.0
 * @since 1.8
 */
@Component
public class LoginFailHandle implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        response.setStatus(500);
        response.getWriter().write(JSON.toJSONString(ResponseResult.getErrorResult("Login failed, possibly because your password or username was entered incorrectly", StatusCode.ERROR, null)));
    }
}
