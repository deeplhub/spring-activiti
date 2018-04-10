# Spring MVC Activiti

这里是把Activiti5.22.0整合到Spring项目里。

<br><br>

Activiti数据库支持：<br>
Activiti的后台是有数据库的支持，所有的表都以act_开头。 第二部分是表示表的用途的两个字母标识。 用途也和服务的api对应。<br>
act_re_*: 're'表示repository。 这个前缀的表包含了流程定义和流程静态资源 （图片，规则，等等）。<br>
act_ru_*: 'ru'表示runtime。 这些运行时的表，包含流程实例，任务，变量，异步任务，等运行中的数据。 activiti只在流程实例执行过程中保存这些数据， 在流程结束时就会删除这些记录。 这样运行时表可以一直很小速度很快。<br>
act_id_*: 'id'表示identity。 这些表包含身份信息，比如用户，组等等。<br>
act_hi_*: 'hi'表示history。 这些表包含历史数据，比如历史流程实例， 变量，任务等等。<br>
act_ge_*: 通用数据， 用于不同场景下，如存放资源文件。

<br><br>

## 所用到的技术
spring<br>
spring mvc<br>
ehcache<br>
shiro<br>
mysql<br>
mybatis<br>
myabtis plus<br>
activiti<br>
...

<br><br>



### 查询缓存配置
1.先在mybatis-config.xml配置中cacheEnabled设为true（默认为true），开启全局缓存。<br>
2.在映射配置文件中设置局部缓存，设置如果下：<br>
以下两个标签二选一,第一个可以输出日志,第二个不输出日志 只要在对应的mapper配置文件中加入标签即可。readOnly默认是 false<br>
```
<cache type="org.mybatis.caches.ehcache.LoggingEhcache"/>
<cache type="org.mybatis.caches.ehcache.EhcacheCache"/>
```

缓存diskStore path的参数说明：<br>
user.home（用户的家目录）<br>
user.dir（用户当前的工作目录）<br>
java.io.tmpdir（默认的临时目录）<br>
ehcache.disk.store.dir（ehcache的配置目录）

<br><br>


### 项目日志功能注意

在项目中分另引入了log4j.properties和logback.xml，下面我们谈一下这两种的区别：<br>
如果引用了log4j.properties式的日志功能，那么当加入activiti工作流的，发布项目时是不会打印日志的，但在测试下可以打印日志功能。<br>
如果引用的logback.xml日志功能，在配置有activiti工作流发布时就可以打印日志。<br>

<br>

根据个人总结所得：因为Activiti工作流用到的日志是SLF4j，而并非LOG4J。一方面因为Logback-classic非常自然实现了SLF4j，另一方面SLF4j自动重新加载配置文件当配置文件修改了，Logback-classic能自动重新加载配置文件等。(如有新的见解请留言分享)
<br>

使用log4j.properties做为日志要引入以下jar:
- log4j.jar
- slf4j-log4j12.jar

另个还要web.xml中配置，配置如下：
```
<context-param>
	<param-name>log4jConfigLocation</param-name>
	<param-value>classpath:log4j.properties</param-value>
</context-param>
<listener>
	<listener-class>org.springframework.web.util.Log4jConfigListener</listener-class>
</listener>
```

<br>

使用logback.xml做为日志要引入以下jar:
- slf4j-api.jar
- log4j-over-slf4j.jar
- logback-classic.jar
- logback-core.jar

在maven中引入log4j-over-slf4j.jar会自动引入slf4j-api.jar<br>
使用logback日志无须在web.xml中配置。<br>
logback-classic不能与slf4j-log4j12同引，会导致jar冲突。


<br><br>


### 如果项目报错

```
java.lang.ClassNotFoundException: org.springframework.web.util.Log4jConfigListener

或

java.lang.ClassNotFoundException: org.springframework.web.context.ContextLoaderListener

```

<br><br>



#### 问题解析：
Maven项目中所有依赖(jdk/jar/classes)关系都被其管理。所以如果确定项目中确实存在该包或文件(org.springframework.web.util.Log4jConfigListener)，那必定是项目没有添加maven依赖所致。

<br><br>



#### 解决方案： 
右击项目，选择properties->Deployment Assembly->Add->Java Build Path Entries->Maven Dependencies

<br><br>

Project->Clear!