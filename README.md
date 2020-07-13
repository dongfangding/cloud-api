笔记外链： http://581c88a2.wiz03.com/share/s/1o78yy200k0w27GvK52Rf_O_3dG3N51ZjAnh2tLOBq0REp1I 密码：4165

[TOC]

## 一、 简介  
### 1. 设想  
因目前开发模块越来越多，有很多在一个项目中已经定义的方法，再开发另外一个系统的时候，总是需要再开发复制一份，如果是针对公共接口的时候，很可能原接口进行了修改，过多的业务系统都要重新进行修改，所以设想开发一个独立的模块，该模块对所有公共的方法，远程调用`api`进行统一封装，别的模块引入这个模块即可直接使用，同时也可以对该模块进行修改。

### 2. 存在问题  
该模块在`pom.xml`中引入之后，需要自行前往`svn`将项目拉取下来，然后使用`maven install`命令，安装到本地使用，这样方便如果对源码进行修改，可以更方便的即使调试，后续如果使用成熟后，会再放到私服中

## 二、开发与使用  
### 1. 项目地址  


### 2. 使用前提  
#### 2.1 检索项目并本地安装  
将项目检索到本地后，导入该项目自身所需依赖，然后执行`install`命令，将该项目安装到本地仓库中
![](index_files/a37e4b32-2d7c-413a-b751-42d6440e4471.png)

#### 2.2 添加依赖  
将该项目的`GAV`坐标信息添加到自己的项目中  
```xml
    <dependency>
        <groupId>com.sinotrans.hd.commonApi</groupId>
            <artifactId>microservice-api</artifactId>
            <version>1.0-SNAPSHOT</version>
    </dependency>
```

## 三、提供功能  
###  1. 公共接口调用  
> 将组织架构、公共服务等各个微服务模块提供的通用功能以接口的方式剥离出来，调用方只需要将接口注入，通过接口的方式调用即可。即使服务方内部修改，而不需要自己在项目中更改；就算服务的参数进行修改，则只需要将这里的接口修改即可。不需要每个调用方自己定义自己修改

**实现方式:**  
> Feign  
通过Feign来完成服务接口的调用功能


** 使用方式:**  
将需要使用的接口使用`Autowired`注解注入即可使用  
```java
@Service
public class TestService {
    @Autowired
    private UserFeignService userFeignService;

    public void test() {
        Map<String, Object> map = userFeignService.queryUserPermissionList(reqUid, sysCode);
    }
}
```

** 实现说明：**  
接口使用的前提在通用项目`jar`包的`com.sinotrans.hd.microservice.api.config.ApiConfiguration`类里已经配置，该类目前代码如下，其中`EnableFeignClients`与本功能有关，指定与`Feign`相关的接口所在包路径，另外`options()`方法，默认向容器中注入了`Request.Options`对象来配置当前`Feign`接口调用的默认超时时间，因`Feign`框架已对容器中注入了一个默认的`Request.Options`对象,但它使用了`@ConditionalOnMissingBean`注解来保证外部程序自定义注入的正常使用，但该默认配置后来因为`ribbon`的加入导致了属性被重置为`ribbion`的超时时间`1000ms`，因此这里需要重新注入一个对象，而且也不能再使用`@ConditionalOnMissingBean`，否则不清楚两个顺序的优先级如何抉择；所以如果使用该`jar`包不需要下面的默认时间`connectTimeoutMillis=10 * 1000, readTimeoutMillis=60 * 1000`，则只能通过配置文件来覆盖；  
```java
@Configuration
@EnableAspectJAutoProxy
@EnableFeignClients(basePackages = "com.sinotrans.hd.microservice.api.feign")
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
```

配置文件的覆盖写法,优先级为配置类注入 < ribbon < feign.default < feign.自己的服务名下的配置  
```
ribbon:
  ReadTimeout: 20000
  ConnectTimeout: 5000  
feign:
  client:
    config:
      default: ## 服务名，默认，如果为default，说明所有的服务都应用该配置，可以被自定义的覆盖
        readTimeout: 3333
        connectTimeout: 4444
      myFeign:   #Feign接口对应的服务名,默认，可以覆盖default
        readTimeout: 5555
        connectTimeout: 6666
```

### 2. 服务端获取客户端的请求并识别其中的登陆用户信息  
> 说明：
获得客户端请求头携带的用户U_ID然后将U_ID信息封装到对象中，同时可选根据U_ID获得用户的登录信息，将登录信息（包含用户、部门、公司、权限、菜单、操作公司等）同时封装到对象中，使用时直接注入对象即可

