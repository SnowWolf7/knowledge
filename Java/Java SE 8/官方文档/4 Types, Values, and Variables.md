# Types, Values, and Variables

The Java programming language is a *statically* typed  language, which means that every variable and every expression has a type that is known at compile time.  

The Java programming language is also a *strongly typed*  language, because types limit the values that a variable (§4.12) can hold or that an expression can produce, limit the operations supported on those values, and determine the meaning of the operations. Strong static typing helps detect errors at compile time. 

The types of the Java programming language are divided into two categories: primitive types and reference types. The primitive types (§4.2) are the boolean type and the numeric types. The numeric types are the integral types byte, short, int, long, and char, and the floating-point types float and double. The reference types (§4.3) are class types, interface types, and array types. There is also a special null type. An object (§4.3.1) is a dynamically created instance of a class type or a dynamically created array. The values of a reference type are references to objects. All objects, including arrays, support the methods of class Object (§4.3.2). String literals are represented by String objects (§4.3.3).

## 4.1 The Kinds of Types and Values

There are two kinds of types in the Java programming language: primitive types (§4.2) and reference types (§4.3). There are, correspondingly, two kinds of data values that can be stored in variables, passed as arguments, returned by methods, and operated on: primitive values (§4.2) and reference values (§4.3).

```java
Type:
	PrimitiveType
	ReferenceType
```

There is also a special *null type*, the type of the expression null (§3.10.7, §15.8.1), which has no name. 

Because the null type has no name, it is impossible to declare a variable of the null type or to cast to the null type. 

The null reference is the only possible value of an expression of null type.  

The null reference can always be assigned or cast to any reference type (§5.2, §5.3, §5.5). 

​	<small>In practice, the programmer can ignore the null type and just pretend that null is merely a special literal that can be of any reference type.</small>

## 4.2 Primitive Types and Values

A primitive type is predefined by the Java programming language and named by its reserved keyword (§3.9):

```java
PrimitiveType:
	{Annotation} NumericType
	{Annotation} boolean

NumericType:
	IntegralType
	FloatingPointType

IntegralType:
	(one of)
	byte short int long char

FloatingPointType:
	(one of)
	float double
```

Primitive values do not share state with other primitive values.

The *numeric types* are the integral types and the floating-point types.

The *integral types* are byte, short, int, and long, whose values are 8-bit, 16-bit, 32-bit and 64-bit signed two's-complement integers, respectively, and char, whose values are 16-bit unsigned integers representing UTF-16 code units (§3.1).

## 4.4 Type variables

A type variable is an unqualified identifier used as a type in class, interface, method, and constructor bodies.

A type variable is introduced by the declaration of a type parameter of a generic class, interface, method, or constructor.

```java
TypeParameter:
	{TypeParameterModifier} Identifier [TypeBound]
TypeParameterModifier:
	Annotation
TypeBound:
	extends TypeVariable
	extends ClassOrInterfaceType {AdditionalBound}
AdditionalBound:
	&InterfaceType
```

The scope of a type variable declared as a type parameter is specified in &sect;6.3.

Every type variable declared as a type parameter has a bound. If no bound is declared for a type variable, Object is assumed. If a bound is declared, it consists of either:

+ a single type variable T, or
+ a class or interface type *T* possibly followed by interface types *I<sub>1</sub> & … & I<sub>n</sub>*

The erasures(&sect;4.6) of all constituent type of a bound  must be pairwise different, or a compile-time error occurs.

A type variable must not at the same time be a subtype of two interfaces types which are different parameterizations of the same generic interface, or a compile-time error occurs.

The order of types in a bound is only significant in that the erasure of a type variable is determined by the first type in its bound, and that a class type or type variable may only appear in the first position.

The members of a type variable *X* with bound *T & I<sub>1</sub> & ... & I<sub>n</sub>* are the members of the intersection type(&sect;4.9) T & I<sub>1</sub> & ... & I<sub>n</sub>*  appearing at the point where the type variable is declared.

**Example 4.4-1. Members of a Type Variable**

```java
package TypeVarMembers
class C {
  public void mCPublic() {}
  protected void mCProtected() {}
  					void mCPackage() {}
  private void mCPrivate() {}
}
interface I {
  void mI();
}
class CT extends C implements I {
  public void mI() {}
}
class Test {
	<T extends C & T> void test(T t) {
    t.mI();							//OK
    t.mCPublic();				//OK
    t.mCProtected();		//OK
    t.mCPackage();			//OK
    t.mCPrivate();			//Compile-time error
  }
}
```

The type *T* has the same members as the intersection type *C & T*, which in turn has the same members as the empty class *CT*, defined in the same scope with equivalent supertypes. The members of an interface are always *public*, and therefore always inherited(unless overridden). Hence *mI* is a member of *CT* and of *T*. Among the members of *C*, all but *mCPrivate* are inherited by *CT*, and are therefore members of both *CT* and *T*.

If *C* had been declared in a different package than *T*, then the call to *mCPackage* would give rise to a compile-time error, as that member would not be accessible at the point where *T* is declared.

## 4.5 Parameterized Types

A class or interface declaration that is generic(&sect;8.1.2, &sect;9.1.2) defines a set of *parameterized types*.

## 4.6 Type Erasure

## 4.7 Reifible Types

## 4.8 Raw Types

## 4.9 Intersection Types

## 4.10 Subtyping

## 4.11 Where Types Are Used

## 4.12 Variables

