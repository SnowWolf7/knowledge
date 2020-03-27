# 8 Classes

Class declarations define new reference types and describe how they are implemented (§8.1).

A *top level* class is a class that is not a nested class.

A *nested class* is any class whose declaration occurs within the body of another class or interface.

This chapter discusses the common semantics of all classes - top level (§7.6) and nested (including member classes (§8.5, §9.5), local classes (§14.3) and anonymous classes (§15.9.5)). Details that are specific to particular kinds of classes are discussed in the sections dedicated to these constructs.

A named class may be declared `abstract` (§8.1.1.1) and must be declared abstract if it is incompletely implemented; such a class cannot be instantiated, but can be extended by subclasses. A class may be declared `final` (§8.1.1.2), in which case it cannot have subclasses. If a class is declared `public`, then it can be referred to from other packages. Each class except `Object` is an extension of (that is, a subclass of) a single existing class (§8.1.4) and may implement interfaces (§8.1.5). Classes may be *generic* (§8.1.2), that is, they may declare type variables whose bindings may differ among different instances of the class.

Classes may be decorated with annotations (§9.7) just like any other kind of declaration.

The body of a class declares members (fields and methods and nested classes and interfaces), instance and static initializers, and constructors (§8.1.6). The scope (§6.3) of a member (§8.2) is the entire body of the declaration of the class to which the member belongs. Field, method, member class, member interface, and constructor declarations may include the access modifiers (§6.6) `public`, `protected`, or `private`. The members of a class include both declared and inherited members (§8.2). Newly declared fields can hide fields declared in a superclass or superinterface.  Newly declared class members and interface members can hide class or interface members declared in a superclass or superinterface. Newly declared methods can hide, implement, or override methods declared in a superclass or superinterface.

Field declarations (§8.3) describe class variables, which are incarnated once, and instance variables, which are freshly incarnated for each instance of the class. A field may be declared `final` (§8.3.1.2), in which case it can be assigned to only once. Any field declaration may include an initializer.

Member class declarations (§8.5) describe nested classes that are members of the surrounding class. Member classes may be static, in which case they have no access to the instance variables of the surrounding class; or they may be inner classes (§8.1.3).

Member interface declarations (§8.5) describe nested interfaces that are members of the surrounding class.

Method declarations (§8.4) describe code that may be invoked by method invocation expressions (§15.12). A class method is invoked relative to the class type; an instance method is invoked with respect to some particular object that is an instance of a class type. A method whose declaration does not indicate how it is implemented must be declared `abstract`. A method may be declared `final` (§8.4.3.3), in which case it cannot be hidden or overridden. A method may be implemented by platform-dependent `native` code (§8.4.3.4). A `synchronized` method (§8.4.3.6) automatically locks an object before executing its body and automatically unlocks the object on return, as if by use of a `synchronized` statement (§14.19), thus allowing its activities to be synchronized with those of other threads (§17 (Threads and Locks)).

Method names may be overloaded (§8.4.9).

Instance initializers (§8.6) are blocks of executable code that may be used to help initialize an instance when it is created (§15.9).

Static initializers (§8.7) are blocks of executable code that may be used to help initialize a class.

Constructors (§8.8) are similar to methods, but cannot be invoked directly by a method call; they are used to initialize new class instances. Like methods, they may be overloaded (§8.8.8).

### 8.1 Class Declarations

A class declaration specifies a new named reference type.

There are two kinds of class declarations: *normal class declarations* and *enum declarations*.

```java
ClassDeclaration:
	NormalClassDeclaration
  EnumDeclaration
    
NormalClassDeclaration:
	{ClassModifier} class Identifier [TypeParameters]
    [SuperClass] [SuperInterfaces] classBody
```

The rules in this section apply to all class declarations, including enum declarations. However, special rules apply to enum declarations with regard to class modifiers, inner classes, and superclasses; these rules are stated in §8.9.

The *Identifier* in a class declaration specifies the name of the class.

It is a compile-time error if a class has the same simple name as any of its enclosing classes or interfaces.

The scope and shadowing of a class declaration is specified in §6.3 and §6.4.

#### 8.1.1 Class Modifiers

A class declaration may include *class modifiers*.

```java
ClassModifier:
	(one of)
	Annotation public protected private
  abstract static final strictfp
```

The rules for annotation modifiers on a class declaration are specified in §9.7.4 and §9.7.5.

The access modifier `public` (§6.6) pertains only to top level classes (§7.6) and member classes (§8.5), not to local classes (§14.3) or anonymous classes (§15.9.5).

The access modifiers `protected` and `private` pertain only to member classes within a directly enclosing class declaration (§8.5).

