package com.sinotrans.hd.microservice.api.usercontext;

import com.sinotrans.hd.microservice.api.POJO.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.context.annotation.RequestScope;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * <p>如果是使用用户信息，请参考 {@link com.sinotrans.hd.microservice.api.usercontext.EnableGetUser}
 *
 * <p>使用注意：
 * <p>某些通用的{@code bean}可能会依赖于这个类，但因为该类为{@code @RequestScope}，所以目前所知，在线程任务中是无法
 * 直接使用的，关于这一点暂时未想到好的解决办法，但以下可以提供其中一种解决办法:
 * <p>使用{@link ThreadLocal}在线程任务执行前将{@code UserContext}入参
 * 如果有异步任务需要使用到这个类的方法，则需要在异步任务调用之前，在当前请求类中获得{@code UserContext}，
 * 因为还没开始异步任务，所以这里还是可以获取到的，然后使用{@link ThreadLocal}封装{@code UserContext}
 * 然后传入到异步方法中，在异步方法体中调用{@code setRequestContext()}将传入的{@code userContext}
 * 赋值给自己线程任务需要依赖{@code UserContext}的bean，当然前提是注入的{@code UserContext}，
 * 要暴漏setter方法,以下演示{@code CommonService}依赖{@code UserContext}
 * <pre class="code"><code>
 * &#064;Service
 * public class CommonService {
 *      &#064;Autowired
 *      &#064;Getter
 *      &#064;Setter
 *      private UserContext userContext;
 *
 *      public String appendCommonUpdateSql() {
 *          String userName = "SYSTEM";
 *          if (userContext != null &amp;&amp; userContext.getUid() != null) {
 *              userName = userContext.getUid();
 *          }
 *          return " VERSION = VERSION + 1, MODIFY_BY = '" + userName + "'," +
 *              " MODIFY_TIME = FUC_NUMBER2TIMESTAMP(" + System.currentTimeMillis() + ") ";
 *      }
 * }</code></pre>
 *
 * <p>正常任务类，该类需要调用另外一个异步任务类，在当前类中{@code UserContext}还是能够正常注入的</p>
 * <pre class="code">
 * &#064;Service
 * public class CurrentService {
 *      &#064;Autowired
 *      private UserContext userContext;
 *      &#064;Autowired
 *      private ThreadTaskService threadTaskService;
 *
 *      public void main() {
 *          ThreadLocal&#60;UserContext&#62; requestContextThreadLocal = new ThreadLocal&#60;&#62;();
 *          requestContextThreadLocal.set(userContext);
 *          threadTaskService.async(requestContextThreadLocal);
 *      }
 * }</pre>
 *
 * <p>异步任务类，由一个接口请求中调用，从该类开始，往下流程中的所有类都会处于异步线程中，
 * 不能够正常注入{@code UserContext}，因此可以再异步任务类开始使用传参setter的方式</p>
 * <pre class="code">
 * &#064;Service
 * public class ThreadTaskService {
 *      &#064;Autowired
 *      private CommonService commonService;
 *
 *      &#064;Async
 *      public void async(ThreadLocal&#60;UserContext&#62; requestContextThreadLocal) {
 *          commonService.setRequestContext(requestContextThreadLocal.get());
 *      }
 * }</pre>
 *
 *
 * @author DDf on 2018/10/9
 */
@Getter
@Setter
@RequestScope
public class UserContext {
    static final String BEAN_NAME = "userContext";

    /** 当前用户信息 */
    private User user;
    /** 当前用户u_id */
    private String uid;
    /** 当前用户信息Map格式 */
    private Map<String, Object> userMap = new HashMap<>();
}
