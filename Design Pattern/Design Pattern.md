## Design Pattern

By definition, Design Patterns are reusable solutions to commonly occuring problems(in the context of software design). Design patterns were started as best practices that were applied again and again to similar problems encountered in different contexts. They become popular after they were collected, in a formalized form, in the Gang Of Four book in 1994. Originally published with c++ and smaltalk code samples, design patterns are very popular in Java and C# can be applied in all object oriented languanges. In functional languages like Scala, certain patterns are not necesary anymore.

### 1 Creational Design Patterns

#### 1.1 Singleton

Ensure that only one instance of a class is created and Provide a global access point to the object.

**When to use**

Singleton should be used when we must ensure that only one instance of a class is created and when the instance must be available through the code. A special care should be taken in multi-threading environments when multiple threads must access the same resources through the same singleton object.

**Common usage**

There are many common situations when singleton pattern is used:

+ Logger Classes
+ Confituration Classes
+ Accessing resources in shared mode
+ Other design patterns implemented as Singletons: Factories and Abstract Factories, Builder, Prototype 

#### 1.2 Factory

Simplified version of Factory Method. Creates objects without exposing the instantiation logic to the client and Refers to the newly created object through a common interface.

#### 1.3 Factory Method

Defines an interface for creating objects, but let subclasses to decide which class to instantiate and Refers to the newly created object through a common interface.

#### 1.4 Abstract Factory

Offers the interface for creating a family of related objects, without explicitly specifying their classes. 

#### 1.5 Builder

Defines an instance for creating an object but letting subclasses decide which class to instantiate and Allows a finer control over the construction process.

#### 1.6 Prototype

Specify the kinds of objects to create using a prototypical instance, and creating new objects by coping this prototype.

#### 1.7 Object Pool

Reuses and shares the objects that are expensive to create.

### 2 Behavioral Design Patterns

#### 2.1 Chain of Responsibility

#### 2.2 Command

Encapsulate a request in an object, Allows the parameterization of clients with different requests and Allows saving the requests in a queue.

#### 2.3 Interpreter

Given a language, define a representation for its grammer along with an interpreter that uses the representation to interpret.

#### 2.4 Iterator

Provide a way to access the elements of an aggregate object sequentially without exposing its underlying representation.

####2.5 Mediator

#### 2.6 Observer

Define a one-to-many dependency between objects so that when one object changes state, all its dependents are notified and updated automatically.

#### 2.7 Strategy

Define a family of algorithms, encapsulate each one , and make them interchangeable. Strategy lets the algorithm vary independently from clients that use it.

#### 2.8 Template Method

Define the skeleton of an algorithm in an operation, deferring some steps to subclasses.

Template Method lets subclasses redefine certain steps of an algorithm without letting them to change the algorithm's structure.

#### 2.9 Visitor

Represents an operation to be performed on the elements of an object structure.

Visitor lets you define a new operation without changes the classes of elements on which it operates.

#### 2.10 Null Object

Provide an object as a surrogate for the lack of an object of a given type. 

### 3 Structural Design Patterns

#### 3.1 Adapter

Convert an interface of a class into another interface clients expect.

Adapter lets classes work together, that could not otherwise because of incompatible interfaces.

#### 3.2 Bridge

Compose objects into tree structures to represent part-whole hierarchies.

Composite lets clients treat individual objects and compositions of objects uniformly.

#### 3.3 Composite

Compose objects into tree structures to represent part-whole hierarchies.

Composite lets clients treat individual objects and compositions of objects uniformly.

#### 3.4 Decorator

Add additional responsibilities dynamically to an object.

#### 3.5 Flyweight

use sharing to support a large number of objects that have part of their internal state in common where the other part of state can vary.

#### 3.6 Memento

Capture the internal state of an object without violating encapsulation and thus providing a mean for restoring the object into initial state when needed.

#### 3.7 Proxy

Provide a “Placeholder” for an object to control references to it.



