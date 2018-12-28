package com.sinotrans.hd.microservice.api.usercontext;

import java.lang.annotation.*;

/**
 *
 * 在开启了{@link EnableGetUser}功能后，则默认拦截指定控制器的类然后获得当前的U_ID，则将u_id放入到
 * {@link com.sinotrans.hd.microservice.api.usercontext.UserContext}对象中，而如果想要获得用户的完成信息，
 * 则需要在控制器类的具体方法中加入该注解，否则就要使用{@code EnableGetUser(globalShowDetails=true)}的属性，但这个
 * 控制器里的每个方法都可以获取用户使用，要自己斟酌是否需要
 *
 * @author DDf on 2018/10/23
 * @see EnableGetUser
 * @see com.sinotrans.hd.microservice.api.aspect.RequestContextAspect
 * @see com.sinotrans.hd.microservice.api.usercontext.UserContext
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GetUser {
}
