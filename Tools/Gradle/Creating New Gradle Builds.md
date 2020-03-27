# Creating New Gradle Builds

### Initialize a project

使用gradle init命令，将在当前路径下创建一下目录：

```java
├── build.gradle  1
├── gradle
│   └── wrapper
│       ├── gradle-wrapper.jar  2
│       └── gradle-wrapper.properties  3
├── gradlew  4
├── gradlew.bat  5
└── settings.gradle  6

```

其中：

1. Gradle build script for configuring the current project
2. Gradle Wrapper executable JAR
3. Gradle Wrapper configuration properties
4. Gradle Wrapper script for Unix-based systems
5. Gradle Wrapper script for Windows
6. Gradle settings script for configurating the Gradle build

### Create a task

Gradle通过Groovy或者Kotlin-based DSL提供用于创建和配置tasks的APIs。A Project includes a collection of Tasks, each of which performs some basic operation. 

Gradle comes with a library of tasks that you can configure in your own projects.以Copy为例：

1. 新建src目录
2. src中添加一个myfile.txt，内容随意。
3. 在build文件（build.gradle)中定义一个Copy任务copy，复制myfile.txt到目录dest下。

```java
task copy(type: Copy, group: "Custom", description: "Copys sources to the dest directory") {
    from "src"
    into "dest"
}
```

执行task任务：

```java
>./gradlew copy
>Task :copy
```

### Apply a plugin

Gradle includes a range of plugins, and many, many more are available at [the Gradle plugin portal](https://plugins.gradle.org/).其中，有一个base插件，结合task Zip，you can create a zip archive of your project with a configured name and location。 

在build脚本文件（build.gradle）中添加baseplugin，添加zip任务：

```java
plugins {
    id "base"
}
task zip(type: Zip, group: "Archive", description: "Archives sources in a zip file") {
from "src"
setArchiveName "gradle-demo-1.0.zip"
}
```

base结合settings，在build/distributions中创建一个gradle-demo-1.0.zip文件。

### Explore and debug your build

Gradle还可以做更多的的事情，完整的命令接口参见[reference to the command-line interface](https://docs.gradle.org/4.10-rc-2/userguide/command_line_interface.html)

#### **Discover available tasks**

The tasks command lists Gradle tasks that you can invoke, including those added by the base plugin, and custom tasks you just added as well. 

可以通过>Task :tasks查看task列表，例如zip, assemble, build, clean, init, wrapper, copy, buildEnvironment, components,dependencies等。 

#### Analyze and debug you build（`复杂，待研究`）

Gradle also provides a rich, web-based view of your build called a [build scan](https://scans.gradle.com/?_ga=2.219657118.60988071.1555567581-1565915737.1552382836)。

![](https://guides.gradle.org/creating-new-gradle-builds/images/basic-demo-build-scan.png)

By using the `--scan` option or by explicitly applying the build scan plugin to your project, you can create a build scan at [scans.gradle.com](https://scans.gradle.com/) free of charge. Publishing build scans to scans.gradle.com transmits [this](https://docs.gradle.com/build-scan-plugin/#captured_information) data to Gradle’s servers. To keep your data on your own servers, check out [Gradle Enterprise](https://gradle.com/enterprise).

Try creating a build scan by adding `--scan` when executing a task.

```java
❯ ./gradlew zip --scan

BUILD SUCCESSFUL in 0s
1 actionable task: 1 up-to-date

Publishing a build scan to scans.gradle.com requires accepting the Terms of Service defined at https://scans.gradle.com/terms-of-service. Do you accept these terms? [yes, no]
Gradle Cloud Services license agreement accepted.

Publishing build scan...
https://gradle.com/s/repnge6srr5qs
```

If you browse around your build scan, you should be able to easily find out what tasks where executed and how long they took, which plugins were applied, and much more. Consider sharing a build scan the next time you are debugging something on StackOverflow.

Learn more about how to configure and use build scans in the [Build Scan Plugin User Manual](https://docs.gradle.com/build-scan-plugin/).

#### Discover available properties

The properties command tells you about a project’s attributes. 

```java
❯ ./gradlew properties
```

The output is extensive. Here are just a few of the available properties:

```java
> Task :properties

------------------------------------------------------------
Root project
------------------------------------------------------------

buildDir: /Users/.../basic-demo/build
buildFile: /Users/.../basic-demo/build.gradle
description: null
group:
name: basic-demo
projectDir: /Users/.../basic-demo
version: unspecified

BUILD SUCCESSFUL
```

The `name` of the project matches the name of the folder by default. You can also specify `group`and `version` properties, but at the moment they are taking their default values, as is `description`.

The `buildFile` property is the fully-qualified path name to your build script, which resides in the `projectDir` — by default.

You can change many of the properties. For example, you might try adding the following lines to your build script file, and re-execute `gradle properties`.

```java
description = "A trivial Gradle build"
version = "1.0"
```

