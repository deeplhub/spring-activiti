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


### 如果项目报错

java.lang.ClassNotFoundException: org.springframework.web.util.Log4jConfigListener
<br>
或
<br>
java.lang.ClassNotFoundException: org.springframework.web.context.ContextLoaderListener

<br><br>

#### 问题解析：
Maven项目中所有依赖(jdk/jar/classes)关系都被其管理。所以如果确定项目中确实存在该包或文件(org.springframework.web.util.Log4jConfigListener)，那必定是项目没有添加maven依赖所致。

<br><br>

#### 解决方案： 
右击项目，选择properties->Deployment Assembly->Add->Java Build Path Entries->Maven Dependencies

<br><br>

Project->Clear!