The modifier `static` pertains only to member classes (§8.5.1), not to top level or local or anonymous classes.

It is a compile-time error if the same keyword appears more than once as a modifier for a class declaration.

​	<small>If two or more (distinct) class modifiers appear in a class declaration, then it is customary, though not required, that they appear in the order consistent with that shown above in the production for ClassModifier.</small>

##### 8.1.1.1 `abstract` Classes

An `abstract`
 class is a class that is incomplete, or to be considered incomplete.

It is a compile-time error if an attempt is made to create an instance of an `abstract` class using a class instance creation expression (§15.9.1).

A subclass of an `abstract` class that is not itself `abstract` may be instantiated, resulting in the execution of a constructor for the `abstract` class and, therefore, the execution of the field initializers for instance variables of that class.

A normal class may have `abstract` methods, that is, methods that are declared but not yet implemented (§8.4.3.1), only if it is an `abstract` class. It is a compile-time error if a normal class that is not `abstract` has an `abstract` method.

A class `C` has `abstract` methods if either of the following is true:

+ Any of the member methods (§8.2) of `C` - either declared or inherited - is `abstract`.
+ Any of `C`'s superclasses has an `abstract` method declared with package access, and there exists no method that overrides the `abstract` method from `C` or from a superclass of `C`.

It is a compile-time error to declare an `abstract` class type such that it is not possible to create a subclass that implements all of its `abstract` methods. This situation can occur if the class would have as members two `abstract` methods that have the same method signature (§8.4.2) but return types for which no type is return-type-substitutable with both (§8.4.5).

​	**Example 8.1.1.1-1. Abstract Class Declaration**

```java
abstract class Point {
  int x = 1;
  int y = 1;
  void move(int dx, int dy) {
    x += dx;
    y += dy;
    alert();
  }
  abstract void alert();
}

abstract class ColoredPoint extends Point {
  int color;
}

class SimplePoint extends Point {
  void alert() { }
}
```

​	<small>Here, a class `Point` is declared that must be declared `abstract`, because it contains a declaration of an `abstract` method named alert. The subclass of `Point` named `ColoredPoint` inherits the `abstract` method `alert`, so it must also be declared `abstract`. On the other hand, the subclass of `Point` named `SimplePoint` provides an implementation of `alert`, so it need not be `abstract`.</small>

​	<small>The statement:</small>

```java
Point point = new Point();
```

​	<small>would result in a compile-time error; the class `Point` cannot be instantiated because it is `abstract`. However, a `Point` variable could correctly be initialized with a reference to any subclass of `Point`, and the class `SimplePoint` is not `abstract`, so the statement:</small>

```java
Point point = new SimplePoint();
```

​	<small>would be correct. Instantiation of a `SimplePoint` causes the default constructor and field initializers for `x` and `y` of `Point` to be executed.</small>

​	**Example 8.1.1.1-2. Abstract Class Declaration that Prohibits Subclasses**

```java
interface colorable {
  void setColor(int color);
}

abstract class Colored implements colorable {
  abstract int setColor(int color);
}
```

​	<small>These declarations result in a compile-time error: it would be impossible for any subclass of class `Colored` to provide an implementation of a method named `setColor`, taking one argument of type int, that can satisfy both `abstract` method specifications, because the one in interface `Colorable` requires the same method to return no value, while the one in class `Colored` requires the same method to return a value of type `int` (§8.4).</small>

​	<small>A class type should be declared `abstract` only if the intent is that subclasses can be created to complete the implementation.  If the intent is simply to prevent instantiation of a class, the proper way to express this is to declare a constructor (§8.8.10) of no arguments, make it private, never invoke it, and declare no other constructors. A class of this form usually contains class methods and variables.</small>

​	<small>The class `Math` is an example of a class that cannot be instantiated; its declaration looks like this:</small>

```java
public final class Math {
  private Math() { } 	//never instantiate this class
 	. . . declarations of class variables and methods . . . 
}
```

##### 8.1.1.2 `final` Classes

A class can be declared `final` if its definition is complete and no subclasses are desired or required.

It is a compile-time error if the name of a `final` class appears in the `extends` clause (§8.1.4) of another class declaration; this implies that a `final` class cannot have any subclasses.

It is a compile-time error if a class is declared both `final` and `abstract`, because the implementation of such a class could never be completed (§8.1.1.1).

Because a `final` class never has any subclasses, the methods of a `final` class are never `overridden` (§8.4.8.1).

##### 8.1.1.3 `strictfp` Classes

The effect of the `strictfp` modifier is to make all `float` or `double` expressions within the class declaration (including within variable initializers, instance initializers, static initializers, and constructors) be explicitly FP-strict (§15.4).

