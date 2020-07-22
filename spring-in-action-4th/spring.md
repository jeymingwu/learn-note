#  Spring 实战（第四版）

## 第一部分：Spring 框架的核心知识
+  Spring 核心： 依赖注入（DI）和面向切面编程（AOP）；
+  Spring **容器**（Spring框架核心）负责创建、装配对象，配置并管理对象的整个生命周期；
    +  类型：
        +  bean 工厂：由 org.springframework.beans.factory.BeanFactory 接口定义；提供基本的 DI 支持；
        +  ApplicationContext 应用上下文：由 org.springframework.context.ApplicationContext 接口定义；基于 BeanFactory 构建，并提供应用框架级别的服务；
            +  多种应用上下文类型：
                +  AnnotationConfigApplicationContext：从一个或多个基于 Java 的配置类中加载 Spring 应用上下文；
                +  AnnotationConfigWebApplicationContext：从一个或多个基于 Java 的配置类中加载 Spring Web 应用上下文；
                +  ClassPathXmlApplicationContext：从**类路径**下的一个或多个 XML 配置文件中加载上下文定义，把应用上下文的定义文件作为类资源；
                +  FileSystemXmlApplicationContext：从**文件系统**下的一个或多个 XML 配置文件中加载上下文定义；
                +  XmlWebApplicationContext：从 Web 应用下的一个或多个 XML 配置文件中加载上下文定义；
    +  生命周期：
        +  传统的生命周期：init()、service()、destroy()；
        +  Spring 容器的生命周期：
            1. 实例化
            2. 填充属性
            3. 调用 BeanNameAware 的 setBeanName() 方法
            4. 调用 BeanFactoryAware 的 setBeanFactory() 方法
            5. 调用 ApplicationContextAware 的 setApplicationContext() 方法
            6. 调用 BeanPostProcessor 的预初始化方法
            7. 调用 InitializingBean 的 afterPropertiesSet() 方法
            8. 调用自定义的初始化方法
            9. 调用 BeanPostProcessor 的初始化后方法
            10. bean 可以使用
            11. 调用 DisposableBean 的 destroy() 方法
            12. 调动自定义的销毁方法

![bean 的生命周期](../spring-in-action-4th/img/spring-bean-life.png)

+  bean 的生命周期说明
1.  Spring 对 bean 进行实例化；
2.  Spring 将值和 bean 引用注入到 bean 对应的属性中；
3.  若 bean 实现了 BeanNameAware 接口， Spring 将 bean 的 ID 传递给 setBeanName() 方法；
4.  若 bean 实现了 BeanFactoryAware 接口， Spring 将调用 setBeanFactory() 方法， 将 BeanFactory 容器实例传入；
5.  若 bean 实现了 ApplicationContextAware 接口，Spring 调用 setApplicationContext() 方法将 bean 所在的应用上下文的引入传入进来；
6.  若 bean 实现了 BeanPostProcessor 接口， Spring 调用 postProcessBeforeInitialization() 方法；
7.  若 bean 实现了 InitializingBean 接口， Spring 调用 afterPropertiesSet() 方法；（若 bean 使用了 init-method 声明了初始化方法，那么该方法也被调用）
8.  若 bean 实现了 BeanPostProcessor 接口， Spring 调用 postProcessAfterInitialization() 方法；
9.  此时 bean 已准备就绪，可被使用，将一直驻留在应用上下文中，直至该应用上下文被销毁；
10. 若 bean 实现了 DisposableBean 接口， Spring 调用 destroy() 接口方法；（若 bean 使用了 destroy-method 声明销毁方法，那么该方法也被调用）

![Spring 体系结构](../spring-in-action-4th/img/spring-architecture.png)
+  Spring 体系结构
    +  Spring 核心容器：Spring 最核心部分，管理 Spring 应用中 bean 的创建、配置和管理；
    +  AOP 模块：Spring 开发切面的基础，对面向切面编程提供丰富支持；
    +  数据访问和集成：
        +  JDBC
        +  ORM：Object-Relational Mapping
        +  JMS：在 Java Message Service 之上构建的 Spring 抽象层，会使用消息以异步的方式与其他应用集成；
    +  Web 与远程调用
        +  MVC：model-view-controller
    +  Instrumentation （使用场景有限）
        +  为 JVM 添加代理
    +  测试 

### 2.装配 Bean

### 3.高级装配

### 4.面向切面

## 第二部分：Web 中的 Spring

## 第三部分：后端中的 Spring

## 第四部分：Spring 集成