package com.sinotrans.hd.microservice.api.exception;

import org.springframework.core.NestedRuntimeException;

/**
 * 自定义异常类
 * @author DDF 2017年11月28日
 *
 */
public class GlobalCustomizeException extends NestedRuntimeException {
	private static final long serialVersionUID = 1L;
	private String code;
	private String message;

	public GlobalCustomizeException(String message) {
		super(message);
		this.message = message;
	}

	public GlobalCustomizeException(String code, String message) {
		super(message);
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