This implies that all methods declared in the class, and all nested types declared in the class, are implicitly `strictfp`.

#### 8.1.2 Normal Classes and Type Parameters

A class is *generic* if it declares one or more type variables (§4.4).

These type variables are known as the *type parameters* of the class. The type parameter section follows the class name and is delimited by angle brackets.

```java
TypeParameters:
	<TypeParameterList>
    
TypeParameterList:
	TypeParameter {, TypeParameter}

The following productions from §4.4 are shown here for convenience:

TypeParameter:
	{TypeParameterModifier} Identifier [TypeBound]
    
TypeParameterModifier:
	Annotation

TypeBound:
	extends TypeVariable
  extends ClassOrInteraceType {AdditionalBound}

AdditionalBound:
	&InterfaceType
```

The rules for annotation modifiers on a type parameter declaration are specified in §9.7.4 and §9.7.5.

In a class's type parameter section, a type variable `T` directly depends on a type variable `S` if `S` is the bound of `T`, while `T` depends on `S` if either `T` directly depends on `S` or `T` directly depends on a type variable `U` that depends on `S` (using this definition recursively). It is a compile-time error if a type variable in a class's type parameter section depends on itself.

The scope and shadowing of a class's type parameter is specified in §6.3 and §6.4.

A generic class declaration defines a set of parameterized types (§4.5), one for each possible parameterization of the type parameter section by type arguments. All of these parameterized types share the same class at run time.

​	<small>For instance, executing the code:</small>

```
Vector<String> x = new Vector<String>();
Vector<Integer> y = new Vector<Integer>();
boolean b = x.getClass() == y.getClass();
```

​	<small>will result in variable `b` holding the value `true`.</small>

It is a compile-time error if a generic class is a direct or indirect subclass of `Throwable` (§11.1.1).

​	<small>The restriction is needed since the catch machanism of the Java Virtual Machine works only with non-generic classes.</small>

It is a compile-time error to refer to a type parameter of a generic class `C` in any of the following:

+ the declaration of a `static` member of `C` (§8.3.1.1, §8.4.3.2, §8.5.1).
+ the declaration of a `static` member of any type declaration nested within `C`.
+ a static initializer of `C` (§8.7), or
+ a static initializer of any class declaration nested within `C`.

**Example 8.1.2-1. Mutually Recursive Type Variable Bound**

```java
interface ConvertibleTo<T> {
  T convert();
}

class ReprChange<T extends ConveribleTo<S>, S extends ConveribleTo<T>> {
  T t;
  void setS(S s) { t = s.convert(); }
  S get() { return t.convert(); }
}
```

**Example 8.1.2-2. Nested Generic Classes**

```java
class Seq<T> {
  T head;
  Seq<T> tail;
  
  Seq() { this(null, null); }
  
  Seq(T head, Seq<T> tail) {
    this.head = head;
    this.tail = tail;
  }
  
  boolean isEmpty() { return tail == null; }
  
  class Zipper<S> {
    Seq<Pair<T, S>> zip(Sep<S> that) {
      if (isEmpty() || that.isEmpty()) {
        return new Seq<Pair<T,S>>();
      } else {
        Seq<T>.Zipper<S> tailZipper = tail.new Zipper<S>();
        return new Seq<Pair<T, S>> (
        	new Pair<T, S>(head, that.head),
          tailZipper.zip(that.tail));
      }
    }
  }
}

class Pair<T, S> {
  T fst; S snd;
  Pair(T f, S s) { fst = f; snd = s; }
}

class Test {
  public static void main(String[] args) {
    Seq<String> strs = new Seq<String>("a", new Seq<String>("b", new Seq<String>()));
    Seq<Number> nums = new Seq<Number>(new Integer(1),
                                       new Seq<Number>(new Double(1.5),
                                                       new Seq<Number>()));
    Seq<String>.Zipper<Number> zipper = strs.new Zipper<Number>();
    Seq<Pair<String, Number>> combined = zipper.zip(nums);
  }
}
```

#### 8.1.3 Inner Classes and Enclosing Instances

An *inner class* is a nested class that is not explicitly or implicitly declared `static`.

An inner class may be a non-`static` member class (§8.5), a local class (§14.3), or an anonymous class (§15.9.5). A member class of an interface is implicitly `static` (§9.5) so is never considered to be an inner class.

It is a compile-time error if an inner class declares a static initializer (§8.7).

It is a compile-time error if an inner class declares a member that is explicitly or implicitly `static`, unless the member is a constant variable (§4.12.4).

An inner class may inherit `static` members that are not constant variables even though it cannot declare them.

