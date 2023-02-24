package com.mengshujoey.mengshusystemgateway.config.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class TokenFilter implements GlobalFilter, Ordered {

    private static final List<String> jumpOverPath = new ArrayList<>();

    static {
        Collections.addAll(jumpOverPath,
                "/joeyAdminSystem/loginAdmin/login",  //用户登录
                "/joeyAdminSystem/adminLogin/obtainPrivateKey",//登录获取公钥信息
                "/joeyAdminSystem/evaluation/uploadCoverAddress",//封面上传地址
                "/joeyAdminSystem/evaluation/uploadEvaluationAddress",//测评图片上传地址
                "/joeyAdminSystem/evaluation/uploadProjectAddress"//项目图片上传地址
        );
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        synchronized (this) {
            ServerHttpResponse response = exchange.getResponse();
            ServerHttpRequest request = exchange.getRequest();
            AtomicBoolean pathBoolean = new AtomicBoolean(false);
            String pathToken = request.getURI().getPath();
            jumpOverPath.forEach(j -> {
                if (j.equals(pathToken)) {
                    pathBoolean.compareAndSet(false, true);
                }
            });
            if (pathBoolean.get()) {
                return chain.filter(exchange);
            }
            return chain.filter(exchange);
        }
    }

    //过滤器排序作用，返回的数值越小越先执行
    @Override
    public int getOrder() {
        return 2;
    }
}
