笔记外链： http://581c88a2.wiz03.com/share/s/1o78yy200k0w27GvK52Rf_O_3dG3N51ZjAnh2tLOBq0REp1I 密码：4165

[TOC]

## 一、 简介  
### 1. 设想  
因目前开发模块越来越多，有很多在一个项目中已经定义的方法，再开发另外一个系统的时候，总是需要再开发复制一份，如果是针对公共接口的时候，很可能原接口进行了修改，过多的业务系统都要重新进行修改，所以设想开发一个独立的模块，该模块对所有公共的方法，远程调用`api`进行统一封装，别的模块引入这个模块即可直接使用，同时也可以对该模块进行修改。

### 2. 存在问题  
该模块在`pom.xml`中引入之后，需要自行前往`svn`将项目拉取下来，然后使用`maven install`命令，安装到本地使用，这样方便如果对源码进行修改，可以更方便的即使调试，但无形之中对一部分使用会造成相当麻烦，因此这种方式适合内部开发人员使用。
## 二、开发与使用  
### 1. 项目地址  
http://172.30.21.33:10080/svn/HuaDong/hd_micro_service/02.code/microservice-api

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
        readTimeout: 3333
        connectTimeout: 4444
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


