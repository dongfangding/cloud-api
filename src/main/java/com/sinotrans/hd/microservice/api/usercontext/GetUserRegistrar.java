package com.sinotrans.hd.microservice.api.usercontext;

import com.sinotrans.hd.microservice.api.aspect.RequestContextAspect;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;

/**
 * @author DDf on 2018/11/2
 */
public class GetUserRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        registryConfiguration(metadata, registry);
    }

    /**
     * 将{@link EnableGetUser}的属性注册到{@link GetUserConfiguration}这个类中，并将配置类放入容器
     * @param metadata metadata
     * @param registry registry
     */
    private void registryConfiguration(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        boolean exist = metadata.hasAnnotation(EnableGetUser.class.getName());
        boolean enableGetUser = false;
        boolean globalShowDetails = false;
        if (exist) {
            enableGetUser = true;
            // 拦截器默认不开启，只有开启了相关功能才注入到IOC，使之生效
            if (!registry.containsBeanDefinition(RequestContextAspect.BEAN_NAME)) {
                BeanDefinitionBuilder requestContextDefinition = BeanDefinitionBuilder.
                        genericBeanDefinition(RequestContextAspect.class);
                registry.registerBeanDefinition(RequestContextAspect.BEAN_NAME,
                        requestContextDefinition.getBeanDefinition());
            }
            Map<String, Object> defaultAttrs = metadata
                    .getAnnotationAttributes(EnableGetUser.class.getName(), true);
            if (defaultAttrs != null && !defaultAttrs.isEmpty()) {
                globalShowDetails = (boolean) defaultAttrs.get("globalShowDetails");
            }

            // registry UserContext
            registry.registerBeanDefinition(UserContext.BEAN_NAME,
                    BeanDefinitionBuilder.genericBeanDefinition(UserContext.class).getBeanDefinition());
        }

        // registry GetUserConfiguration
        BeanDefinitionBuilder definition = BeanDefinitionBuilder
                .genericBeanDefinition(GetUserConfiguration.class);
        definition.addPropertyValue("enableGetUser", enableGetUser);
        definition.addPropertyValue("globalShowDetails", globalShowDetails);
        definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
        BeanDefinitionHolder holder = new BeanDefinitionHolder(definition.getBeanDefinition(),
                GetUserConfiguration.BEAN_NAME, new String[]{GetUserConfiguration.BEAN_NAME});
        BeanDefinitionReaderUtils.registerBeanDefinition(holder, registry);
    }
}
