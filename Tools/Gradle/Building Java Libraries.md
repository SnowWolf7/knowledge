# Building Java Libraries

This guide walks you through the process of using Gradle's Build Init plugin to produce a new Java library which can be used in other JVM libraries and applications.

### What you'll build

You'll generate a Java library that follows Gradle's conventions.

### What you'll need

+ About 12 minutes
+ A text editor or IDE
+ A Java Develolment Kit (JDK) version 8 or higher
+ A Gradle distribution, version 5.0 or better

### Creating a library project

Gradle comes with a built-in plugin called the Build Init plugin.It is documented in the [Gradle User Manual](https://docs.gradle.org/current/userguide/build_init_plugin.html). The plugin provides a task, called `init`, that generates the project. The `init` task uses the (also built-in) `wrapper` task to create a Gradle wrapper script, `gradlew`.

The first step is to create a folder for the new project and change directory into it.

```java
$ mkdir demo
$ cd demo
```

### Run the init task

From inside the new project directory, run the `init` task and select `java-library` project type when prompted. For the ohter questions, press enter to use the default values.

```java
$ gradle init

Select type of project to generate:
  1: basic
  2: cpp-application
  3: cpp-library
  4: groovy-application
  5: groovy-library
  6: java-application
  7: java-library
  8: kotlin-application
  9: kotlin-library
  10: scala-library
Enter selection (default: basic) [1..10] 7

Select build script DSL:
  1: groovy
  2: kotlin
Enter selection (default: groovy) [1..2]

Select test framework:
  1: junit
  2: testng
  3: spock
Enter selection (default: junit) [1..3]

Project name (default: demo):

Source package (default: demo):


BUILD SUCCESSFUL in 1s
2 actionable tasks: 2 executed
```

If you prefer the kotlin DSL, you can select `kotlin` for the build script DSL

The `init` task runs the `wrapper` task first, which generates the `gradlew` and `gradlew.bat` wrapper scripts. Then it creates the new project with the following structure. Then it creates the new project with the following structure:

```java
.
├── build.gradle
├── gradle
│   └── wrapper  1
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── gradlew
├── gradlew.bat
├── settings.gradle
└── src
    ├── main
    │   └── java  2
    │       └── demo
    │           └── Library.java
    └── test
        └── java  3
            └── demo
                └── LibraryTest.java
```

1: Generated for wrapper files

2: Default Java source folder

3: Default Java test folder

You now have the necessary components for a simple Java library project.

### Review the generated project files

The settings file is heavily commented, but has only one active line:

```java
rootProject.name = 'building-java-libraries' 1
```

1: This assigns the name of the root project.

The generated `build.gradle` file also has many comments. The active portion is reproduced here (note version numbers for the dependencies may be updated in later versions of Gradle):

```java
plugins {
    id 'java-library'
}

repositories {
    jcenter() 1
}

dependencies {
    api 'org.apache.commons:commons-math3:3.6.1' 2

    implementation 'com.google.guava:guava:27.0.1-jre' 3

    testImplementation 'junit:junit:4.12' 4
}
```

1: Public Binary Artifactory repository

2: This is an example of a dependency which is exported to consumers, that is to say found on their compile classpath.

3: This is an example of a dependency which is used internally, and not exposed to consumers on their own compile classpath.

4: JUnit testing library

The build script adds the `java-library` plugin. This is an extension of the `java-base` plugin and adds additional tasks for compiling Java source code.

The file `src/main/java/guidesamples/Library.java` is shown here:

```java
package demo;

public class Library {
	public boolean someLibraryMethod() {
		return true;
	}
}
```

The generated JUnit specification, `src/main/java/demo/LibraryTest.java ` is shown next:

```java
package demo;

import org.junit.Test;
import static org.junit.Assert.*;

public class LibraryTest {
  @Test 
  public void testSomeLibraryMethod() {
    Library classUnderTest = new Library();
    assertTrue("someLibraryMethod should return 'true'",classUnderTest.someLibraryMethod());
    }
}
```

The generated test class has a single JUnit 4 test. The test instantiates the `Library` class, invokes the `someLibraryMethod` method, and checks that the returned value is true.

### Assemble the library JAR

To build the project, return the `build` task. You can use the regular `gradle` command, but when a project includes a wrapper script, it is good form to use it instead.

```java
$ ./gradlew build
> Task :compileJava
> Task :processResources NO-SOURCE
> Task :classes
> Task :jar
> Task :assemble
> Task :compileTestJava
> Task :processTestResources NO-SOURCE
> Task :testClasses
> Task :test
> Task :check
> Task :build
```

*Note: The first time you run the wrapper script, `gradlew`, there may be a delay while that version of `gradle` is downloaded and stored locally in your `~/.gradle/wrapper/dists` folder.*

The first time you run the build, Gradle will check whether or not you already have the JUnit libraries and other listed dependencies in your cache under your `~/.gradle` directory. If not, the libraries will be downloaded and stored there. The next time you run the build, the cached versions will be used. The `build` task compiles the classes, runs the tests, and generates a test report.

You can view the test report by opening the HTML output file, located at `build/reports/tests/test/index.html`.

A simple report is shown here:

![Test Summary](https://guides.gradle.org/building-java-libraries/images/Test-Summary.png)

You can find your newly packaged JAR file in the `build/libs` directory with the name `building-java libraries.jar`. Verify that the archive is valid by running the following command:

```java
$ jar tf build/libs/building-java-libraries.jar
META-INF/
META-INF/MANIFEST.MF
Library.class
```

You should see the required manifest file -- `MANIFEST.MF` -- and the compiled `Library` class.

*Note: All of this happens without any additional configuration in the build script because Gradle’s `java-library` plugin assumes your project sources are arranged in a [conventional project layout](https://docs.gradle.org/5.0/userguide/java_plugin.html#sec:java_project_layout). You can customize the project layout if you wish [as described in the user manual](https://docs.gradle.org/5.0/userguide/java_plugin.html#sec:changing_java_project_layout).*

Congratulations, you have just completed the first step of creating a Java library! You can now customize this to your own project needs.

### Customize the library JAR

You will often want the name of the JAR file to include the library *version*. This is easily achieved by setting a top-level `version`property in the build script, like so:

```java
version = '0.1.0'
```

Now run the `jar` task:

```java
$ ./gradlew jar
```

and notice that the resulting JAR file at `build/libs/building-java-libraries-0.1.0.jar` contains the version as expected.

Another common requirement is customizing the manifest file, typically by adding one or more attributes. Let’s include the library name and version in the manifest file by [configuring the `jar` task](https://docs.gradle.org/5.0/userguide/more_about_tasks.html#sec:configuring_tasks) . Add the following to the end of your build script:

```java
jar {
    manifest {
        attributes('Implementation-Title': project.name,
                   'Implementation-Version': project.version)
    }
}
```

To confirm that these changes work as expected, run the `jar` task again, and this time also unpack the manifest file from the JAR	:

```java
$ ./gradlew jar
$ jar xf build/libs/building-java-libraries-0.1.0.jar META-INF/MANIFEST.MF
```

Now view the contents of the `META-INF/MAINFEST.MF` file and you should see the following:

META-INF/MANIFEST/MF

```java
Manifest-Version: 1.0
Implementation-Title: building-java-libraries
Implementationn-Versioin: 0.1.0
```

*Note: The `mainfest` is just one of many properties that can be configured on the `jar` task. For a complement list, see the [Jar section](https://docs.gradle.org/5.0/dsl/org.gradle.api.tasks.bundling.Jar.html) of the [Gradle Language Reference](https://docs.gradle.org/5.0/dsl/) as well as the [Jar](https://docs.gradle.org/5.0/userguide/java_plugin.html#sec:jar) and [Creating Archives](https://docs.gradle.org/5.0/userguide/working_with_files.html#sec:archives) sections of Gradle [User Manual](https://docs.gradle.org/5.0/userguide/userguide.html).*

Now you can complete this exercise by trying to compile some Java code that uses the library you just built.

### Adding API documentation

The `java-library` plugin has built-in support for Java's API documentation tool via the `javadoc` task.

The code generated by the Build Init plugin already placed a comment on the `Library.java` file. Modify the comment to become `javadoc` markup.

src/main/java/Library.java

```java
/**
 * This java source file was generated by the Gradle 'init' task.
 */
package demo;

public class Library {
    public boolean someLibraryMethod() {
        return true;
    }
}
```

Run the `javadoc` task.

```java
$ ./gradlew javadoc

> Task :compileJava
> Task :processResources NO-SOURCE
> Task :classes
> Task :javadoc

BUILD SUCCESSFUL in 1s
2 actionable tasks: 2 executed
```

You can see the generated `javadoc` files by opening the HTML file located at `build/docs/javadoc/index.html`.

### Summary

That's it!  You’ve now successfully built a Java library project, packaged it as a JAR and consumed it within a separate application. Along the way, you’ve learned how to:

- Generate a Java library
- Adapt the generated `build.gradle` and sample Java files are structured
- Run the build and view the test report
- Customize the name of a JAR file and the content of its manifest
- Generate API documentation.

### Next steps

Building a library is just one aspect of reusing code across project boundaries. From here, you may be interested in:

+ [Consuming JVM libraries](https://docs.gradle.org/current/userguide/dependency_management_for_java_projects.html)
+ [Publishing JVM libraries](https://docs.gradle.org/5.0/userguide/artifact_management.html)

+ [Working with multi-project builds](https://docs.gradle.org/5.0/userguide/intro_multi_project_builds.html)



