# SpringBoot 编程思想·目录

## 第一部分 总览 SpringBoot

### 1.初览 SpringBoot

+ SpringBoot 的特性
    + Create stand-alone Spring application
    + Embed Tomcat, Jetty or Undertow directly (no need to deploy WAR files)
    + Provide opinionated 'starter' dependencies to simplify your build configuration
    + Automatically configure Spring and 3rd party libraries whenever possible
    + Provide production-ready features such as metrics, health checks and externalized configuration
    + Absolutely no code generation and no requirement for XML configuration
+ SpringBoot 特征中文总结：
    + 创建独立的 Spring 应用；
    + 内嵌 Web 容器；
    + 开箱即用，简化构建配置；
    + 自动装配 Spring 或第三方类库；
    + 提供运维特性，如指标信息、健康检查和外部化配置；
    + 无代码生成，无 XML 配置；

### 2.理解独立的 Spring 应用

+ SpringBoot 按应用类型分类：
    + 非 Web 应用：主要用于服务提供、调度任务、消息处理等场景；
    + Web 应用：
        + SpringBoot 1.x：仅有 Servlet 容器实现，包括传统的 Servlet 和 Spring Web MVC；
        + SpringBoot 2.x：新增 Reactive Web 容器实现，即 Spring 5.0 WebFlux；
            + 故 SpringApplication API 上新增了 setWebApplicationType(WebApplicationType) 方法，允许程序显式设置 Web 应用的枚举类型；
                + WebApplicationType：
                    + NONE：非 Web 类型；
                    + SERVLET：Servlet Web；（依赖 org.springframework.boot:spring-boot-starter-web）
                    + REACTIVE：Reactive Web；（依赖 org.springframework.boot:spring-boot-starter-webflux）
+ SpringBoot 应用：
    + 应用类型
    + Spring Boot Starter 技术
    + Spring Boot 自动装配 
    + Spring Boot 和 Spring Framework 的生命周期 
    + -> 创建并启动嵌入式 Web 容器（不同于传统 J2EE 将应用打包成 WAR 或 EAR 再部署容器运行）
    
+ 创建 Spring Boot 应用
    + 命令行创建方式：创建 pom.xml 和借助 Maven Archetype 插件创建；
    + 图形化创建方式
        + 官网创建：[https://start.spring.io/](https://start.spring.io/) 
        + IDEA 内置工具
    + 创建可执行 Jar 的 SpringBoot 应用
        + 前提：需添加 spring-boot-maven-plugin 到 pom.xml 的 plugin 中；
        + 打包命令：mvn packing
+ 运行 Spring Boot 应用
    + 执行 SpringBoot 应用可执行 Jar：java -jar xxx.jar
 
### [3.理解固化的 Maven 依赖]()

### [4.理解嵌入式 Web 容器]()

### [5.理解自动装配]()

### [6.理解 Production-Ready 特性]()