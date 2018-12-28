package com.sinotrans.hd.microservice.api.sessioncontext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 *
 * 用来处理一旦开启{@link EnableSessionContext}，则为整个功能注册必要的组件
 * @author DDf on 2018/12/4
 */
public class SessionContextRegistry implements ImportBeanDefinitionRegistrar {
	private Logger logger = LoggerFactory.getLogger(SessionContextRegistry.class);

	@Override
	public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
		logger.info("SessionContextRegistry.........................");
		boolean enableSessionContext = metadata.hasAnnotation(EnableSessionContext.class.getName());
		if (enableSessionContext) {

			// 如果开启了EnableSessionContext的功能，则相关组件需要注入到IOC容器中
			registry.registerBeanDefinition(SessionContext.BEAN_NAME,
					BeanDefinitionBuilder.genericBeanDefinition(SessionContext.class).getBeanDefinition());

			registry.registerBeanDefinition(SessionContextAspect.BEAN_NAME,
					BeanDefinitionBuilder.genericBeanDefinition(SessionContextAspect.class).getBeanDefinition());

		}
	}
}
