## 14 Blocks and Statements

The sequence of execution of a program is controlled by statements, which are executed for their effect and do not have values.

Some statements contain other statements as part of their structure; such other statements are substatements of the statement. We say that statement `S` immediately contains statement `U` if there is no statement `T` different from `S` and `U` such that `S` contains `T` and `T` contains `U`. In the same manner, some statements contain expressions (§15 (Expressions)) as part of their structure.

The first section of this chapter discusses the distinction between normal and abrupt completion of statements (§14.1).  Most of the remaining sections explain the various kinds of statements, describing in detail both their normal behavior and any special treatment of abrupt completion

Blocks are explained first (§14.2), followed by local class declarations (§14.3) and local variable declaration statements (§14.4).

Next a grammatical maneuver that sidesteps the familiar "dangling else" problem (§14.5) is explained.

The last section (§14.21) of this chapter addresses the requirement that every statement be *reachable* in a certain technical sense.

### 14.1 Normal and Abrupt Completion of Statements

### 14.2 Blocks

A *block* is a sequence of statements, local class declarations, and local variable declaration statements within braces.

```java
Block:
	{[BlockStatements]}

BlockStatements:
	BlockStatement {BlockStatement}

BlockStatement:
	LocalVariableDeclarationStatement
  ClassDeclaration
  Statement
```

A block is executed by executing each of the local variable declaration statements and other statements in order from first to last (left to right). If all of these block statements complete normally, then the block completes normally. If any of these block statements complete abruptly for any reason, then the block completes abruptly for the same reason.

### 14.3 Local Class Declarations

### 14.4 Local Variable Declaration Statements

#### 14.4.1 Local Variable Declarators and Types

#### 14.4.2 Execution of Local Variable Declarations

### 14.5 Statements

There are many kinds of statements in the Java programming language. Most correspond to statements in the C and C++ languages, but some are unique.

### 14.6 Empty Statement

An empty statement does nothing.

```java
Empty Statement:
  ;
```

Execution of an empty statement always completes normally.

### 14.7 Labeled Statements

### 14.8 Expression Statements

### 14.9 The `if` Statement

#### 14.9.1 The ``if-then` Statement

#### 14.9.2 The ``if-then-else` Statement

### 14.10 The `assert` Statement

### 14.11 The `switch` Statement

### 14.12 The `while`  Statement

#### 14.12.1 Abrupt Completion of `while` Statement

### 14.13 The `do` Statement

#### 14.13.1 Abrupt Completion of `do` Statement

### 14.14 The `for` Statement

#### 14.14.1 The basic `for` Statement

#### 14.14.2 The enhanced `for` Statement

### 14.15 The `break` Statement

### 14.16 The `continue` Statement

### 14.17 The `return` Statement

### 14.18 The `throw` Statement

### 14.19 The `synchronized` Statement

A `synchronized` statement acquires a mutual-exclusion lock (§17.1) on behalf of the executing thread, executes a block, then releases the lock. While the executing thread owns the lock, no other thread may acquire the lock.

```java
SynchronizedStatement:
	synchronized (Expression) Block
```

The type of *Expression* must be a reference type, or a compile-time error occurs.

A `synchronized` statement is executed by first evaluating the *Expression*. Then:

+ If evaluation of the *Expression* completes abruptly for some reason, then the `synchronized` statement completes abruptly for the same reason.
+ Otherwise, if the value of the *Expression* is `null`, a `NullPointerException` is thrown.
+ Otherwise, let the non-`null` value of the Expression be `V`. The executing thread locks the monitor associated with `V`. Then the Block is executed, and then there is a choice:
    + If execution of the *Block* completes normally, then the monitor is unlocked and the `synchronized` statement completes normally.
    + If execution of the *Block* completes abruptly for any reason, then the monitor is unlocked and the `synchronized` statement completes abruptly for the same reason.

The locks acquired by `synchronized` statements are the same as the locks that are acquired implicitly by `synchronized` methods (§8.4.3.6). A single thread may acquire a lock more than once.

Acquiring the lock associated with an object does not in itself prevent other threads from accessing fields of the object or invoking un-`synchronized` methods on the object. Other threads can also use `synchronized` methods or the `synchronized` statement in a conventional manner to achieve mutual exclusion.

**Example 14.9-1. The `synchronized` Statement**

```java
class Test {
  public static void main(String[] args) {
    Test t = new Test();
    synchronized(t) {
      synchronized(t) {
        System.out.println("made it!");
      }
    }
  }
}
```

This program produces the output: `made it!`.

Note that this program would deadlock if a single thread were not permitted to lock a monitor more than once.

### 14.20 The `try` Statement

#### 14.20.1 Execution of `try-catch`

#### 14.20.2 Execution of `try-finally` and `try-catch-finally`

#### 14.20.3 `try`-with-resources

### 14.21 Unreachable Statements



 