#### 2.1 基本使用  
默认该功能不启用，如果需要使用，请参考以下步骤说明：  
自己项目的配置类上使用注解`@EnableGetUser`, 然后项目可以直接注入`com.sinotrans.hd.microservice.api.usercontext.UserContext`,然后通过`UserContext#getUid()`获取到用户u_id
```java
 @Configuration
 @EnableGetUser
 public class Application {

 }

 @Service
 public class HelloService {
     @Autowired
     private UserContext userContext;

     public String sayHello() {
         return "hello " + userContext.getUid();
     }
 }
```
默认开启功能后只能简单的获取用户的u_id信息，如果需要获取到用户的详细信息，则需要自行决定哪个方法需要获取用户的详细 信息，则再控制器的方法上，注意一定是`@Controller`或`@RestController`注解上的类的具体方法上加入注入`@GetUser`即可，另外还需要配置系统代码，请参考该节最后说明  
```java
@Service
 public class HelloService {
     @Autowired
     private UserContext userContext;

     public String sayHello() {
         // 用户信息对象
         User user = userContext.getUser();
         // 用户信息Map形式
         Map<String, Object> userMap = userContext.getUserMap();
     }
 }

 @Controller
 public class HelloController {
      @Autowired
      HelloService helloService;

      @RequestMapping("/hello")
      @GetUser
      public String sayHello() {
          helloService.sayHello();
      }
 }
```
`EnableGetUser`注解还有一个属性globalShowDetails， 作为对`GetUser`的全局性替换，即如果 不再细究哪些方法需要用户全部信息，而哪些不需要用户全部信息，则可以全局设置，所有的方法都获取用户全部信息以备用， 则可以修改配置类如下，，另外还需要配置系统代码，请参考该节最后说明  
```java
@Configuration
 @EnableGetUser(globalShowDetails=true)
 public class Application {

 }
```
**注意:**  
对获取全部用户信息做一条配置补充，因为用户的权限等信息在每个系统各有不同，因此需要具体当前项目的系统代码，在项目 的配置文件.yml中配置提供两个属性,其中`sysCode`代表当前项目的 系统代码，`ignoreClass`属性可以配置包或类的全路径，标识指定类或包下的类不会被拦截 默认已经配置了`com.sinotrans.hd.microService.controller.BaseController`  
配置如下：  
```
RequestContextAspect:
  config: 
    sysCode: ORG
    ignoreClass: com.sinotrans.hd.microService.controller.BaseController,xxx,yyy
```
**无论是否获取具体用户信息，EnableGetUser提供的默认功能都是可以直接获取用户u_id信息的**  

#### 2.2 异步任务使用  
某些通用的bean可能会依赖于这个类，但因为该类为@RequestScope，所以目前所知，在线程任务中是无法被创建并使用的，关于这一点暂时未想到好的解决办法，但以下可以提供其中一种解决办法:
使用`ThreadLocal`在线程任务执行前将`UserContext`入参 如果有异步任务需要使用到这个类的方法，则需要在异步任务调用之前，在当前请求类中获得`UserContext`， 因为还没开始异步任务，所以这里还是可以获取到的，然后使用`ThreadLocal`封装`UserContext` 然后传入到异步方法中，在异步方法体中调用`setUserContext()`将传入的`userContext` 赋值给自己线程任务需要依赖`UserContext`的bean，当然前提是注入的`UserContext`， 要暴漏setter方法,以下演示`CommonService`依赖`UserContext`
依赖`UserContext`的bean  
```java
@Service
public class CommonService {
    @Autowired
    @Getter
    @Setter
    private UserContext userContext;

    public String appendCommonUpdateSql(String userName) {
        if (userContext != null && userContext.getUid() != null) {
            userName = userContext.getUid();
        }
        if (Constants.isNull(userName)) {
            userName = Thread.currentThread().getName();
        }
        return " VERSION = VERSION + 1, MODIFY_BY = '" + userName + "'," +
                " MODIFY_TIME = FUC_NUMBER2TIMESTAMP(" + System.currentTimeMillis() + ") ";
    }
}
```
正常任务类，该类需要调用另外一个异步任务类，在当前类中`UserContext`还是能够正常注入的  
```java
@Service
 public class CurrentService {
      @Autowired
      private UserContext userContext;
      @Autowired
      private ThreadTaskService threadTaskService;

      public void main() {
          ThreadLocal<UserContext> userContextThreadLocal = new ThreadLocal<>();
          userContextThreadLocal.set(userContext);
          threadTaskService.async(userContextThreadLocal);
      }
 }
```
异步任务类，由一个请求中调用，从该类开始，往下流程中的所有类都会处于异步线程中， 不能够正常注入`UserContext`，因此可以再异步任务类开始使用传参setter的方式  
```java
@Service
 public class ThreadTaskService {
      @Autowired
      private CommonService commonService;

      @Async
      public void async(ThreadLocal<UserContext> userContextThreadLocal) {
          commonService.setUserContext(userContextThreadLocal.get());
      }
 }
```

