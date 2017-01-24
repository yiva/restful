# restful
SpringMVC REST Demo
> 通过前后端分离的方式将服务端从系统中独立分配出来进行部署，使前端的视图层与后端的业务逻辑层进行分层，对逻辑以及视图进行解耦。通过个人实践，使用SpringMVC搭建REST风格系统。   

_搭建过程主要还是对spring的配置文件的设置_ 

### maven依赖
项目构建通过maven进行包的管理，在这里我们通过Spring框架进行系统配置，同时使用json作为rest请求消息格式，我们所需要的包分别是SpringMVC的依赖包以及Jackson的Json处理包。主要依赖包配置如下：
```html
<!--spring 依赖-->
<dependency>
	<groupId>org.springframework</groupId>
	<artifactId>spring-webmvc</artifactId>
	<version>4.2.5.RELEASE</version>
</dependency>
<dependency>
	<groupId>org.springframework</groupId>
	<artifactId>spring-oxm</artifactId>
	<version>4.2.5.RELEASE</version>
</dependency>
<!-- jackson json -->
<dependency>
	<groupId>com.fasterxml.jackson.core</groupId>
	<artifactId>jackson-core</artifactId>
	<version>2.5.4</version>
</dependency>
<dependency>
	<groupId>com.fasterxml.jackson.core</groupId>
	<artifactId>jackson-databind</artifactId>
	<version>2.5.4</version>
</dependency>
<dependency>
	<groupId>org.codehaus.castor</groupId>
	<artifactId>castor-xml</artifactId>
	<version>1.3.1</version>
</dependency>
```
### web.xml
在这里进行Spring配置文件的配置，以及过滤器的配置工作。
```html
<context-param>
	<param-name>contextConfigLocation</param-name>
	<param-value>/WEB-INF/applicationContextMain.xml</param-value>
</context-param>
<listener>
	<listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
</listener>
<servlet>
	<servlet-name>action</servlet-name>
	<servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
	<init-param>
		<param-name>contextConfigLocation</param-name>
		<param-value>/WEB-INF/action-servlet.xml</param-value>
	</init-param>
	<load-on-startup>1</load-on-startup>
</servlet>
<!--设置所有请求都提交给spring进行处理-->
<servlet-mapping>
	<servlet-name>action</servlet-name>
	<url-pattern>/</url-pattern>
</servlet-mapping>
```
### spring配置
```html
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:p="http://www.springframework.org/schema/p"
	xmlns:context="http://www.springframework.org/schema/context"
	xmlns:mvc="http://www.springframework.org/schema/mvc" xmlns:oxm="http://www.springframework.org/schema/oxm"
	xmlns:tx="http://www.springframework.org/schema/tx"
	xsi:schemaLocation="
		http://www.springframework.org/schema/beans 
		http://www.springframework.org/schema/beans/spring-beans-4.2.xsd   
       	http://www.springframework.org/schema/aop 
       	http://www.springframework.org/schema/aop/spring-aop-4.2.xsd   
       	http://www.springframework.org/schema/tx 
       	http://www.springframework.org/schema/tx/spring-tx-4.2.xsd   
       	http://www.springframework.org/schema/context 
       	http://www.springframework.org/schema/context/spring-context-4.2.xsd
       	http://www.springframework.org/schema/mvc  
      	http://www.springframework.org/schema/mvc/spring-mvc-4.2.xsd
      	http://www.springframework.org/schema/oxm
      	http://www.springframework.org/schema/oxm/spring-oxm-4.2.xsd">

	<!-- 静态资源处理，由于我们将所有请求都交由spring进行处理，对于html,js,css这些静态资源我们需要进行特殊配置，使得我们能够直接进行访问 -->
	<mvc:resources location="/resources/" mapping="/resources/**"></mvc:resources>

	<mvc:annotation-driven />

	<!-- 设置使用注解的类所在的jar包，限定只对controller进行扫描 -->
	<context:component-scan base-package="org.rest.action"
		use-default-filters="false">
		<context:include-filter type="annotation"
			expression="org.springframework.stereotype.Controller" />
	</context:component-scan>

	<!-- 完成请求和注解POJO的映射-->
	<bean
		class="org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter">
		<property name="messageConverters">
			<list>
				<ref bean="stringHttpMessageConverter" />
				<ref bean="mappingJackson2HttpMessageConverter" />
				<ref bean="marshallingHttpMessageConverter" />
			</list>
		</property>
	</bean>
	<!--pojo对象转换Json-->
	<bean id="stringHttpMessageConverter"
		class="org.springframework.http.converter.StringHttpMessageConverter" />
	<bean id="mappingJackson2HttpMessageConverter"
		class="org.springframework.http.converter.json.MappingJackson2HttpMessageConverter" />
	<bean id="marshallingHttpMessageConverter"
		class="org.springframework.http.converter.xml.MarshallingHttpMessageConverter">
		<property name="marshaller" ref="castorMarshaller" />
		<property name="unmarshaller" ref="castorMarshaller" />
	</bean>

	<bean id="castorMarshaller" class="org.springframework.oxm.castor.CastorMarshaller" />

	<!-- 对转向页面的路径解析。prefix：前缀， suffix：后缀 -->
	<bean
		class="org.springframework.web.servlet.view.InternalResourceViewResolver"
		p:prefix="/jsp/" p:suffix=".jsp" />
</beans>
```
### 试例方法
__Controller类，负责接收url请求__
```java
package org.rest.action;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.rest.pojo.UserInfo;
import java.util.ArrayList;

@Controller
@RequestMapping("/userinfo")
public class SpittleController {

	@RequestMapping(value = "/{id}/{name}", method = RequestMethod.GET, headers = { "Accept=application/json" })
	public @ResponseBody ArrayList<UserInfo> getSpittle(
			@PathVariable("id") int id, @PathVariable("name") String name,
			Model model) {
		UserInfo user = new UserInfo();
		user.setId(id);
		user.setName(name);
		ArrayList<UserInfo> list = new ArrayList<UserInfo>();
		list.add(user);
		return list;
	}
}
```
__Pojo类__
```java
package org.rest.pojo;

public class UserInfo {
	
	private Integer id;
	private String name;
	private Integer age;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
}
```

----
部署完成，测试如下：

![系统部署测试](http://upload-images.jianshu.io/upload_images/3010157-df747c98d521335e.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)    
