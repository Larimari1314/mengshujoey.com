package com.mengshujoey.mengshuadminsystem.common.config.springSecurity.handle;

import com.alibaba.fastjson.JSON;
import com.mengCommon.common.common.http.ResponseResult;
import com.mengCommon.common.common.http.StatusCode;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * application name：librarySystemPatient - AuthenticationEnryPoint
 * application describing：
 * copyright：
 * company：
 * time： 2022-12-14 16:38:20
 *
 * @author liujingwen
 * @version ver 1.0
 * @since 1.8
 */
@Component
public class AuthenticationEnryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");
        response.setStatus(401);
        response.getWriter().write(JSON.toJSONString(ResponseResult.getErrorResult("You need to log in to the system", StatusCode.UNAUTHORIZED, null)));
    }
}
