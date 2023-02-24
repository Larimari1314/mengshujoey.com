package com.mengshujoey.mengshuadminsystem.common.config.springSecurity.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mengCommon.common.config.RSAEncrypt;
import com.mengshujoey.mengshuadminsystem.backend.service.impl.TimedTasksServiceImpl;
import com.mengshujoey.mengshuadminsystem.common.config.springSecurity.handle.LoginFailHandle;
import com.mengshujoey.mengshuadminsystem.common.config.springSecurity.handle.LoginSuccessHandle;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * <p>application name：librarySystemPatient - AdminUsernamePasswordAuthenticationFilter</p>
 * <p>application describing： </p>
 * <p>copyright： </p>
 * <p>company： </p>
 * <p>time：2022-12-15 08:53:13</p>
 *
 * @author liujingwen
 * @version ver 1.0
 * @since 1.8
 */
public class AdminUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private RedisTemplate redisTemplate;

    public AdminUsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager, LoginSuccessHandle loginSuccessHandle, LoginFailHandle loginFailHandle, RedisTemplate redisTemplate) {
        //只允许post请求,不是post请求也能经过filter
        this.setPostOnly(false);
        //设置登陆的路径和请求方式
        this.setAuthenticationManager(authenticationManager);
        this.setAuthenticationSuccessHandler(loginSuccessHandle);
        this.setAuthenticationFailureHandler(loginFailHandle);
        this.redisTemplate = redisTemplate;
        this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/loginAdmin/login"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        lb:
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        } else if (request.getContentType().equals(MediaType.APPLICATION_JSON_UTF8_VALUE) || request.getContentType().equals(MediaType.APPLICATION_JSON_VALUE)) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                //前端传来加密数据，后端进行解析
                //从redis中获取公钥
                String privateKey = null;
                synchronized (this) {
                    privateKey = (String) redisTemplate.boundHashOps(TimedTasksServiceImpl.REDIS_ENCRYPTED_DATA).get(TimedTasksServiceImpl.REDIS_PRIVATE_KEY_STRING);
                }
                //正常情况下基本都不会为空，但是不排除，当用户端正在装配公钥的时候，管理员登录
                if (privateKey == null)
                    break lb;
                Map<String, String> map = objectMapper.readValue(RSAEncrypt.decrypt((String) objectMapper.readValue(request.getInputStream(), Map.class).get("LoginCredentials"), privateKey), Map.class);
                String username = map.get("loginEmail");
                username = username != null ? username.trim() : "";
                String password = map.get("password");
                password = password != null ? password.trim() : "";
                UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken.unauthenticated(username, password);
                setDetails(request, authRequest);
                return getAuthenticationManager().authenticate(authRequest);
            } catch (Exception e) {
                throw new AuthenticationServiceException("The username or password is incorrect");
            }
        }
        throw new AuthenticationServiceException("User input error");
    }
}
