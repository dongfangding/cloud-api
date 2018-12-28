package com.sinotrans.hd.microservice.api.sessioncontext;

import java.lang.annotation.*;

/**
 *
 * 用该注解声明的方法会被解析成获取当前用户信息的方法，
 * 一旦该方法调用，返回值会被用作SessionContext的值
 *
 * 前提是必须开启@EnableSessionContex功能
 *
 * @see EnableSessionContext
 * @see SessionContextRegistry
 * @see SessionContextAspect
 * @see SessionContext
 * @author DDf on 2018/12/6
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CurrUser {
}
