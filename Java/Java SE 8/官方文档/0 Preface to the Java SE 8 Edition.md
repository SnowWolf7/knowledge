# Preface to the Java SE 8 Edition
@(Java)
---
Java SE 8代表了Java历史上最大的Java语言演变。相对较少的特性--**lambda expressions, method references, and functional interfaces**--conbine to 提供一种融合object-oriented和functional styles的编程模型。这种融合的方式鼓励最佳实践--**immutability, statelessness, compositionality**--同时保留"the feel of java" -- **readability, simplicity, universality**。 
至关重要的是，Java SE平台的libraries和Java共同发展。这意味着使用lambda expressions 和 method references来表达行为具有生产性和高性能“开箱即用”--例如要应用于列表中每个元素的操作。以类似的方式，JVM和Java共同发展，以确保默认方法在编译时间和运行时间尽可能一致地支持库演化，考虑到单独编译的限制。
自20世纪90年代以来，为Java语言添加一流功能的举措已经出现。 2007年左右的BGGA和CICE提案为该主题带来了新的活力，而2009年左右在OpenJDK创建的Lambda项目吸引了前所未有的兴趣。
在Java SE 7中向JVM添加menthod handles（方法句柄）打开了通向新的实现技术的大门，同时保留了“write once, run anywhere"。随着时间的推移，Java的新变化--Lambda Expressions得到了JSR 335的监督。
在JSR 335中，最大的复杂性潜伏在**implicitly typed lambda expressions**和**overload resolution**的交互中。 在这个以及许多其他领域，Oracle的Dan Smith在**thoroughly specifying the desired behavior**方面做得非常出色。 在整个说明书中都可以找到他的话，包括关于**type inference**的全新章节。
Java SE 8中的另一项举措是增强`annotation`的实用性，`annotation`是Java语言最受欢迎的功能之一。