A nested class that is not an inner class may declare `static` members freely, in accordance with the usual rules of the Java programming language.

**Example 8.1.3-1. Inner Class Declarations and Static Members**

```java
class HasStatic {
  static int j = 100;
}

class Outer {
  class Inner extends HasStatic {
    static final int x = 3;	//OK: constant variable
    static int y = 4;				//Compile-time error: an inner class
  }
  static class NestedButNotInner {
    static int z = 5;				//OK: not an inner class
  }
  interface NeverInner {}		//Interface are never inner 
}
```

A statement or expression *occurs in a static context* if and only if the innermost method, constructor, instance initializer, static initializer, field initializer, or explicit constructor invocation statement enclosing the statement or expression is a static method, a static initializer, the variable initializer of a static variable, or an explicit constructor invocation statement (§8.8.7.1).

An *inner class* `C` is a direct inner class of a class or interface `O` if `O` is the immediately enclosing type declaration of `C` and the declaration of `C` does not occur in a static context.

A class `C` is an inner class of class or interface `O` if it is either a direct inner class of `O` or an inner class of an inner class of `O`.

​	<small>It is unusual, but possible, for the immediately enclosing type declaration of an inner class to be an interface. This only occurs if the class is declared in a default method body (§9.4). Specifically, it occurs if an anonymous or local class is declared in a default method body, or a member class is declared in the body of an anonymous class that is declared in a default method body.</small>

A class or interface `O` is the *zeroth lexically enclosing type declaration of itself*.

A class `O` is the *n'th lexically enclosing type declaration of a class `C`* if it is the immediately enclosing type declaration of the *n-1*'th lexically enclosing type declaration of `C`.

An instance `i` of a direct inner class `C` of a class or interface `O` is associated with an instance of `O`, known as the *immediately enclosing instance of `i`*. The immediately enclosing instance of an object, if any, is determined when the object is created (§15.9.2).

An object `o` is the *zeroth lexically enclosing instance of itself*.

An object `o` is the *n'th lexically enclosing instance of an instance `i`* if it is the immediately enclosing instance of the *n-1*'th lexically enclosing instance of `i`.

An instance of an inner class `I` whose declaration occurs in a static context has no lexically enclosing instances. However, if `I` is immediately declared within a static method or static initializer then `I` does have an enclosing block, which is the innermost block statement lexically enclosing the declaration of `I`.

For every superclass `S` of `C` which is itself a direct inner class of a class or interface `SO`, there is an instance of `SO` associated with `i`, known as the *immediately enclosing instance of `i` with respect to `S`*. The immediately enclosing instance of an object with respect to its class' direct superclass, if any, is determined when the superclass constructor is invoked via an explicit constructor invocation statement (§8.8.7.1).

When an inner class (whose declaration does not occur in a static context) refers to an instance variable that is a member of a lexically enclosing type declaration, the variable of the corresponding lexically enclosing instance is used.

Any local variable, formal parameter, or exception parameter used but not declared in an inner class must either be declared `final` or be effectively `final` (§4.12.4), or a compile-time error occurs where the use is attempted.

Any local variable used but not declared in an inner class must be definitely assigned (§16 (Definite Assignment)) before the body of the inner class, or a compile-time error occurs.

​	<small>Similar rules on variable use apply in the body of a lambda expression (§15.27.2).</small>

A blank `final` field (§4.12.4) of a lexically enclosing type declaration may not be assigned within an inner class, or a compile-time error occurs.

**Example 8.1.3-2. Inner Class Declarations**

```java
class Outer {
  int i = 100;
  static void classMethod() {
    final int l = 200;
    class LocalInStaticContext {
      int k = i;	//Compile-time error
      int m = l;	//OK
    }
  }
  void foo() {
    class Local {		//A local class
      int j = i;
    }
  }
}
```

​	<small>The declaration of class `LocalInStaticContext` occurs in a static context due to being within the static method `classMethod`.  Instance variables of class Outer are not available within the body of a static method. In particular, instance variables of `Outer` are not available inside the body of `LocalInStaticContext`. However, local variables from the surrounding method may be referred to without error (provided they are marked `final`). </small>

​	<small>Inner classes whose declarations do not occur in a static context may freely refer to the instance variables of their enclosing type declaration. An instance variable is always defined with respect to an instance.  In the case of instance variables of an enclosing type declaration, the instance variable must be defined with respect to an enclosing instance of that declared type. For example, the class `Local` above has an enclosing instance of class `Outer`. As a further example:</small>

