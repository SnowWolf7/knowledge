## Memory Management in the Java HotSpot Virtual Machine

### 1 Introduction

One strength of the Java™ 2 Platform, Standard Edition (J2SE™) is that it performs automatic memory management, thereby shielding the developer from the complexity of explicit memory management.

This paper provides a broad overview of memory management in the Java HotSpot virtual machine (JVM) in Sun’s J2SE 5.0 release. It describes the garbage collectors available to perform the memory management, and gives some advice regarding choosing and configuring a collector and setting sizes for the memory areas on which the collector operates. It also serves as a resource, listing some of the most commonly-used options that affect garbage collector behavior and providing numerous links to more detailed documentation.

Section 2 is for readers who are new to the concept of automatic memory management. It has a brief discussion of the benefits of such management versus requiring programmers to explicitly deallocate space for data. Section 3 then presents an overview of general garbage collection concepts, design choices, and performance metrics. It also introduces a commonly-used organization of memory into different areas called generations based on the expected lifetimes of objects. This separation into generations has proven effective at reducing garbage collection pause times and overall costs in a wide range of applications.

The rest of the paper provides information specific to the HotSpot JVM. Section 4 describes the four garbage collectors that are available, including one that is new in J2SE 5.0 update 6, and documents the generational memory organization they all utilize. For each collector, Section 4 summarizes the types of collection algorithms used and specifies when it would be appropriate to choose that collector.

Section 5 describes a technique new in the J2SE 5.0 release that combines (1) automatic selection of garbage collector, heap sizes, and HotSpot JVM (client or server) based on the platform and operating system on which the application is running, and (2) dynamic garbage collection tuning based on user-specified desired behavior. This technique is referred to as ergonomics.

Section 6 provides recommendations for selecting and configuring a garbage collector. It also provides some advice as to what to do about `OutOfMemoryErrors`. Section 7 briefly describes some of the tools that can be utilized to evaluate garbage collection performance, and Section 8 lists the most commonly-used command line options that relate to garbage collector selection and behavior. Finally, Section 9 supplies links to more detailed documentation for the various topics covered by this paper.

### 2 Explicit vs. Automantic Memory Management

### 3 Garbage Collection Concepts

A garbage collector is responsible for

+ allocating memory
+ ensuring that any referenced objects remain in memory, and 
+ recovering memory used by objects that are no longer reachable from references in executing code. 

Objects that are referenced are said to be live. Objects that are no longer referenced are considered dead and are termed garbage. The process of finding and freeing (also known as reclaiming) the space used by these objects is known as garbage collection. 

Garbage collection solves many, but not all, memory allocation problems. You could, for example, create objects indefinitely and continue referencing them until there is no more memory available. Garbage collection is also a complex task taking time and resources of its own.

The precise algorithm used to organize memory and allocate and deallocate space is handled by the garbage collector and hidden from the programmer. Space is commonly allocated from a large pool of memory referred to as the heap.

The timing of garbage collection is up to the garbage collector. Typically, the entire heap or a subpart of it is collected either when it fills up or when it reaches a threshold percentage of occupancy.

The task of fulfilling an allocation request, which involves finding a block of unused memory of a certain size in the heap, is a difficult one. The main problem for most dynamic memory allocation algorithms is to avoid fragmentation (see below), while keeping both allocation and deallocation efficient. 

**Desirable Garbage Collector Characteristics**

A garbage collector must be both safe and comprehensive. That is, live data must never be erroneously freed, and garbage should not remain unclaimed for more than a small number of collection cycles.

It is also desirable that a garbage collector operate efficiently, without introducing long pauses during which the application is not running. However, as with most computer-related systems, there are often trade-offs between time, space, and frequency. For example, if a heap size is small, collection will be fast but the heap will fill up more quickly, thus requiring more frequent collections. Conversely, a large heap will take longer to fill up and thus collections will be less frequent, but they may take longer.

Another desirable garbage collector characteristic is the limitation of fragmentation. When the memory for garbage objects is freed, the free space may appear in small chunks in various areas such that there might not be enough space in any one contiguous area to be used for allocation of a large object. One approach to eliminating fragmentation is called compaction, discussed among the various garbage collector design choices below.

Scalability is also important. Allocation should not become a scalability bottleneck for multithreaded applications on multiprocessor systems, and collection should also not be such a bottleneck.

**Design Choices**

