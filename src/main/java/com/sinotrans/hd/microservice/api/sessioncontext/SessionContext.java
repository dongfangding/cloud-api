package com.sinotrans.hd.microservice.api.sessioncontext;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.context.annotation.SessionScope;

import java.util.Map;

/**
 *
 * <p>用来将当前用户信息存储在基于{@link SessionScope}作用域的对象中，
 *  客户端可以直接在注入该对象使用；</p>
 *  <p><strong>注意事项：</strong></p>
 *  在线程任务中无法直接注入该对象，使用会报错；因为一旦一个主请求结束，随之{@code Session}作用域会不能存在，因此
 *  需要使用方在主任务还未结束线程任务未开始之前，将该对象传入线程任务中，方可正常使用；
 *
 * @see EnableSessionContext
 * @see CurrUser
 * @see SessionContextRegistry
 * @see SessionContextAspect
 * @author DDf on 2018/8/6
 */
@SessionScope
public class SessionContext {
	static final String BEAN_NAME = "sessionContext";

	@Getter
	@Setter
	private String uid;

	@Getter
	@Setter
	private Map<String, Object> currUserMap;
}
