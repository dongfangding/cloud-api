package com.sinotrans.hd.microservice.api.config;

import com.sinotrans.hd.microservice.api.exception.ClientDefaultAdvice;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 *
 * 移除原框架包中对服务端和客户端对异常的默认处理类，原处理对异常处理不是很友好；
 * 且优先级别为最高，本地覆盖存在一定变数，因此直接移除，并使用全新的类来代替处理
 *
 * @author DDf on 2018/11/29
 */
public class RemoveOldDefaultAdvice implements ImportBeanDefinitionRegistrar {
	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
		if (registry.containsBeanDefinition("clientDefultAdvice")) {
			registry.removeBeanDefinition("clientDefultAdvice");
		}
		if (registry.containsBeanDefinition("dataDefaultAdvice")) {
			registry.removeBeanDefinition("dataDefaultAdvice");
		}

		registry.registerBeanDefinition("clientDefaultAdvice", BeanDefinitionBuilder.genericBeanDefinition(
				ClientDefaultAdvice.class).getBeanDefinition());
	}
}
