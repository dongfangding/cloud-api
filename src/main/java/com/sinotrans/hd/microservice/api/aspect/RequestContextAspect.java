package com.sinotrans.hd.microservice.api.aspect;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sinotrans.hd.microservice.api.POJO.User;
import com.sinotrans.hd.microservice.api.exception.GlobalCustomizeException;
import com.sinotrans.hd.microservice.api.feign.org.UserFeignService;
import com.sinotrans.hd.microservice.api.log.LogAspectConfiguration;
import com.sinotrans.hd.microservice.api.usercontext.GetUser;
import com.sinotrans.hd.microservice.api.usercontext.GetUserConfiguration;
import com.sinotrans.hd.microservice.api.usercontext.UserContext;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.util.*;

/**
 *
 * 拦截指定的请求为某些注解功能提供支持，目前支持功能如下
 * <ul>
 *     <li>{@link com.sinotrans.hd.microservice.api.usercontext.EnableGetUser}</li>
 *     <li>{@link com.sinotrans.hd.microservice.api.log.EnableLogAspect}</li>
 * </ul>
 * 
 * @author DDf on 2018/10/9
 */
@Aspect
@ConfigurationProperties(prefix = "RequestContextAspect")
public class RequestContextAspect {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public static final String BEAN_NAME = "requestContextAspect";
    private ObjectMapper objectMapper = new ObjectMapper();

    /** 内部配置类 */
    @Getter
    @Setter
    private RequestContextConfig config = new RequestContextConfig();

    @Autowired(required = false)
    private UserContext userContext;
    @Autowired
    private UserFeignService userFeignService;
    @Autowired(required = false)
    private GetUserConfiguration getUserConfiguration;
    @Autowired
    private LogAspectConfiguration logAspectConfiguration;

    @Pointcut(value = "execution(public * com.sinotrans.hd..controller..*(..))")
    public void pointCut() {}

    /**
     * before处理日志和封装用户信息
     * @param joinPoint
     */
    @Before("pointCut()")
    public void before(JoinPoint joinPoint) {
        if (checkIgnore(joinPoint)) {
            return;
        }
        logBefore(joinPoint);
        handlerUserContext(joinPoint);
    }

    /**
     * 请求成功执行并返回值后后打印日志
     * @param joinPoint
     * @param result
     */
    @AfterReturning(pointcut = "pointCut()", returning = "result")
    public void afterReturning(JoinPoint joinPoint, Object result) {
        if (checkIgnore(joinPoint)) {
            return;
        }
        logAfter(joinPoint, result, "方法执行结束，成功返回值");
    }

    /**
     * 请求出现异常打印日志
     * @param joinPoint
     * @param exception
     */
    @AfterThrowing(pointcut = "pointCut()", throwing = "exception")
    public void afterThrowing(JoinPoint joinPoint, Exception exception) {
        if (checkIgnore(joinPoint)) {
            return;
        }
        logAfter(joinPoint, exception, "方法执行出现异常");
    }

    /**
     * 判断当前被拦截的类是否需要跳过
     * @param joinPoint {@link JoinPoint}
     */
    private boolean checkIgnore(JoinPoint joinPoint) {
        Set<String> ignoreClass;
        if (config.getIgnoreClass() == null) {
            ignoreClass = new HashSet<>();
        } else {
            ignoreClass = config.getIgnoreClass();
        }
        ignoreClass.add("com.sinotrans.hd.microService.controller.BaseController");
        String className = joinPoint.getSignature().getDeclaringType().getName();
        for (String aClass : ignoreClass) {
            if (aClass.equals(className) || className.startsWith(aClass)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 记录方法入参
     * @param joinPoint {@link JoinPoint}
     */
    private void logBefore(JoinPoint joinPoint) {
        if (!logAspectConfiguration.isEnableLogAspect()) {
            return;
        }
        Map<String, Object> paramsMap = new HashMap<>();
        String className = joinPoint.getSignature().getDeclaringType().getName();
        String methodName = joinPoint.getSignature().getName();
        String[] parameterNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
        String str = "";
        if (parameterNames.length > 0) {
            for (int i = 0; i < parameterNames.length; i++) {
                String value = joinPoint.getArgs()[i] != null ? joinPoint.getArgs()[i].toString() : "null";
                paramsMap.put(parameterNames[i], value);
            }
            try {
                str = objectMapper.writeValueAsString(paramsMap);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        logger.info("[{}.{}方法执行，参数列表===>({})]", className, methodName, str);
    }

    private void logAfter(JoinPoint joinPoint, Object result, String message) {
        if (!logAspectConfiguration.isEnableLogAspect()) {
            return;
        }
        String className = joinPoint.getSignature().getDeclaringType().getName();
        String methodName = joinPoint.getSignature().getName();
        logger.info("[{}.{}]{}..........: ({})", className, methodName, message, result != null ? result.toString() : "");
    }

    /**
     * 处理当前请求中的用户U_ID，并判断当前请求是否需要获取全部用户信息，如果需要将信息封装到{@link UserContext}中
     * @param joinPoint {@link JoinPoint}
     */
    private void handlerUserContext(JoinPoint joinPoint) {
        if (getUserConfiguration == null || !getUserConfiguration.isEnableGetUser()) {
            return;
        }
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        String reqUid = request.getHeader("REQ_UID");
        logger.info("HEADER REQ_UID: {}", reqUid);
        userContext.setUid(reqUid);
        logger.info("uid: {}", userContext.getUid());
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Annotation getUser = methodSignature.getMethod().getAnnotation(GetUser.class);
        if (getUser != null || getUserConfiguration.isGlobalShowDetails()) {
            if (config.getSysCode() == null) {
                throw new GlobalCustomizeException("如需使用详细功能，请确认是否已配置当前项目的系统代码？如有疑问，请查看该类获得帮助？" +
                        "com.sinotrans.hd.microservice.api.usercontext.EnableGetUser");
            }
            if (reqUid != null) {
                try {
                    Map<String, Object> map = userFeignService.queryUserPermissionList(reqUid, config.getSysCode(), false);
                    String s = objectMapper.writeValueAsString(map);
                    User user = objectMapper.readValue(s, User.class);
                    userContext.setUserMap(map);
                    userContext.setUid(reqUid);
                    userContext.setUser(user);
                    logger.info("uid: {}, user: {}", userContext.getUid(), userContext.getUser());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                logger.info("user: (null)");
                userContext.setUid(null);
                userContext.setUser(null);
            }
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    private class RequestContextConfig {
        /**
         * 忽略处理的class全类名，也可以使用包名
         */
        private Set<String> ignoreClass;

        /**
         * 系统代码
         */
        private String sysCode;
    }
}
