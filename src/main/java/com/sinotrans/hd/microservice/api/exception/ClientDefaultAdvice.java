package com.sinotrans.hd.microservice.api.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestClientResponseException;

/**
 * 接收服务端抛出的异常并将异常消息返回到前端展示
 *
 * @author DDf on 2018/4/20
 */
@Order(Ordered.HIGHEST_PRECEDENCE + 100)
@ControllerAdvice
@ResponseBody
public class ClientDefaultAdvice {
	private static Logger logger = LoggerFactory.getLogger(ClientDefaultAdvice.class);

	/**
	 * 接收服务端返回的异常，接收处理后返回给前端展示
	 *
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(value = RestClientResponseException.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public String exceptionHandler(RestClientResponseException ex) {
		logger.error("系统出现异常： {}", ex);
		return ex.getResponseBodyAsString();
	}

}
