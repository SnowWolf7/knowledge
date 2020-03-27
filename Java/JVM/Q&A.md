**Q：Java虚拟机的运行内存分配（Run-Time Data Area）**

**A**：分为两类，一种是和JVM的生命周期一致，另一种和线程的生命周期一致。即线程私有和线程共享。

+ 程序计数器（pc register）：保存正在执行的JVM指令地址，如果the current method is native，则pc register值为undefined。 线程私有。
+ JVM栈：保存frames。JVM只push和pop frames，frames may be heap allocated。A *frame* is used to store data and partial results, as well as to perform dynamic linking, return values for methods, and dispatch exceptions. 大小可能是固定的，也可能是动态的。线程私有。
+ 堆：保存所有的class instances和arrays。Heap storage由GC回收。线程共享。
+ 方法区：保存per-class structures，例如run-time constant pool, field and method data, and the code for methods and constructors, including the special methods (&sect;2.9) used in class and instance initialization and interface initialization. 线程共享。
+ Native方法栈：与JVM栈类似，但是服务与Native方法，线程私有。

---

**Q：Java内存堆和栈的区别**

**A：** 

+ 栈线程私有、堆线程共享。
+ 栈存储的是frames，堆存储的是class instances和arrays。

---

**Q：GC回收机制**

**A：** 