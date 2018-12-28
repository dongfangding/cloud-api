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

	@Bean
	@LoadBalanced
	@Primary
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
	/**
	 * 对于某些非Eureka服务的请求，可以去除@LoadBalanced注解
	 * @return
	 */
	@Bean
	public RestTemplate urlFactoryRestTemplate() {
		SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
		requestFactory.setConnectTimeout(3000);
		requestFactory.setReadTimeout(3000);
		return new RestTemplate(requestFactory);
	}
	
	/**
	 * 使用json来序列化来操作redis的缓存存取，默认使用的jdk的序列化和反序列化
	 * @param redisConnectionFactory
	 * @return
	 */
	@Bean
	@Primary
	public RedisTemplate<Object, Object> redisTemplate(
			RedisConnectionFactory redisConnectionFactory){
		RedisTemplate<Object, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(redisConnectionFactory);
		template.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());
		return template;
	}
	
	
	@Bean
	@Primary
	public ObjectMapper objectMapper() {
		return new ObjectMapper();
	}
	
	/**
	 * 默认线程池
	 * @return
	 */
	@Bean
	@Primary
	public Executor taskExecutor() {
		ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
		threadPoolTaskExecutor.setThreadNamePrefix(DEFAULT_TASK_EXECUTOR);
		threadPoolTaskExecutor.setCorePoolSize(8);
		threadPoolTaskExecutor.setMaxPoolSize(50);
		threadPoolTaskExecutor.setKeepAliveSeconds(0);
		threadPoolTaskExecutor.setQueueCapacity(1000);
		return threadPoolTaskExecutor;
	}
	
	
	/**
	 * 定时任务调度线程池
	 * @return
	 */
	@Bean
	@Primary
	public Executor scheduledExecutorService() {
		ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
				.setNameFormat("scheduledExecutorService-%d").build();
		ScheduledThreadPoolExecutor scheduledExecutorService = new ScheduledThreadPoolExecutor(8,
				namedThreadFactory);
		scheduledExecutorService.setMaximumPoolSize(50);
		return scheduledExecutorService;
	}


}
