## 背景

> 基于springcloud的微服务应用，其调用关系比较复杂，现有服务治理如Zipkin/Pinpoint/Skywalking这些框架虽然可以做服务追踪，但对于某些特定需求支持的不是特别友好,所以基于此，才有了该收集器；

> 该收集器简化了日志收集，只针对远程feign调用的全部信息进行收集记录(请求和响应结果记录与否可选)，增加了服务信息列表，正反向依赖等功能

## 待完成功能

- 保持注册中心(eureka)状态和收集信息状态一致
- 收集系统可以控制服务上下线(直接影响到负载均衡处(ribbon)，并同步到注册中心(eureka))
- 服务可基于业务表达式进行定点路由

## 接入说明

### maven依赖

> 可先将base和feign模块打包上传到私有仓库或公司仓库

```xml
<!-- 系统集成依赖 -->
<dependency>
    <groupId>com.littlehow.springcloud</groupId>
    <artifactId>apm-feign</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>

<!-- sleuth支持 调用上下文生成trace等追踪信息 -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-sleuth</artifactId>
</dependency>
```

### 启动类修改
> 启动类需要继承收集系统的一个类

> 目的主要是为了优先加载org.springframework.cloud.netflix.feign.ribbon.LoadBalancerFeignClient

> 因为在sleuth下LoadBalancerFeignClient被其wrapper之后就无法进行继承或其他操作，因为sleuth是自己new的对象，所以就只能有为数不多的方式进行改变，如attach(java.lang.instrument.Instrumentation)、重新改变类后打包发到自己的maven仓库、改后编译出字节码进行自己加载

> 由于attach和重新打包都会依赖外部环境改变，所以使用字节码方式来进行自己加载，具体实现可以查看类com.littlehow.apm.base.ApmClassLoader

<br>

**特别注意**

**如果使用了@RibbonClients注解自定义RoundRobinRule或NIWSDiscoveryPing的，请注释掉**

```java

import com.littlehow.apm.feign.ApmApplication;

@SpringBootApplication
@EnableSwagger2Doc
@PropertySource("classpath:swagger.properties")
@EnableFeignClients(basePackages = {"xx.xx.feign"})
public class XXXApplication extends ApmApplication {

    public static void main(String[] args) {
        SpringApplication.run(XXXApplication.class, args);
    }
}
```

### 配置文件说明

> 配置文件全部存在默认项，所以不配置也不会影响系统运行

> 配置信息类 com.littlehow.apm.base.configuration.OuterProperties

