package com.sinotrans.hd.microservice.api.exception;

import com.sinotrans.hd.microService.exception.DataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.DefaultErrorAttributes;
import org.springframework.web.context.request.RequestAttributes;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

/**
 * 对默认返回的异常消息做定制,
 * 这里使用@ConditionalOnMissingBean作为一个默认处理，
 * 如果使用方不使用，可以定义自己的，那么这里的就会失效
 *
 * @author DDf on 2018/4/20
 */
@ConditionalOnMissingBean
public class ErrorAttributes extends DefaultErrorAttributes {
    private Logger logger = LoggerFactory.getLogger("exception");
    @Override
    public Map<String, Object> getErrorAttributes(RequestAttributes requestAttributes, boolean includeStackTracee) {
        Map<String, Object> errorAttributes = super.getErrorAttributes(requestAttributes, includeStackTracee);
        Throwable error = getError(requestAttributes);
        if (error == null) {
            return errorAttributes;
        }
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        error.printStackTrace(pw);
        logger.error(sw.toString());
        if (error instanceof GlobalCustomizeException) {
            errorAttributes.put("code", ((GlobalCustomizeException) error).getCode());
            errorAttributes.put("message", error.getMessage());
        } else if (error instanceof DataException) {
            errorAttributes.put("message", error.getMessage());
        } else {
            error.printStackTrace();
            errorAttributes.put("code", "SYSTEM_ERROR");
            errorAttributes.put("message", "系统异常！");
            // 这里作为一个补充返回，可以在前端开发时更方便看到异常栈信息与接口开发者交流
            errorAttributes.put("details", sw.toString());
        }
        return errorAttributes;
    }
}
