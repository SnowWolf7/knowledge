## Getting started

开始使用Spring或者Spring Boot，需要回答三个问题：what，how，why？

### Spring Boot的介绍

- Spring boot可以轻松地创建独立的、生产级的基于Spring的应用，无需过多的spring配置。
- 可以使用Spring Boot创建使用java -jar或者更传统的war部署启动的java应用。还提供一个运行“spring 脚本”的命令行工具。
- 主要目标：
- - 为所有spring开发提供一个从根本上更快更广泛访问的入门体验。
  - Be opinionated out of the box, but get out of the way quickly as requirements start to diverge from the defaults.
  - Provide a range of non-functional features that are common to large classes of projects (e.g. embedded servers, security, metrics, health checks, externalized configuration).
  - 绝对没有代码生成和XML配置的需求。

### 系统需求

+ 需要java7和Spring Framework4.1.5，最好使用java8。

### servlet 容器

开箱即用支持以下servlet容器

| **Name**    | **Servlet Version** | **Java Version** |
| ----------- | ------------------- | ---------------- |
| Tomcat8     | 3.1                 | Java7+           |
| Tomcat7     | 3.0                 | Java6+           |
| Jetty9      | 3.1                 | Java7+           |
| Jetty8      | 3.0                 | Java6+           |
| Undertow1.1 | 3.1                 | Java7+           |

同样，也可以将Spring Boot应用部署到任何兼容Servlet3.0的容器中。

### 安装Spring Boot

Spring Boot可以与经典的java开发工具一起使用，或者作为命令行工具安装。无论哪种，都需要Java SDK v1.6或者更高版本。推荐使用最新版本的java。

### 安装说明

- 你可以像其他独立的Java库一样使用Spring Boot。只需简单地在类路径中包含spring-boot-*.jar文件即可。
- Spring Boot不需要特殊的工具集成，你可以使用任何IDE或者文本编辑器。
- Spring Boot没有任何特别的，你可以像其他java程序一样run和debug。

### Maven安装

Spring Boot兼容Maven3.2或者更高版本。

- Spring Boot dependencies use the *org.springframework.boot* **groupId**。通常，Maven POM文件继承自spring-boot-starter-parent项目并声明对一个或多个“Starter POMs”的依赖。Spring Boot提供可选的Maven插件来创建可执行jar。下面是一个经典的pom文件。 

```java
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.example</groupId>
    <artifactId>myproject</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <!-- Inherit defaults from Spring Boot -->
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>1.2.3.RELEASE</version>
    </parent>
    <!-- Add typical dependencies for a web application -->
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
    </dependencies>
    <!-- Package as an executable jar -->
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

**注意** spring-boot-starter-parent是使用Spring Boot的好方法，但是并不是所有时候都适合的。详见[Maven安装注意2](https://docs.spring.io/spring-boot/docs/1.2.3.RELEASE/reference/htmlsingle/#getting-started-maven-installation)

### Gradle安装

Spring Boot兼容Gradle1.12或者更高版本。

- Spring Boot dependencies use the *org.springframework.boot* **groupId**。通常，你的项目声明对一个或多个“Starter POMs”的依赖。Spring Boot提供一个有用的gradle插件来依赖声明并创建可执行jar。 
- 当你构建项目时，Gradle Wapper提供了一种“获取”Gradle的好方法，这是一个小脚本和库，它和你的代码一起提交以引导构建过程。下面是一个经典的build.gradle文件。

```java
buildscript {
    repositories {
        jcenter()
        maven { url "http://repo.spring.io/snapshot" }
        maven { url "http://repo.spring.io/milestone" }
    }
    dependencies {
        classpath("org.springframework.boot:spring-boot-gradle-plugin:1.2.3.RELEASE")
    }
}
apply plugin: 'java'
apply plugin: 'spring-boot'
jar {
    baseName = 'myproject'
    version =  '0.0.1-SNAPSHOT'
}
repositories {
    jcenter()
    maven { url "http://repo.spring.io/snapshot" }
    maven { url "http://repo.spring.io/milestone" }
}
dependencies {
    compile("org.springframework.boot:spring-boot-starter-web")
    testCompile("org.springframework.boot:spring-boot-starter-test")
}

```

### 安装Spring Boot CLI

Spring Boot CLI是一个命令行工具，如果你想快速进行原型设计，可以使用它。它允许你运行Groovy脚本。

你不需要使用CLI来work with Spring Boot，但它一定是实现Spring应用程序最快的方法。

#### 手动安装

从Spring软件库下载Spring Boot CLI的发行版。依照INSTALL.txt安装。

#### GVM安装

详情见[官网](https://docs.spring.io/spring-boot/docs/1.2.3.RELEASE/reference/htmlsingle/#getting-started-gvm-cli-installation)

#### OSX Homebrew 安装

详情见[官网](https://docs.spring.io/spring-boot/docs/1.2.3.RELEASE/reference/htmlsingle/#getting-started-homebrew-cli-installation)

#### MacPorts 安装

详情见[官网](https://docs.spring.io/spring-boot/docs/1.2.3.RELEASE/reference/htmlsingle/#getting-started-macports-cli-installation)

#### 命令行完成

*该部分晦涩难懂*

#### 快速开始一个Spring CLI实例

详情见[官网](https://docs.spring.io/spring-boot/docs/1.2.3.RELEASE/reference/htmlsingle/#getting-started-cli-example)

#### 更新 CLI

建议使用包管理命令，例如 `brew upgrade`

### 开发第一个Spring Boot应用



