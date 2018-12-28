package com.sinotrans.hd.microservice.api.sessioncontext;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 *
 * 在当前结构中，该功能只能在客户端使用！！！！！！！
 * 服务端有提供类似功能，请参考{@link com.sinotrans.hd.microservice.api.usercontext.EnableGetUser}
 *
 * <p>是否启用基于session作用域的用户信息后端保存，该功能开启后，还需配合{@link CurrUser}注解在系统获取用户信息的方法上，
 * 这样一旦调用获取用户信息的方法，该方法的返回值便会被放入到{@link SessionContext}中，避免该功能重复请求</p>
 *
 * <p><strong>注意事项：</strong></p>
 *  *  在线程任务中无法直接注入该对象，使用会报错；因为一旦一个主请求结束，随之{@code Session}作用域会不能存在，因此
 *  *  需要使用方在主任务还未结束线程任务未开始之前，将该对象传入线程任务中，方可正常使用；
 *
 *  <p>使用方式: </p>
 *  <p>在配置中开启功能</p>
 *  <pre class="code">
 *  &#064;Configuration
 *  &#064;EnableSessionContext
 *  public class Application {
 *
 *  }
 *  </pre>
 *
 *  <p>标识获取用户信息的方法，方法是你自己的，你需要的只是在自己获取用户信息的方法上加上注解{@link CurrUser},
 *  ,controller和service最好只标注一个地方）</p>
 *  <pre class="code">
 *  &#064;RestController
 *  public class UserController {
 *      &#064;RequestMapping("/currUser")
 *      &#064;CurrUser
 *      public Map&#60;String, Object&#62; getCurrUser() {
 *          // return 当前用户信息
 *      }
 *  }
 *  </pre>
 *
 *  <p>接下来就可以直接使用了，每次在服务器重启的时候该信息会被重置，如果客户端自己的session并没有刷新重置，
 *  界面也没有刷新，没有重新获取用户信息，那么继续使用就会出现问题，那么刷新下界面重新获取用户信息即可正常使用；</p>
 *
 *
 * @see CurrUser
 * @see SessionContextRegistry
 * @see SessionContextAspect
 * @see SessionContext
 * @author DDf on 2018/11/29
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(SessionContextRegistry.class)
public @interface EnableSessionContext {


}
