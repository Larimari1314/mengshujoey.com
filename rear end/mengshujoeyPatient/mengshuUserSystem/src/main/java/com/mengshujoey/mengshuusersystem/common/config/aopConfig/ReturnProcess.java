package com.mengshujoey.mengshuusersystem.common.config.aopConfig;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * application name:mengshujoeyPatient - ReturnProcess
 * application describing: 返回值处理类，需要对标注此注解的类返回值进行处理
 * copyright:
 * company:
 * time:2023-03-12 12:14:03
 *
 * @author liujingwen
 * @version ver 1.0
 * @since 1.8
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ReturnProcess {
}
