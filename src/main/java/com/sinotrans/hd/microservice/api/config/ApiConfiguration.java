package com.sinotrans.hd.microservice.api.config;

import feign.Request;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.*;

/**
 * 当前模块的主要配置类，将该模块提供的某些默认的模块在这里生效
 *
 * @author DDf on 2018/10/17
 */
@Configuration
@EnableAspectJAutoProxy
@EnableFeignClients(basePackages = "com.sinotrans.hd.microservice.api.feign")
@Import(value = {RemoveOldDefaultAdvice.class})
public class ApiConfiguration {


    /**
     * 为Feign设置默认的connectTimeoutMillis和readTimeoutMillis属性，优先级低于配置文件，如引用方
     * 不想使用默认的配置，可以自行在项目中配置
     * @return options
     */
    @Bean
    public Request.Options options() {
        return new Request.Options();
    }




}
