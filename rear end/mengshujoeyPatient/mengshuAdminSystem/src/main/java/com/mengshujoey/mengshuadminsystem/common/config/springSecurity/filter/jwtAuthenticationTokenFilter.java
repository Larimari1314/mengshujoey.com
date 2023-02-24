package com.mengshujoey.mengshuadminsystem.common.config.springSecurity.filter;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.mengCommon.common.utils.JwtUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;

/**
 * application name：librarySystemPatient - jwtAuthenticationTokenFilter
 * application describing：
 * copyright：
 * company：
 * time：2022-12-16 09:27:15
 *
 * @author liujingwen
 * @version ver 1.0
 * @since 1.8
 */
public class jwtAuthenticationTokenFilter extends BasicAuthenticationFilter {
    private final RedisTemplate redisTemplate;

    public jwtAuthenticationTokenFilter(AuthenticationManager authenticationManager, RedisTemplate redisTemplate) {
        super(authenticationManager);
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("token");
        if (token != null) {
            String username = JwtUtils.getUsernameByToken(token);
            Collection<LinkedHashMap<String, String>> authorities = (Collection<LinkedHashMap<String, String>>) redisTemplate.opsForValue().get(username);
            Collection<GrantedAuthority> authorityCollection = new ArrayList<>();
            if (!CollectionUtils.isEmpty(authorities)) {
                for (LinkedHashMap<String, String> permissionValue : authorities) {
                    SimpleGrantedAuthority authority = new SimpleGrantedAuthority(permissionValue.get("authority"));
                    authorityCollection.add(authority);
                }
            }
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(username, token, authorityCollection));
        }
        filterChain.doFilter(request, response);
    }
}