### 3. 开启控制层方法级访问记录  
对控制层的方法的访问进行日志的记录，在方法执行之前、方法成功执行并返回值，方法出现异常三个层面对方法的参数和返回值以及异常信息进行记录  
使用方法，在配置类上加上注解`@EnableLogAspect`  
```java
@Configuration
@EnableLogAspect
public class Config {
    
}
```
效果如下,如果返回的对象没有重写`toString()`方法,则返回值只能打印对象名称和地址，如`Page`对象，可以把对象里的数据拿出来打印，目前没有这么处理，这么大的数据列表,似乎也没有意义，如果自己需要，可以修改相关代码  
```
2018-11-07 16:03:46.841 [http-nio-9008-exec-1] INFO  c.s.h.microservice.api.aspect.RequestContextAspect - [com.sinotrans.hd.account_center.controller.MappingController.list方法执行，参数列表===>({sysCode=null, currCode=null, sqlObject=com.sinotrans.hd.microService.util.SqlObject@26a2bed2, isInclude=false, receiverUnique=null, orgCode=null, mappingType=null, name=null, page=null, mappingCode=null, lines=null, groupCode=null})]
c.s.h.microservice.api.aspect.RequestContextAspect - [com.sinotrans.hd.account_center.controller.MappingController.list]方法执行结束，成功返回值..........: (com.sinotrans.hd.common.http.Page@4743d992)

```

### 4. 客户端基于Session作用域的用户信息保存  
> 客户端每当重新登录或重新刷新界面都会重新请求最新的用户信息，而后端服务如果需要获取当前用户信息，就需要根据当前登录的u_id去重复请求当前用户信息，而且不同的方法还需要重复去请求，这就导致了无畏的多次重复请求，因此该功能提供了一个解决思路，每当界面重新请求用户信息时，该功能就会收集每次请求的用户信息返回的结果保存到基于Session作用域的一个容器中，这样既利用了本来就需要请求的这次结果，又避免了其它方法想要使用用户信息还需要重复获取的过程；以后该用户需要使用用户信息的时候只要直接注入该对象，从中获取即可；而每次前端刷新重新获取用户信息，最新的用户信息又会覆盖到原来的值中；

使用步骤
* 在配置类上加入注解`@EnableSessionContext`开启该功能  

```java
@Configuration
@EnableSessionContext
public class config() {
    
}
```

* 使用注解`@CurrUser`标识自己项目中获取用户信息的方法 

```java
@RestController
public class UserController {
    
    @RequestMapping("/currUser")
    @CurrUser
    public Map<String, Object> getCurrUser() {
        // return 当前用户信息
    }
}
```


* 注入`SessionContext`使用  
```java
@Service
public class UserService() {
    
    @Autowired
    private SessionContext sessionContext;
    
    public void permission() {
        Map<String, Object> currUser = sessionContext.getCurrUserMap();
        // .............
    }
}
```

* 注意事项
`SessionContext`在线程任务中无法直接注入该对象，使用会报错；因为一旦一个主请求结束，随之`Session`作用域会不能存在，因此，需要使用方在主任务还未结束线程任务未开始之前，将该对象传入线程任务中，方可正常使用；

### 5. 通用跨服务列表查询
有一些列表查询，分属不同的服务，但调用通用方法却相同，比如组织架构的系统表，公共服务的数据字典表等，如果在第三个服务中需要调用，那么普遍的做法是第三方服务调用系统表或数据字典表之后再暴露接口给前端使用，这样每次都是要查询通用列表，而只是因为表不同或者服务不同，就需要暴露不同的方法，重复量太多，因此这里增加一个控制层方法接收服务名和要调用的表，然后去调用通用方法，这样就可以完成不同的服务对不同的表的简单化,其实本质上还是原框架中的通用查询