```properties
# 可选配置项
# 该配置项可以不配置，没有配置该项的时候，系统默认获取配置项spring.application.name
# 如果apm.application.name和spring.application.name都没有任何信息，将默认命名为NONE
# 都为NONE时是无法分辨出服务依赖的
apm.application.name=application-name

# 可选配置项
# 该项不配置默认为空字符串
apm.application.name.cn=系统中文名称

# 可选配置项
# 该配置项表示自己系统包名的前缀，主要用于鉴别本系统真实调用类
# 如com.littlehow，这样就不会将org.springframework这样的识别成自有系统调用
# 打包时已将本系统表示包前缀打入，所以不是新包前缀，不需要进行配置该项
# 具体打入信息在com.littlehow.apm.base.web.SelfServerContext中
apm.application.package.start=

# 可选配置项
# 是否开启本服务调用栈信息缓存,默认开启
# 开启后不用每次调用都去爬栈分析，但有一个弊端
# 如过本服务有两个地方调用同一个远程服务的同一个接口
# 则可能解析有误，因为只会记录第一次调用的调用栈信息
apm.application.self.server.cache=true

# 可选配置项
# 默认配置为false，该功能表示，是否进行日志记录
# 该配置可以和远程记录同时存在
apm.collector.use.log=true

# 可选配置项
# 如过需要可视化数据，请务必配置该项
# 默认为空字符串(即不开启)，该配置项为是否开启远程记录
apm.collector.server.url=http://localhost:8000/

# 可选配置项
# 该配置项为当开始远程记录时的服务器与收集器之间的心跳间隔时间
# 间隔时间默认为30 (单位:秒)
apm.application.heartbeat.distance=30

# 可选配置项
# 该配置项主要用于控制是否记录远程调用的请求体以及返回的响应体，开启的话日志或收集信息量将增大
# 默认为true
apm.collector.trace.body=true

# 可选配置项
# 该配置项主要用于生成ScheduledThreadPoolExecutor定时任务池，默认为2
apm.schedule.pool.size=2

# 可选配置项
# 发送日志、服务信息、心跳的线程池,默认核心线程和最大线程为2
apm.thread.pool.size=2

# 可选配置项
# 发送日志、服务信息、心跳的线程池任务队列大小，默认100W
apm.thread.pool.queue.size=1000000

# 可选配置项
# 发送服务使用的template最大连接数, 默认50
apm.restTemplate.config.maxTotal=50

# 可选配置项
# 发送服务使用的template单机最大并发, 默认50
apm.restTemplate.config.maxPerRoute=50

# 可选配置项
# 发送服务使用的template连接超时时间(毫秒), 默认15000ms
apm.restTemplate..config.timeout.connection=15000

# 可选配置项
# 发送服务使用的template读取超时时间(毫秒), 默认15000ms
apm.restTemplate..config.timeout.read=15000
```

### 日志配置文件说明(非必须)
> 将收集日志单独打到指定文件下

```xml
    <!-- 属性配置 -->
    <property name="log_dir" value="logs"/>
    <property name="apm_log" value="${log_dir}/common-apm"/>
    
    <appender name="apm_log" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>${apm_log}.log</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>${apm_log}.%d{yyyy-MM-dd_HH}.log.gz</fileNamePattern>
            <maxHistory>7</maxHistory>
            <totalSizeCap>40GB</totalSizeCap>
            <cleanHistoryOnStart>true</cleanHistoryOnStart>
        </rollingPolicy>
        <encoder>
            <pattern>
                <!-- 这里去掉了[%X{X-B3-TraceId:-} %X{X-B3-SpanId:-}]，因为日志和发送都切换了线程 -->
                %date{HH:mm:ss.SSS}, [%thread] %-5level - %msg %n
            </pattern>
        </encoder>
    </appender>
    
    <!-- additivity不继续将日志打印到root配置的日志中去 -->
    <logger name="com.littlehow.apm.feign.advice.support.TraceInfoExecutor" level="info" additivity="false">
        <appender-ref ref="apm_log"/>
    </logger>
```


### feign调用前后信息

> 如果业务方想要获取feign调用前后的信息，有两种方式

- 申明一个类，继承com.littlehow.apm.feign.advice.AbstractCallFeignProcessor该类，并申明为spring组件即可
- 申明一个类，继承com.littlehow.apm.feign.advice.AbstractCallFeignProcessor类或实现com.littlehow.apm.feign.advice.CallFeignProcessor接口；然后调用com.littlehow.apm.feign.advice.AdviceExecutor.addProcessor进行注册即可

**调用示例如下**

```java
@Component
public class CustomerCallFeignProcessor extends AbstractCallFeignProcessor {
    @Override
    public void preExecute(WebAdviceContext context) {
        System.out.println(context.getSelf());
        System.out.println(context.getTraceId());
        System.out.println(context.getRequest().url());
        //设置自己的属性
        context.attribute("name", "littlehow");
    }

    @Override
    public void postExecute(WebAdviceContext context) {
        System.out.println((String)context.attribute("name"));
        System.out.println(context.getTraceId());
        System.out.println(context.getResponse().status());
    }

    public static void main(String[] args) {
        //如果没有注解component，则可以在任何地方调用即可
        AdviceExecutor.addProcessor(new CustomerCallFeignProcessor());
    }
}
```
