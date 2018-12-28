package com.sinotrans.hd.microservice.api.sessioncontext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sinotrans.hd.microservice.api.config.SpringContextUtil;
import com.sinotrans.hd.microservice.api.exception.GlobalCustomizeException;
import lombok.Getter;
import lombok.Setter;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * @author DDf on 2018/12/6
 */
@Aspect
public class SessionContextAspect {
	static final String BEAN_NAME = "sessionContextAspect";
	private ObjectMapper objectMapper = new ObjectMapper();

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Getter
	@Setter
	private String sysCode;


	@Autowired
	private SpringContextUtil springContextUtil;

	/**
	 * 只拦截标注了@CurrUser注解的方法，使用方必须保证该方法能够正确返回所需要使用的用户信息
	 */
	@Pointcut("@annotation(com.sinotrans.hd.microservice.api.sessioncontext.CurrUser)")
	public void pointcut() {}

	/**
	 * 仅仅只是打印方法是否被拦截
	 * @param joinPoint
	 */
	@Before("pointcut()")
	public void before(JoinPoint joinPoint) {
		String className = joinPoint.getSignature().getDeclaringType().getName();
		String methodName = joinPoint.getSignature().getName();
		logger.info("[{}.{}]被标注为获取当前用户信息", className, methodName);
	}

	/**
	 * 在获取当前用户信息的方法成功执行并返回结果后，切面介入将结果存储到SessionContext中，这样可以让后端
	 * 直接注入{@link SessionContext}，然后从中获取用户信息而不需要重复请求，该类是基于Session的
	 * @param joinPoint
	 * @param result
	 */
	@AfterReturning(value = "pointcut()", returning = "result")
	public void after(JoinPoint joinPoint, Object result) {
		SessionContext sessionContext = springContextUtil.getApplicationContext().getBean(SessionContext.class);
		if (sessionContext != null) {
			Subject subject = SecurityUtils.getSubject();
			String reqUid;
			if (subject.getPrincipals() != null) {
				reqUid = (String) subject.getPrincipals().getPrimaryPrincipal();
			} else {
				throw new GlobalCustomizeException("用户登录信息丢失，请重新登录！");
			}
			sessionContext.setUid(reqUid);
			if (result instanceof Map) {
				sessionContext.setCurrUserMap((Map) result);
			} else if (result != null) {
				try {
					sessionContext.setCurrUserMap(objectMapper.readValue(result.toString(), Map.class));
				} catch (Exception e) {
					logger.error("将返回的用户信息转换为Map出错！");
				}
			}
			logger.info("sessionContext: {}, uid: {}, currUser: {}", sessionContext, sessionContext.getUid(),
					sessionContext.getCurrUserMap());
		} else {
			logger.info("无法获取当前用户信息.............");
		}
	}
}
