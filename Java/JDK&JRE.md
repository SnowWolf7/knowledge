# JDK&JRE

下载地址：https://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html

配置：

1. Terminal中输入指令`sudo -i vi /etc/.bash_profile`并回车
2. 输入密码并回车。在弹出的页面中按`i`键开始输入一下内容

```java
 JAVA_HOEM=/Library/Java/JavaVirtualMachines/jdk1.8.0_221.jdk/Contents/Home/
 CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
 PATH=$JAVA_HOME/bin:$PATH
 export JAVA_HOEM
 export CLASSPATH
 export PATH
```

3. 完成后按`esc`键退出编辑，再输入`:wq` 保存刚刚的操作
4. 输入`source /etc/.bash_profile并回车，使配置生效

