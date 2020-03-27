## OOM报错分析和解决方案

常见的报错信息如下：

+ java.lang.OutOfMemoryError: Java heap space

+ java.lang.OutOfMemoryError: GC overhead limit exceeded
+ java.lang.OutOfMemoryError: Permgen space （java8以下版本）
+ java.lang.OutOfMemoryError: Metaspace
+ java.lang.OutOfMemoryError: Out of swap space?
+ java.lang.OutOfMemoryError: Requested array size exceeds VM limit
+ Out of memory: Kill process or sacrifice child

#### Java heap space

**Demo1：**

```java
public class OOM {
    static final int SIZE = 2 * 1024 * 1024;
    public static void main(String[] args) {
        int[] i = new int[SIZE];
    }
}
```

运行如下：

```
$ javac OOM.java
$ java -Xmx12m -cp ./ OOM
Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
	at OOM.main(OOM.java:6)
```

> Note
>
> int[SIZE]实际需求内存大小 = 2 * 1024 * 1024 * 4字节 = 8m
>
> 经测试，当SIZE = 3 * 1024 *1024的时，分配18m的内存刚好OOM，当SIZE = 4 * 1024 * 1024时，分配24m内存刚好OOM，可知分配的内存大小必须大于等于需求大小的3/2。

**原因分析：** 没有足够的堆空间来运行程序

**解决方案：** 分配合适大小的内存

**Demo2：**

```java
import java.util.Map;
import java.util.HashMap;

public class OOM {
	static class Key {
		Integer id;

		Key(Integer id) {
			this.id = id;
		}

		@Override
		public int hashCode() {
			return id.hashCode();
		}
	}

	public static void main(String[] args) {
		Map<Key, String> m = new HashMap<Key, String>();

		while(true) {
			for (int i = 0; i < 1000; i++) {
				if (!m.containsKey(new Key(i))) {
					m.put(new Key(i), "number:" + i);
				}
			}
		}
	}
}
```

运行如下：

```java
$ javac OOM.java
$ java -Xmx10m -XX:+UseConcMarkSweepGC -cp ./ OOM
Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
	at java.lang.AbstractStringBuilder.<init>(AbstractStringBuilder.java:68)
	at java.lang.StringBuilder.<init>(StringBuilder.java:89)
	at OOM.main(OOM.java:24)
```

**原因分析：** `Key`中没有实现`equals`方法，导致`m.containsKey(new Key(i))` 一直为false，结果HashMap中元素一直增加，导致内存泄露。

**解决方案：** `Key` 实现自己的`equals`方法。

> Note
>
> 要想完全解决这个问题，就要好好提升编程能力。同时运用好Debugger、profiles、heap dump analyzers等工具，可以最大程度地避免内存泄漏问题。

####GC overhead limit exceeded

使用 **Demo2** 

运行如下：

```java
$ javac OOM.java
$ java -Xmx10m -XX:+UseParallelGC -cp ./ OOM
Exception in thread "main" java.lang.OutOfMemoryError: GC overhead limit exceeded
	at java.lang.Integer.valueOf(Integer.java:832)
	at OOM.main(OOM.java:24)
```

**原因分析：** HashMap中无限循环添加键值对，运行后就会抛出`GC overhead limit exceeded`。

**解决方案：** 使用profilers和memory dump analyzers这些工具，花费时间和精力来查找问题。

> Note
>
> 可以看出，同一个demo2，使用两种不同的GC算法，抛出的异常也不同。其实，在资源有限的情况下，根本无法预测应用是怎样挂掉的，什么时候会挂掉，所以在开发时，不能仅仅保证自己的应用程序在特定的环境下正常运行。

####Permgen space 和 Metaspace

**Demo3:**

```java
public class Metaspace {
    static javassist.ClassPool cp = javassist.ClassPool.getDefault();

    public static void main(String[] args) throws Exception{
        for (int i = 0; ; i++) { 
            Class c = cp.makeClass("eu.plumbr.demo.Generated" + i).toClass();
            System.out.println(i);
        }
    }
}
```

运行如下：

```java
$ javac Metaspace.java
$ java -XX: MaxMetaspaceSize = 32m -cp ./ Metaspace
Exception in thread "main" javassist.CannotCompileException: by java.lang.OutOfMemoryError: Metaspace
    at javassist.ClassPool.toClass(ClassPool.java:1170)
    at javassist.ClassPool.toClass(ClassPool.java:1113)
    at javassist.ClassPool.toClass(ClassPool.java:1071)
    at javassist.CtClass.toClass(CtClass.java:1275)
    at cn.moondev.book.Metaspace.main(Metaspace.java:12)
    .....
```

**原因分析：** 太多的类或者太大的类加载到metaspace

**解决方案：** 增加metaspace的大小，更改启动配置如下：

```java
// 告诉JVM：Metaspace允许增长到512，然后才能抛出异常
-XX: MaxMetaspaceSize = 512m
```

或者删除此参数，解除对metaspace大小的限制。

#### Unable to create native Thread

**Demo4**

```java
public class OOM {
    public static void main(String[] args) {
    	while(true) {
    		new Thread(new Runnable() {
    			public void run() {
    				try {
    					Thread.sleep(1000000);
    				} catch(InterruptedException e) {

    				}
    				
    			}
    		}).start();
    	}
    }
}
```

运行如下：

```java
$ javac OOM.java
$ java -cp ./ OOM
Exception in thread "main" java.lang.OutOfMemoryError: unable to create new native thread
	at java.lang.Thread.start0(Native Method)
	at java.lang.Thread.start(Thread.java:717)
	at OOM.main(OOM.java:13)
```

**原因分析：**

当JVM向OS请求创建一个新线程时，而线程数已经达到了OS的限制，无法创建新的native线程，抛出`Unable to create new native thread`错误。

**解决方案：**

最佳方案：分析你的应用是否真的需要创建如此多的线程来完成任务？是否可以使用线程池或者说线程池的数量是否合适？是否可以更合理的拆分业务来实现.....

当然，也可以通过在OS级别增加线程数限制来解决这个问题，例如：

```java
// macOS 10.12上执行
$ ulimit -u
709
```

#### Requested array size exceed VM limit

**Demo5:**

```java
public class OOM {
  public static void main(String[] args) {
  	for (int i = 3; i >= 0; i--) {
	    try {
	        int[] arr = new int[Integer.MAX_VALUE-i];
	        System.out.format("Successfully initialized an array with %,d elements.\n", Integer.MAX_VALUE-i);
	    } catch (Throwable t) {
	        t.printStackTrace();
	    }
		}
	}
}
```

运行如下：

```java
$ javac OOM.java
$ java -cp ./ OOM
java.lang.OutOfMemoryError: Java heap space
	at OOM.main(OOM.java:5)
java.lang.OutOfMemoryError: Java heap space
	at OOM.main(OOM.java:5)
java.lang.OutOfMemoryError: Requested array size exceeds VM limit
java.lang.OutOfMemoryError: Requested array size exceeds VM limit
```

**原因分析：**

该错误由JVM中的`native code`抛出。 JVM在为数组分配内存之前，会执行特定于平台的检查：分配的数据结构是否在此平台中是可寻址的。

初始化$2^{31}-1$个元素的数组需要8G的内存，超出了JVM使用的默认值，抛出`Java heap space`。第三次执行时，数组的大小达到`Integer.MAX_VALUE`，超出了JVM的支持。

**解决方案：**

检查代码，确定是否需要这么大的数组，可以减小数组的大小，分批处理。