A number of choices must be made when designing or selecting a garbage collection algorithm:

+ **Serial versus Parallel.** With serial collection, only one thing happens at a time. For example, even when multiple CPUs are available, only one is utilized to perform the collection. When parallel collection is used, the task of garbage collection is split into parts and those subparts are executed simultaneously, on different CPUs.  The simultaneous operation enables the collection to be done more quickly, at the expense of some additional complexity and potential fragmentation.
+ **Concurrent versus Stop-the-world.** When stop-the-world garbage collection is performed, execution of the application is completely suspended during the collection. Alternatively, one or more garbage collection tasks can be executed concurrently, that is, simultaneously, with the application. Typically, a concurrent garbage collector does most of its work concurrently, but may also occasionally have to do a few short stop-the-world pauses. Stop-the-world garbage collection is simpler than concurrent collection, since the heap is frozen and objects are not changing during the collection. Its disadvantage is that it may be undesirable for some applications to be paused. Correspondingly, the pause times are shorter when garbage collection is done concurrently, but the collector must take extra care, as it is operating over objects that might be updated at the same time by the application. This adds some overhead to concurrent collectors that affects performance and requires a larger heap size.
+ **Compacting versus Non-compacting versus Copying.** After a garbage collector has determined which objects in memory are live and which are garbage, it can compact the memory, moving all the live objects together and completely reclaiming the remaining memory. After compaction, it is easy and fast to allocate a new object at the first free location. A simple pointer can be utilized to keep track of the next location available for object allocation. In contrast with a compacting collector, a *non-compacting* collector releases the space utilized by garbage objects *in-place*, i.e., it does not move all live objects to create a large reclaimed region in the same way a compacting collector does. The benefit is faster completion of garbage collection, but the drawback is potential fragmentation.  In general, it is more expensive to allocate from a heap with in-place deallocation than from a compacted heap.  It may be necessary to search the heap for a contiguous area of memory sufficiently large to accommodate the new object. A third alternative is a *copying* collector, which copies (or *evacuates*) live objects to a different memory area. The benefit is that the source area can then be considered empty and available for fast and easy subsequent allocations, but the drawback is the additional time required for copying and the extra space that may be required.

**Performance Metrics**

Several metrics are utilized to evaluate garbage collector performance, including:

+ **Throughput** — the percentage of total time not spent in garbage collection, considered over long periods of time
+ **Garbage collection overhead** — the inverse of throughput, that is, the percentage of total time spent in garbage collection.
+ **Pause time** — the length of time during which application execution is stopped while garbage collection is occurring.
+ **Frequency of collection** — how often collection occurs, relative to application execution.
+ **Footprint** — a measure of size, such as heap size.
+ **Promptness** — the time between when an object becomes garbage and when the memory becomes available.

An interactive application might require low pause times, whereas overall execution time is more important to a non-interactive one. A real-time application would demand small upper bounds on both garbage collection pauses and the proportion of time spent in the collector in any period. A small footprint might be the main concern of an application running in a small personal computer or embedded system.

**Generational Collection**

When a technique called generational collection is used, memory is divided into *generations*, that is, separate pools holding objects of different ages. For example, the most widely-used configuration has two generations: one for young objects and one for old objects.

Different algorithms can be used to perform garbage collection in the different generations, each algorithm optimized based on commonly observed characteristics for that particular generation. Generational garbage collection exploits the following observations, known as the *weak generational hypothesis*, regarding applications written in several programming languages, including the Java programming language:

+ Most allocated objects are not referenced (considered live) for long, that is, they die young.
+ Few references from older to younger objects exist.

Young generation collections occur relatively frequently and are efficient and fast because the young generation space is usually small and likely to contain a lot of objects that are no longer referenced.

Objects that survive some number of young generation collections are eventually *promoted*, or *tenured*, to the old generation. See Figure 1. This generation is typically larger than the young generation and its occupancy grows more slowly. As a result, old generation collections are infrequent, but take significantly longer to complete.

![](/Users/SnowWolf7/Desktop/技术储备/Java/JVM/HotSpot VM/图/Generational garbage collection.png)

The garbage collection algorithm chosen for a young generation typically puts a premium on speed, since young generation collections are frequent. On the other hand, the old generation is typically managed by an algorithm that is more space efficient, because the old generation takes up most of the heap and old generation algorithms have to work well with low garbage densities. 	