代码如下： 
```java
package com.sinotrans.hd.microservice.api.controller;

import com.sinotrans.hd.common.http.Page;
import com.sinotrans.hd.microservice.api.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author DDf on 2018/5/29
 */
@RestController
@RequestMapping("/apiCommon")
public class CommonApplicationController {
    @Autowired
    private RestTemplate restTemplate;


    /**
     * 有一些通用的列表查询，查询的表不同，可能所在的服务也不同，但每个服务都有相同
     * 的通用方法，为了方便调用指定服务下的通用方法查询某张表的列表数据，对原通用方法进行
     * 稍微修改，使前端可以直接调用指定服务名下的通用列表查询方法
     *
     * @param applicationName 服务名
     * @param JName           要查询的表名
     * @param page            页数
     * @param lines           条数
     * @param httpHeaders     header
     * @param whereColumns    列
     * @param whereOprators   操作符
     * @param whereValues     属性值
     * @param whereRelations  两个属性之间的关系符
     * @return
     */
    @RequestMapping("/pagedList")
    public Page pagedList(
            @RequestParam String applicationName, @RequestParam String JName,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer lines,
            @RequestHeader HttpHeaders httpHeaders,
            @RequestParam(value = "WHERE_COLUMNS", required = false) String whereColumns,
            @RequestParam(value = "WHERE_OPRATORS", required = false) String whereOprators,
            @RequestParam(value = "WHERE_VALUES", required = false) String whereValues,
            @RequestParam(value = "WHERE_RELATIONS", required = false) String whereRelations) {
        String url = "http://" + applicationName + "/GetPagedJsonByName?JName=" + JName;
        if (page != null) {
            url += "&page=" + page;
        }
        if (lines != null) {
            url += "&lines=" + lines;
        }
        if (Constants.isNotNull(whereColumns)) {
            url += "&WHERE_COLUMNS=" + whereColumns;
        }
        if (Constants.isNotNull(whereOprators)) {
            url += "&WHERE_OPRATORS=" + whereOprators;
        }
        if (Constants.isNotNull(whereValues)) {
            url += "&WHERE_VALUES=" + whereValues;
        }
        if (Constants.isNotNull(whereRelations)) {
            url += "&WHERE_RELATIONS=" + whereRelations;
        }
        return restTemplate.postForObject(url, httpHeaders, Page.class);
    }
}
```



## 四、原包改进
在项目中一定会引用的`jar`包中提供的某些功能，在实际使用过程中发现会带来一些不友好的感受，所以单方面会对个别已存在的类提供的功能或覆盖或改进，都统一在这个模块说明；以下功能不需要将代码复制到自己项目中！！！！

### 1. com.sinotrans.hd.microService.exception.DataException
> 该类提供的自定义异常继承自`Exception`，并且之后对该异常的支持不是特别友好，因此使用了全新的异常类继承自`org.springframework.core.NestedRuntimeException`运行时异常更为妥帖；
暂不支持国际化和占位符；

`GlobalCustomizeException.java`
```java
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

```

### 2. com.sinotrans.hd.filter.DataDefaultAdvice
> 该类原意为对系统中的各个不同的异常做响应处理，但区分太细，并且返回的语句十分不友好，并且对自定义的异常支持也不是很好，因`springboot`本身提供的错误属性针对目前开发来说已经足够使用，因此对该类采取直接覆盖的方法；并适当对`springboot`提供的返回属性进行修改，用以支持在第一步新建立的自定义异常；当然这里只是作为一个默认的修改，如果引用方对该修改不满意，可以自行修改；因此这里注入的时候使用了`@ConditionalOnMissingBean`注解；

`ErrorAttributes.java`  
```java
package com.sinotrans.hd.microservice.api.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

/**
 * Created by DDf on 2018/4/20
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
```

### 3. com.sinotrans.microService.client.filter.ClientDefultAdvice
> 该类问题大致与`com.sinotrans.hd.microService.exception.DataException`一致，并且不能友好的支持接收服务端抛出的异常然后返回到前端；因此使用如下新的类代替，类的全名不能和原类一样，否则一样会被移除！

`ClientDefultAdvice.java`
```java
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

```

### 4. 对以上三点的补充说明
在通用`jar`包中直接按照原来的包路径进行源码覆盖，如果第三方应用导入该项目`jar`包，发现并不能成功覆盖，因此如果使用该项目，则会在项目启动时将原来的两个默认处理类进行删除,新的处理类与原类型一直，但所属不同包

```java
package com.sinotrans.hd.microservice.api.config;

import com.sinotrans.hd.microservice.api.exception.ClientDefaultAdvice;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 *
 * 移除原框架包中对服务端和客户端对异常的默认处理类，原处理对异常处理不是很友好；
 * 且优先级别为最高，本地覆盖存在一定变数，因此直接移除，并使用全新的类来代替处理
 *
 * @author DDf on 2018/11/29
 */
public class RemoveOldDefaultAdvice implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        if (registry.containsBeanDefinition("clientDefultAdvice")) {
            registry.removeBeanDefinition("clientDefultAdvice");
        }
        if (registry.containsBeanDefinition("dataDefaultAdvice")) {
            registry.removeBeanDefinition("dataDefaultAdvice");
        }

        registry.registerBeanDefinition("clientDefaultAdvice", BeanDefinitionBuilder.genericBeanDefinition(
                ClientDefaultAdvice.class).getBeanDefinition());
    }
}
```

