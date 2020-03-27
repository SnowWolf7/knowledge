# **Creating Build Scans**

Build scan是一个build的共享的集中记录，提供深入了解一个build发生了什么以及发生原因的功能。通过将build scan插件加入project中，you can publish build scans to [http://scans.gradle.com](https://scans.gradle.com/?_ga=2.160323770.60988071.1555567581-1565915737.1552382836) for free. 

## **What you’ll create**

本指南介绍如何在不进行任何build script修改的情况下及时发布build scans。当然，也会介绍如何通过修改build script以对给定project的所有builds启用build scans。或者，你可以修改init script以启用所有项目的build scans。

## **Auto-apply the build scan plugin**

Gradle 4.3开始，you can enable scans without any additional configuration in your build script. 当使用命令行option - -scan来发布一个build scan时，the required build scan plugin is applied automatically. Before the end of the build, you are asked to accept the license agreement on the command line.

## **Enable build scans on all builds of your project**

Add a plugins block to the root project build script file with the following contents:

```java
plugins: {
    id 'com.gradle.build-scan' version '2.1'  1
}
```

*1 use latest plugin version which can be found on the* [Gradle Plugin Portal](https://plugins.gradle.org/plugin/com.gradle.build-scan) 

If you already hava a plugins block, always put the build scan plugin first. Adding it below any existing plugins will still work, but will misss useful information.

## **Accept the license agreement**

In order to publish build scans to [https://scans.gradle.com](https://scans.gradle.com/), you need to accept the license agreement. This can be done ad-hoc via the command line when publishing, but can also be specified in your Gradle build file（build.gradle), by adding the following section: 

```java
buildScan {
    termsOfServiceUrl = 'https://gradle.com/terms-of-service'
    termsOfServiceAgree = 'yes'
}
```

The buildScan block allows you to configure the plugin. Here you are setting two properties necessary to accept the license agreement. Other are availale in [Build Scans User Manual](https://docs.gradle.com/build-scan-plugin/?_ga=2.159938618.60988071.1555567581-1565915737.1552382836) 

## **Publish a build scan**

A build scan is published using a command-line flag called - -scan.

## **Access the build scan online**

The first time you follow the link, you will be asked to activate the created build scan.

The email you receive to activate your build scan will look similar to:

![](https://guides.gradle.org/creating-build-scans/images/build_scan_email.png)

Follow the link provided in the email, and you will see the created build scan.

![](https://guides.gradle.org/creating-build-scans/images/build_scan_page.png)

## **Enable build scans for all builds(optional)**

为了避免 为每一个build添加plugin和license agreement，可以使用Gradle init script. Create a file(例如buildScan.gradle) in the directory ~/.gradle/init.d with the following contents:

```java
initscript {
    repositories {
        gradlePluginPortal()
    }
    dependencies {
        classpath 'com.gradle:build-scan-plugin:@scanPluginVersion@'
    }
}
rootProject {
    apply plugin: com.gradle.scan.plugin.BuildScanPlugin
    buildScan {
        termsOfServiceUrl = 'https://gradle.com/terms-of-service'
        termsOfServiceAgree = 'yes'
    }
}
```

## **Summary**

本指南中，你可以学到：

- 如何generate a build scan
- 如何view the build scan information online
- 如何 create an init script to enable build scans for all builds

