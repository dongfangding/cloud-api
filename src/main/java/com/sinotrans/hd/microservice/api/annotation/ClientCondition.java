package com.sinotrans.hd.microservice.api.annotation;

import com.sinotrans.microService.controller.BaseControllerInterface;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;

import java.lang.annotation.*;

/**
 *
 * <p>目前是个空注解，主要引用{@code @ConditionalOnBean(BaseControllerInterface.class)}，用来判断当前项目是否
 * 存在这个Class,将该逻辑应用于当前注解，用来作为识别当前项目是否是客户端的条件之一；
 * </p>
 *
 * @author DDf on 2018/11/29
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ConditionalOnClass(BaseControllerInterface.class)
public @interface ClientCondition {
}
