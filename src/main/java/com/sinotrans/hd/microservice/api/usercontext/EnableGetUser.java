package com.sinotrans.hd.microservice.api.usercontext;

import com.sinotrans.hd.microservice.api.aspect.RequestContextAspect;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 *
 *  <p>该功能在当前架构中只能在服务端使用！！！！！！如果需要在客户端使用，有提供类似功能请参考{@link com.sinotrans.hd.microservice.api.sessioncontext.EnableSessionContext}</p>
 *
 *
 * <p>将客户端传入的U_ID到服务端解析后直接封装用户的信息，以便服务端可以直接使用对象注入使用用户信息</p>
 * <p>使用步骤：</p>
 * <p>自己项目的配置类上使用注解{@code @EnableGetUser}, 该注解实际上就是将{@link GetUserRegistrar}注入到容器中，
 *    然后动态注册与该功能相关的Bean,并且使切面类生效,这一步功能开启后，则项目可以直接注入{@link UserContext},
 *    然后通过{@link UserContext#getUid()} 获取到用户u_id</p>
 *
 * <pre class="code">
 * &#064;Configuration
 * &#064;EnableGetUser
 * public class Application {
 *
 * }
 *
 * &#064;Service
 * public class HelloService {
 *     &#064;Autowired
 *     private UserContext userContext;
 *
 *     public String sayHello() {
 *         return "hello " + userContext.getUid();
 *     }
 * }</pre>
 *
 * <p>默认开启功能后只能简单的获取用户的u_id信息，如果需要获取到用户的详细信息，则需要自行决定哪个方法需要获取用户的详细
 * 信息，则再控制器的方法上，注意一定是{@code @Controller}或{@code @RestController}
 * 注解上的类的具体方法上加入注入{@code @GetUser}即可</p>
 *
 * <pre class="code">
 * &#064;Service
 * public class HelloService {
 *     &#064;Autowired
 *     private UserContext userContext;
 *
 *     public String sayHello() {
 *         // 用户信息对象
 *         User user = userContext.getUser();
 *         // 用户信息Map形式
 *         Map&#60;String, Object&#62; userMap = userContext.getUserMap();
 *     }
 * }
 *
 * &#064;Controller
 * public class HelloController {
 *      &#064;Autowired
 *      HelloService helloService;
 *
 *      &#064;RequestMapping("/hello")
 *      &#064;GetUser
 *      public String sayHello() {
 *          helloService.sayHello();
 *      }
 * }
 * </pre>
 *
 * <p>{@code EnableGetUser}注解还有一个属性{@code globalShowDetails}， 作为对{@link GetUser}的全局性替换，即如果
 * 不再细究哪些方法需要用户全部信息，而哪些不需要用户全部信息，则可以全局设置，所有的方法都获取用户全部信息以备用，
 * 则可以修改配置类如下</p>
 * <pre class="code">
 * &#064;Configuration
 * &#064;EnableGetUser(globalShowDetails=true)
 * public class Application {
 *
 * }
 *     </pre>
 *
 * <b>对获取全部用户信息做一条配置补充，因为用户的权限等信息在每个系统各有不同，因此需要具体当前项目的系统代码，在项目
 * 的配置文件{@code .yml}中配置提供两个属性,其中{@code ignoreClass}属性可以配置包或类的全路径，标识指定类或包下的类不会被拦截
 * 默认已经配置了{code com.sinotrans.hd.microService.controller.BaseController}</b>
 *
 * <pre class="code">
 *   RequestContextAspect:
 *     config:
 *       sysCode: ORG
 *       ignoreClass:
 *         - com.sinotrans.hd.microService.controller.BaseController
 *         - xxx
 * </pre>
 *
 * <b>需要注意的是，无论是否获取具体用户信息，{@code EnableGetUser}提供的默认功能都是可以直接获取用户u_id信息的</b>
 *
 * @author DDF
 * @since 2018/10/18
 * @see RequestContextAspect
 * @see UserContext
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(value = {GetUserRegistrar.class})
public @interface EnableGetUser {
    /**
     * 是否全局使用用户的全部信息，如果为true,则只需要在配置类上{@code @EnableGetUser(globalShowDetails=true)}即所有
     * 允许被拦截的请求都能够获取到用户的全部信息
     */
    boolean globalShowDetails() default false;
}