### 4 Garbage Collector in the J2SE 5.0 Hotspot VM

The Java HotSpot virtual machine includes four garbage collectors as of J2SE 5.0 update 6. All the collectors are generational. This section describes the generations and the types of collections, and discusses why object allocations are often fast and efficient. It then provides detailed information about each collector.

#### HotSpot Generations

Memory in the Java HotSpot virtual machine is organized into three generations: a young generation, an old generation, and a permanent generation. Most objects are initially allocated in the young generation. The old generation contains objects that have survived some number of young generation collections, as well as some large objects that may be allocated directly in the old generation. The permanent generation holds objects that the JVM finds convenient to have the garbage collector manage, such as objects describing classes and methods, as well as the classes and methods themselves.

The young generation consists of an area called Eden plus two smaller survivor spaces, as shown in Figure 2. Most objects are initially allocated in Eden. (As mentioned, a few large objects may be allocated directly in the old generation.) The survivor spaces hold objects that have survived at least one young generation collection and have thus been given additional chances to die before being considered “old enough” to be promoted to the old generation.  At any given time, one of the survivor spaces (labeled *From* in the figure) holds such objects, while the other is empty and remains unused until the next collection.

![](/Users/SnowWolf7/Desktop/技术储备/Java/JVM/HotSpot VM/图/Young generation memory areas.png)

#### Garbage Collection Types

When the young generation fills up, a young generation collection (sometimes referred to as a minor collection) of just that generation is performed. When the old or permanent generation fills up, what is known as a full collection (sometimes referred to as a major collection) is typically done.  That is, all generations are collected. Commonly, the young generation is collected first, using the collection algorithm designed specifically for that generation, because it is usually the most efficient algorithm for identifying garbage in the young generation. Then what is referred to below as the old generation collection algorithm for a given collector is run on both the old and permanent generations. If compaction occurs, each generation is compacted separately.

Sometimes the old generation is too full to accept all the objects that would be likely to be promoted from the young generation to the old generation if the young generation was collected first. In that case, for all but the CMS collector, the young generation collection algorithm is not run. Instead, the old generation collection algorithm is used on the entire heap. (The CMS old generation algorithm is a special case because it cannot collect the young generation.)

#### Fast Allocation

As you will see from the garbage collector descriptions below, in many cases there are large contiguous blocks of memory available from which to allocate objects. Allocations from such blocks are efficient, using a simple *bump-the-pointer* technique.  That is, the end of the previously allocated object is always kept track of. When a new allocation request needs to be satisfied, all that needs to be done is to check whether the object will fit in the remaining part of the generation and, if so, to update the pointer and initialize the object.

For multithreaded applications, allocation operations need to be multithread-safe. If global locks were used to ensure this, then allocation into a generation would become a bottleneck and degrade performance. Instead, the HotSpot JVM has adopted a technique called Thread-Local Allocation Buffers (TLABs). This improves multithreaded allocation throughput by giving each thread its own buffer (i.e., a small portion of the generation) from which to allocate.  Since only one thread can be allocating into each TLAB, allocation can take place quickly by utilizing the bump-the-pointer technique, without requiring any locking. Only infrequently, when a thread fills up its TLAB and needs to get a new one, must synchronization be utilized. Several techniques to minimize space wastage due to the use of TLABs are employed. For example, TLABs are sized by the allocator to waste less than 1% of Eden, on average. The combination of the use of TLABs and linear allocations using the bump-the-pointer technique enables each allocation to be efficient, only requiring around 10 native instructions.

#### Serial Collector

With the serial collector, both young and old collections are done serially (using a single CPU), in a stop-theworld fashion. That is, application execution is halted while collection is taking place.

+ **Young Generation Collection Using the Serial Collector.** Figure 3 illustrates the operation of a young generation collection using the serial collector. The live objects in Eden are copied to the initially empty survivor space, labeled To in the figure, except for ones that are too large to fit comfortably in the To space. Such objects are directly copied to the old generation. The live objects in the occupied survivor space (labeled From) that are still relatively young are also copied to the other survivor space, while objects that are relatively old are copied to the old generation. Note: If the To space becomes full, the live objects from Eden or From that have not been copied to it are tenured, regardless of how many young generation collections they have survived. Any objects remaining in Eden or the From space after live objects have been copied are, by definition, not live, and they do not need to be examined. (These garbage objects are marked with an X in the figure, though in fact the collector does not examine or mark these objects.)

    ![](/Users/SnowWolf7/Desktop/技术储备/Java/JVM/HotSpot VM/图/Serial young generation collection.png)

    After a young generation collection is complete, both Eden and the formerly occupied survivor space are empty and only the formerly empty survivor space contains live objects. At this point, the survivor spaces swap roles. See Figure 4.

    ![](/Users/SnowWolf7/Desktop/技术储备/Java/JVM/HotSpot VM/图/After a young generation collection.png)

