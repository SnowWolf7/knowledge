## Dependency Management for Java Projects

This chapter explains how to apply basic dependency management concepts to Java-based projects. For a detailed introduction to dependency management, see [Introduction to Dependency Management](https://docs.gradle.org/current/userguide/introduction_dependency_management.html#introduction_dependency_management).

### Dissecting a typical build script

Let’s have a look at a very simple build script for a Java-based project. It applies the [Java Library plugin](https://docs.gradle.org/current/userguide/java_library_plugin.html#java_library_plugin) which automatically introduces a standard project layout, provides tasks for performing typical work and adequate support for dependency management.

*Example 1. Dependency declarations for a Java-based project*

```java
plugins {
    id 'java-library'
}

repositories {
    mavenCentral()
}

dependencies {
    implementation 'org.hibernate:hibernate-core:3.6.7.Final'
    api 'com.google.guava:guava:23.0'
    testImplementation 'junit:junit:4.+'
}
```

To find out more about defining dependencies, have a look at [Declaring Dependencies](https://docs.gradle.org/current/userguide/declaring_dependencies.html#declaring_dependencies).

### Using dependency configurations

A [Configuration](https://docs.gradle.org/current/dsl/org.gradle.api.artifacts.Configuration.html) is a named set of dependencies and artifacts. There are three main purposes for a *configuration*:

#### 	Declaring dependencies

​		A plugin uses configurations to make it easy for build authors to declare what other subprojects or external artifacts are needed for various purposes during the execution of tasks defined by the plugin. For example a plugin may need the Spring web framework dependency to compile the source code.

	#### 	Resolving dependencies

​		A plugin uses configurations to find (and possibly download) inputs to the tasks it defines. For example Gradle needs to download Spring web framework JAR files from Maven Central.

#### 	Exposing artifacts for consumption

​		A plugin uses configurations to define what *artifacts* it generates for other projects to consume. For example the project would like to publish its compiled source code packaged in the JAR file to an in-house Artifactory repository.

With those three purposes in mind, let’s take a look at a few of the [standard configurations defined by the Java Library Plugin](https://docs.gradle.org/current/userguide/java_library_plugin.html#sec:java_library_configurations_graph).

​	**implementation**

​		The dependencies required to compile the production source of the project which are not part of the API exposed by the project. For example the project uses Hibernate for its internal persistence layer implementation.

​	**api**

​		The dependencies required to compile the production source of the project which *are* part of the API exposed by the project. For example the project uses Guava and exposes public interfaces with Guava classes in their method signatures.

​	**testImplementation**

​		The dependencies required to compile and run the test source of the project. For example the project decided to write tets code with the test framework JUnit.

Various plugins add further standard configurations. You can also define your own custom configurations in your build via [Project.configurations{}](https://docs.gradle.org/current/dsl/org.gradle.api.Project.html#org.gradle.api.Project:configurations(groovy.lang.Closure)). See [Managing Dependency Configurations](https://docs.gradle.org/current/userguide/managing_dependency_configurations.html#managing_dependency_configurations) for the details of defining and customizing dependency configurations.

### Declaring common Java repositories

How does Gradle know where to find the files for external dependencies? Gradle looks for them in a *repository*. A repository is a collection of modules, organized by `group`, `name` and `version`. Gradle understands different [repository types](https://docs.gradle.org/current/userguide/repository_types.html#repository_types), such as Maven and Ivy, and supports various ways of accessing the repository via HTTP or other protocols.

By default, Gradle does not define any repositories. You need to define at least one with the help of [Project.repositories{}](https://docs.gradle.org/current/dsl/org.gradle.api.Project.html#org.gradle.api.Project:repositories(groovy.lang.Closure))before you can use module dependencies. One option is use the Maven Central repository:

```java
repositories {
	mavenCentral()
}
```

You can also have repositories on the local file system. This works for both Maven and Ivy repositories.

```java
repositories {
    ivy {
        // URL can refer to a local directory
        url "../local-repo"
    }
}
```

A project can have multiple repositories. Gradle will look for a dependency in each repository in the order they are specified, stopping at the first repository that contains the requested module.

To find out more about defining repositories, have a look at [Declaring Repositories](https://docs.gradle.org/current/userguide/declaring_repositories.html#declaring_repositories).

### Publishing artifacts

Dependency configurations are also used to publish files. Gradle calls these files *publication artifacts*, or usually just *artifacts*. As a user you will need to tell Gradle where to publish the artifacts. You do this by declaring repositories for the `uploadArchives` task. Here’s an example of publishing to a Maven repository:

```java
plugins {
    id 'maven'
}

uploadArchives {
    repositories {
        mavenDeployer {
            repository(url: "file://localhost/tmp/myRepo/")
        }
    }
}
```

Now, when you run `gradle uploadArchives`, Gradle will build the JAR file, generate a `.pom` file and upload the artifacts.

To learn more about publishing artifacts, have a look at [Legacy Publishing](https://docs.gradle.org/current/userguide/artifact_management.html#artifact_management).