```java
class withDeepNesting {
  boolean toBe;
	withDeepNesting(boolean b) { toBe = b; }
	
	class Nested {
    boolean theQuestion;
    class DeepNested {
      DeeplyNested() {
      theQuestion = toBe || !toBe;
    	}
    }
  }
}
```

​	<small>Here, every instance of `WithDeepNesting.Nested.DeeplyNested` has an enclosing instance of class `WithDeepNesting.Nested` (its immediately enclosing instance) and an enclosing instance of class `WithDeepNesting` (its 2nd lexically enclosing instance).</small>

#### 8.1.4 Superclasses and Subclasses

The optional `extends` clause in a normal class declaration specifies the *direct superclass* of the current class.

```java
SuperClass:
	extends ClassType
```

The `extends` clause must not appear in the definition of the class `Object`, or a compile-time error occurs, because it is the primordial class and has no direct superclass.

The *ClassType* must name an accessible class type (§6.6), or a compile-time error occurs.

It is a compile-time error if the *ClassType* names a class that is `final`, because `final` classes are not allowed to have subclasses (§8.1.1.2).

It is a compile-time error if the *ClassType* names the class `Enum` or any invocation of `Enum` (§8.9).

If the ClassType has type arguments, it must denote a well-formed parameterized type (§4.5), and none of the type arguments may be wildcard type arguments, or a compile-time error occurs.

Given a (possibly generic) class declaration `C<F1,...,Fn>` (n ≥ 0, C ≠ Object), the *direct superclass* of the class type `C<F1,...,Fn>` is the type given in the `extends` clause of the declaration of `C` if an `extends` clause is present, or `Object` otherwise.

*未完待续*

#### 8.1.5 SuperInterfaces

The optional `implements` clause in a class declaration lists the names of interfaces that are direct superinterfaces of the class being declared.

```
SuperInterfaces:
	implements InterfaceTypeList
	
InterfaceTypeList:
	InterfaceType {,InterfaceType}
```

Each *InterfaceType* must name an accessible interface type (§6.6), or a compiletime error occurs.

If an *InterfaceType* has type arguments, it must denote a well-formed parameterized type (§4.5), and none of the type arguments may be wildcard type arguments, or a compile-time error occurs.

It is a compile-time error if the same interface is mentioned as a direct superinterface more than once in a single `implements` clause. This is true even if the interface is named in different ways.

**Example 8.1.5-1. Illegal Superinterfaces**

```java
class Redundant implements java.lang.Cloneable, Cloneable {
	int x;
}
```

​	<small>This program results in a compile-time error because the names java.lang.Cloneable and Cloneable refer to the same interface.</small>

*未完待续*

#### 8.1.6 Class Body and Member Declarations

A *class body* may contain declarations of members of the class, that is, fields (§8.3), methods (§8.4), classes (§8.5), and interfaces (§8.5).

A class body may also contain instance initializers (§8.6), static initializers (§8.7), and declarations of constructors (§8.8) for the class.

```java
ClassBody:
	{{ClassBodyDeclaration}}

ClassBodyDeclaration:
	ClassMemberDeclaration
  InstanceInitializer
  StaticInitializer
  ConstructorDeclation
    
ClassMemberDeclation:
	FieldDeclaration
  MethodDeclaration
  ClassDeclaration
  InterfaceDeclaration
  ;
```

The scope and shadowing of a declaration of a member `m` declared in or inherited by a class type `C` is specified in §6.3 and §6.4.

​	<small>If `C` itself is a nested class, there may be definitions of the same kind (variable, method, or type) and name as `m` in enclosing scopes. (The scopes may be blocks, classes, or packages.) In all such cases, the member `m` declared in or inherited by `C` shadows (§6.4.1) the other definitions of the same kind and name.</small>

### 8.2 Method Declarations

### Method Signature

Two methods or constructors, ` M` and `N`, have the same signature if they have the same name, the same type parameters (if any) (&sect;8.4.4), and, after adapting the formal parameter types of `N` to the type parameters of `M`, the same formal parameter types.

The signature of a method $m_1$ is a subsignature of the signature of an method $m_2$ if either:

+ $m_2$ has the same signature as $m_1$, or
+ the signature of $m_1$ is the same as the erasure (&sect;4.6) of the signature of $m_2$.

Two method signatures $m_1$ and $m_2$ are override-equivalent iff either $m_1$ is a subsignature of $m_2$ or $m_2$ is a subsignature of $m_1$.

It is a compile-time error to declare two methods with override-equivalent signatures in a class.

​	**Example 8.4.2-1. Override-Equivalent Signatures**

```java
class point {
	int x, y;
  abstract void move(int dx, int dy);
  void move(int dx, int dy) { x += dx; y += dy; }
}
```