+ **Old Generation Collection Using the Serial Collector.** With the serial collector, the old and permanent generations are collected via a mark-sweep-compact collection algorithm. In the mark phase, the collector identifies which objects are still live. The sweep phase “sweeps” over the generations, identifying garbage. The collector then performs sliding compaction, sliding the live objects towards the beginning of the old generation space (and similarly for the permanent generation), leaving any free space in a single contiguous chunk at the opposite end. See Figure 5. The compaction allows any future allocations into the old or permanent generation to use the fast, bump-the-pointer technique.

    ![](/Users/SnowWolf7/Desktop/技术储备/Java/JVM/HotSpot VM/图/Compaction of the old generation.png)

+ **When to Use the Serial Collector.** The serial collector is the collector of choice for most applications that are run on client-style machines and that do not have a requirement for low pause times. On today’s hardware, the serial collector can efficiently manage a lot of nontrivial applications with 64MB heaps and relatively short worst-case pauses of less than half a second for full collections.

+ **Serial Collector Selection.** In the J2SE 5.0 release, the serial collector is automatically chosen as the default garbage collector on machines that are not server-class machines, as described in Section 5. On other machines, the serial collector can be explicitly requested by using the `-XX:+UseSerialGC` command line option.

#### Parallel Collector

#### Parallel Compact Collector

#### Concurrent Mark-Sweep (CMS) Collector

### 5 Ergonomics - Automatic Selection and Behavior Tuning

### 6 Recommandations

**What to Do about OutOfMemoryError**

One common issue that many developers have to address is that of applications that terminate with `java.lang.OutOfMemoryError`. That error is thrown when there is insufficient space to allocate an object. That is, garbage collection cannot make any further space available to accommodate a new object, and the heap cannot be further expanded. An `OutOfMemoryError` does not necessarily imply a memory leak. The issue might simply be a configuration issue, for example if the specified heap size (or the default size if not specified) is insufficient for the application. 

The first step in diagnosing an `OutOfMemoryError` is to examine the full error message.  In the exception message, further information is supplied after “`java.lang.OutOfMemoryError`”. Here are some common examples of what that additional information may be, what it may mean, and what to do about it: 

+ **Java heap space.** This indicates that an object could not be allocated in the heap. The issue may be just a configuration problem. You could get this error, for example, if the maximum heap size specified by the –Xmx command line option (or selected by default) is insufficient for the application.  It could also be an indication that objects that are no longer needed cannot be garbage collected because the application is unintentionally holding references to them. The HAT tool (see Section 7) can be used to view all reachable objects and understand which references are keeping each one alive.  One other potential source of this error could be the excessive use of finalizers by the application such that the thread to invoke the finalizers cannot keep up with the rate of addition of finalizers to the queue. The `jconsole` management tool can be used to monitor the number of objects that are pending finalization.
+ **PermGen space.** This indicates that the permanent generation is full. As described earlier, that is the area of the heap where the JVM stores its metadata. If an application loads a large number of classes, then the permanent generation may need to be increased. You can do so by specifying the command line option $–XX:MaxPermSize=n$, where n specifies the size.
+ **Requested array size exceeds VM limit.** This indicates that the application attempted to allocate an array that is larger than the heap size. For example, if an application tries to allocate an array of 512MB but the maximum heap size is 256MB, then this error will be thrown. In most cases the problem is likely to be either that the heap size is too small or that a bug results in the application attempting to create an array whose size is calculated to be incorrectly huge. 

Some of the tools described in Section 7 can be utilized to diagnose OutOfMemoryError problems. A few of the most useful tools for this task are the Heap Analysis Tool (HAT), the jconsole management tool, and the jmap tool with the –histo option.

### 7 Tools to Evaluate Garbage Collection Performance

###  8 Key Options Related to Garbage Collection

### 9 For More Information

