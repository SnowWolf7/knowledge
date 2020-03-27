# **Name**

Names are used to refer to entities declared in a program.

A declared entity is a package, class type (normal or enum), interface type(normal or annotation type), member(class, interface, field, or method) of a reference type, type parameter (of a class, interface, method or constructor), parameter (to a method, constructor, or exception handler), or local variable.

Naming in programs are either *simple*, consisting of a single identifier, or *qualified*, consisting of a sequence of identifiers separated by “.” tokens(§6.2). 

Every declaration that introduces a name has a scope(§6.3), which is the part of the program text within which the declared entity can be refered to by a simple name.

A qualified name `N.x` may be used to refer a member of a package or reference type, where `N` is a simple or qualified name and `x` is an identifier. If `N` names a package, then `x` is a member of that type, which is either a class, an interface, a field, or a method. 

In determining the meaning of a name(§6.5), the context of the occurrence is used to dismbiguate among packages, types, variables, and methods with the same name.

Access control(§6.6) can be specified in a class, interface, method, or field declaration to control when *access* to a member is allowed. Access is different concept from scope. Access specifies the part of the program text within which the declared entity can be refered to by a qualified name. Access to a declared entity is also relevant in a field access expression(§15.11), a method invocation expression in which the method is not specified by a simple name(§15.12), a method reference expression(§15.13), or a qualified class instance creation expression(§15.9). In the absence of an access modifier, most declarations have package access, allowing access anywhere within the package that contains its declaration; other possibilities are `public`, `protected`, and `private`. 

Fully qualified and canonical names(§6.7) are also discussed in this chapter.

## **Declarations**

## **Names and Identifiers**

## **Scope of a Declaration**

The *scope* of a declaration is the region of the program within which the entity declared by the declaration can be referred to using a simple name, provided it is visible(§6.4.1). 

A declaration is said in *scope* at a particular point in program if and only if the declaration’s scope includes the point. 

The scope of the declaration of an observable(§7.4.3) top level package is all observable compilation units(§7.3).

The declaration of a package that is not observable is never in scope.

The declaration of a subpackage is never in scope.

The package `java` is always in scope. 

The scope of a type imported by a single-type-import declaration(§7.5.1) or a type-import-on-demand declaration(§7.5.2) is all the class and interface type declarations(§7.6) in the compilation unit in which the `import` declaration appears, as well as any annotations on the package declaration (if any) of the compilation unit. 

The scope of a member imported by a single-static-import declaration(&sect;7.5.3) or a static-import-on-demand declaration(&sect;7.5.4) is all the class and interface type declarations(&sect;7.6) in the compilation unit in which the `import` declaration appears, as well as any annotations on the package declaration (if any) of the compilation unit.

The scope of a top level (&sect;7.6) is all type declarations in the package in which the top level type is declared.

The scope of a declaration of  a member `m` declared in or inherited by a class type `C` (&sect;8.1.6) is the entire body of `C`, including any nested type declarations.

The scope of a declaration of a member `m` declared in or inherited by a interface type `I`(&sect;9.1.4) is the entire body of `I`, including any nested type declarations.

The scope of an enum constant `C` declared in an enum type `T` is the body of `T`, and any `case` label of a `switch` statement whose expression is of enum type `T`.

The scope of a formal parameter of a method (&sect;8.4.1), constructors (&sect;8.8.1), or lambda expression (&sect;15.27) is the entire body of the method, constructor, or lambda expression.

The scope of a class's type parameter(&sect;8.1.2) is the type parameter section of the class declaration, the type parameter section of any superclass or superinterface of the class declaration, and the class body.

The scope of a interface's type parameter(&sect;9.1.2) is the type parameter section of the interface declaration, the type parameter section of any superinterface of the interface declaration, and the interface body.

The scope of a method's type parameter(&sect;8.4.4) is the entire declaration of the method, including the type parameter section, but excluding the method modifiers.

The scope of a constructor's type parameter(&sect;8.8.4) is the ectire declaration of the constructor, including the type parameter section, but excluding the constructor modifiers.

The scope of a local class declaration immediately enclosed by a block(&sect;14.2) is the rest of the immediately enclosing block, including its own class declaration.

The scope of a local class declaration immediately enclosed by a switch block statement group(&sect;14.11) is the rest of the immediately enclosing switch block statement group, including its own class declaration.

The scope of a local variable declaration in a block is the rest of the block in which the declaration appears, starting with its own initializer and including any further declarators to the right in the local variable declaration statement.

The scope of a local variable declaration in the `ForInit` part of a basic `for` statement(&sect;14.14.1) includes all of the following:

+ Its own initializer
+ Any further declarators to the right in the `ForInit` part of the `for` statement
+ The `Expression` and `ForUpdate` parts of the `for` statement
+ The contained `Statement`

The scope of a local variable declared in the `FormalParameter` part of an enhanced `for` statement(&sect;14.14.2) is the contained `Statement`.

The scope of a parameter of an exception handler that is declared in a `catch` clause of a `try` statement(&sect;14.20) is the entire block associated with the `catch`.

The scope of a variable declared in the`ResourceSpecification` of a `try`-with-resources statement(&sect;14.20.3) is from the declaration rightward over the remainder of the `ResourceSpecification` and the entire `try` block associated with the `try`-with-resources statement.

<small>		The translation of a `try`-with-resources statement implies the rule above.</small>

**Example 6.3-1. Scope of Type Declarations**

These rules imply that declarations of class and interface types need not appear before uses of the types. In the following program, the use of `PointList` in class `Point` is valid, because the scope of the class declaration `PointList` includes both class `Point` and class `PointList`, as well as any other type declarations in other compilation units of package `points`.

```java
package points;
class Point {
  int x, y;
  PointList list;
  Point next;
}

class PointList {
  Point first;
}
```

**Example 6.3.2 Scope of Local Variable Declarations**

The following program causes a compile-time error because the initialization of local variable `x` is within the scope of the declaration of local variable `x`, but the local variable `x` does not yet have a value and cannot be use. The field `x` has a value of 0 (assigned when `Test1` was initialized) but is a red herring since it is shadowed(&sect;6.4.1) by the local variable `x`.

```java
class Test1 {
  static int x;
  public static void main(String[] args) {
    int x = x;
  }
}
```

The following program does compile:

```java 
class Test2 {
  static int x;
  public static void main(String[] args) {
    int x = (x=2)*2;
    System.out.println(x);
  }
}
```

because the local variable `x` is definitely assigned(&sect;16(Definite Assignment)) before it is used. It prints: `4`.

In the following program, the initializer for `three` can correctly refer to the variable `two` declared in an earlier declarator, and the method invocation in the next line can correctly refer to the variable `three` declared earlier in the block.

```java
class Test3 {
	public static void main(String[] args) {
    System.out.print("2+1=");
    int two = 2, three = two + 1;
    System.out.println(three);
  }
}
```

## Shadowing and Obscuring









