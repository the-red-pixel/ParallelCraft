# SRFG-v1-X
> Stack Related Flow Graph (Revision 1) eXtension  
> 栈相关流图（第一修订版）扩展  

**（阅读此文档前，请确定您已知晓 Java 字节码相关的基础知识，并且已阅读 SRFG-v1 的相关文档）**


## 目录
- [1 概述](#1)
- [2 类型系统](#2)
    - [2.1 基本类型](#2-1)
    - [2.2 松散模型](#2-2)
    - [2.3 严格模型](#2-3)
        - [2.3.1 多重继承](#2-3-1)
        - [2.3.2 类型匹配](#2-3-2)
- [3 图结构](#3)
    - [3.1 蕴含性](#3-1)
    - [3.2 基本表示](#3-2)
    - [3.3 基本单位](#3-3)
- [4 堆栈](#4)

## <a id = "1">1&nbsp;概述</a>
&emsp;&emsp;**SRFG-v1-X** 是对于 **SRFG-v1** （栈相关流图第一修订版）的扩展版本，使第一修订版的 **SRFG** 更加适合于对于 Java 字节码的动态修改操作，支持更多的面向切面编程相关特性。在此 **SRFG** 版本中，将基于 **SRFGv1** 的大部分基础特性进行修改及扩展，所有相关的被修改的及扩展的特性都将在本文中进行详细的列举与解释。  


## <a id = "2">2&nbsp;类型系统</a>
&emsp;&emsp;**SRFG-v1-X** 与 **SRFG-v1** 对于类型的处理策略不同，**SRFG-v1-X** 中引入了较为完整、严格的类型系统以实现一些多态化的功能。此类型系统的规则与 Java 语言规范大致一致，但不考虑泛型的相应情况。  

### <a id = "2-1">2.1&nbsp;基本类型</a>
&emsp;&emsp;在本类型系统中，有两种不同的类型模型，分别为**松散模型**与**严格模型**。这两种模型生效的条件不同，可以达成的多态化效果也不同，在不同的环境中应适当选择不同的模型，后续章节中将对他们详细描述。  
&emsp;&emsp;首先，可以将 Java 的所有类型归类为以下几种类型，并且有对应的描述符，与 JVM 规范相仿：  

类型名称   | 描述符 | 类别 | 描述       |
--------- | ------ | --- | ---------- |
Integer   | I      |  1  | 32位整型数  |
Float     | F      |  1  | 32位浮点数  |
Long      | J      |  2  | 64位整型数  |
Double    | D      |  2  | 64位整型数  |
Reference | L      |  1  | 对象引用    |
Array     | [      |  1  | 数组引用    |

&emsp;&emsp;以上这种对基本类型的分类方法对于最基本的堆栈推算与验证是必须的，这些类型又可以被分为两种类别，以表示其在堆栈的不同表示方式。区别在于第一类别的类型在堆栈内占用一个槽位，而第二类别的类型在堆栈内占用两个槽位。实际上 Java 内还有四种整型（```boolean```、```byte```、```char```、```short```），比已有的两种整型类型的精度都要低，它们之所以没有被列出来是因为它们在 JVM 的内存模型中表示方式都与32位整型数一致，只存在处理方式上的差别。  
&emsp;&emsp;以上这种基本类型分类方式在松散验证与对性能敏感的场景下是种不错的选择，但这明显不符合 **SRFG-v1-X** 的设计目标，这样松散、简单的分类方式是无法满足多态化的需求的。所以本类型系统将考虑所有的基本类型，并且对于对象引用、数组引用的类型计算会有更加复杂的实现。基本类型分类情况如下表:  

| 类型名称   | 描述符 | 类别 | 描述                 |
| --------- | ------ | --- | -------------------- |
| Boolean   | Z      |  1  | 布尔类型（1位整型数）  |
| Byte      | B      |  1  | 字节类型（8位整型数）  |
| Char      | C      |  1  | 字符类型（16位整型数） |
| Short     | S      |  1  | 16位整型数            |
| Integer   | I      |  1  | 32位整型数            |
| Float     | F      |  1  | 32位浮点数            |
| Long      | J      |  2  | 64位整型数            |
| Double    | D      |  2  | 64位整型数            |
| Reference | L      |  1  | 对象引用              |
| Array     | [      |  1  | 数组引用              |

### <a id = "2-2">2.2&nbsp;松散模型</a>
&emsp;&emsp;松散模型完全采用第 [2.1](#2-1) 节中所描述的分类方式，且松散模型仅计算以上十种基本类型，所有的对象都被视为 ```Reference``` 类型，而所有的数组对象都被视为 ```Array``` 类型。此种类型模型较为简单，开销较小，不需要预先提供并计算具体类型的继承关系，但多态化的程度也因此受到限制，仅能支持一些基本的多态化特性。  
&emsp;&emsp;不同类型在本模型内的表示如下例（解析描述符、解析类型分别表示该 Java 类型在本模型下对应的描述符与类型）：  

| Java类型           | 解析描述符 | 解析类型   |
| ----------------- | ---------- | --------- | 
| int               | I          | Integer   |
| double            | D          | Double    |
| short             | S          | Short     |
| java.lang.Object  | L          | Reference |
| java.lang.Integer | L          | Reference |
| java.lang.String  | L          | Reference |


### <a id = "2-3">2.3&nbsp;严格模型</a>
&emsp;&emsp;严格模型的类型系统基于第 [2.1](#2-1) 节中所描述的分类方式，并对对象与数组对象的转换、继承关系进行严格计算。此种类型模型较为复杂，运行时开销较大，并且需要预先提供具体类型的继承关系，但可以达到更好的多态化效果，支持更多的多态化特性。  
&emsp;&emsp;Java 语言规范允许类型继承，不允许多重类型继承，但允许实现、重载多个接口。由于继承与接口的存在，某种类型可能可以被认作多种其它类型，或与多种其它类型向兼容，被认作同一个身份的多个对象也可能根据其子身份以不同的方式运作，以上这些特性即是**多态**。在严格模型下，此种类型就不会简单地被看作只有一种单一的身份，不会被笼统地概括为“对象”。这就需要额外的计算，并且不允许出现未知的继承关系，但此时类型之间的关系就会十分明确，并且可以将**多态**的一些优点应用到面向切面编程（AOP）中去。  

&emsp;&emsp;需要注意的是，存在继承关系的类型就会有多个身份，且这些身份的存在不全是并列的。观察以下类型：
```Java
public final class Integer extends Number
        implements Comparable<Integer>, Constable, ConstantDesc {
        
        // ...
}
```
&emsp;&emsp;我们可以简单画出该类型的继承树：
```
 +--------+
 | Object |
 +--------+
      |      +--------------+
      |      | Serializable |
      |      +--------------+
      | extends    |
 +--------+        |                   
 | Number |<-------+
 +--------+ implements
      |      +------------+ +-----------+ +--------------+
      |      | Comparable | | Constable | | ConstantDesc |
      |      +------------+ +-----------+ +--------------+
      | extends    |              |              |
 +---------+       |              |              |
 | Integer |<------+--------------+--------------+
 +---------+ implements
```
&emsp;&emsp;观察此继承树可知，类型的不同身份实际上存在一定的层次区别。并且一般地，类型的继承树遵循自顶向下的原则，即越靠近顶端的类型越抽象，越靠近底端的类型越具象，这也是类型继承的核心理念。而 Java 中接口（Interface）的存在也使得类型的继承树中可能出现不同身份并列的情况，使得我们不得不考虑**多重继承**可能带来的副作用。  
&emsp;&emsp;本模型规定在此情况下，其不同身份的优先级如下（数字编号越小优先级越高，前缀为 ```#``` 表示实现接口，```@``` 表示继承，无前缀表示本类型自身）：
```
#0   java.lang.Integer
#1 # java.lang.Comparable
#1 # java.lang.constant.Constable
#1 # java.lang.constant.ConstantDesc
#2 @ java.lang.Number
#3 # java.io.Serializable
#4 @ java.lang.Object
```
&emsp;&emsp;同样地，不同类型在本模型内的表示如下例（解析描述符、解析类型分别表示该 Java 类型在本模型下对应的描述符与类型）：  

| Java 类型           | 解析描述符              | 解析类型列表（含优先级）         |
| ------------------- | ---------------------- | ------------------------------ |
| int                 | I                      | #0&ensp;&ensp;&ensp;int                       
| double              | D                      | #0&ensp;&ensp;&ensp;double                    
| short               | S                      | #0&ensp;&ensp;&ensp;short                     
| java.lang.Object    | Ljava/lang/Object;     | #0&ensp;&ensp;&ensp;java.lang.Object          
| java.lang.Override  | Ljava/lang/Override;   | #0&ensp;&ensp;&ensp;java.lang.Override<br>#1&ensp;@&ensp;java.lang.Object        
| java.lang.Number    | Ljava/lang/Number;     | #0&ensp;&ensp;&ensp;java.lang.Number<br>#1&ensp;#&ensp;java.io.Serializable<br>#2&ensp;@&ensp;java.lang.Object
| java.lang.Exception | Ljava/lang/Exception;  | #0&ensp;&ensp;&ensp;java.lang.Exception<br>#1&ensp;@&ensp;java.lang.Throwable<br>#2&ensp;#&ensp;java.io.Serializable<br>#3&ensp;@&ensp;java.lang.Object
| java.lang.Integer   | Ljava/lang/Integer;    | #0&ensp;&ensp;&ensp;java.lang.Integer<br>#1&ensp;#&ensp;java.lang.Comparable<br>#1&ensp;#&ensp;java.lang.constant.Constable<br>#1&ensp;#&ensp;java.lang.constant.ConstantDesc<br>#2&ensp;@&ensp;java.lang.Number<br>#3&ensp;#&ensp;java.io.Serializable<br>#4&ensp;@&ensp;java.lang.Object<br>

#### <a id = "2-3-1">2.3.1&nbsp;多重继承</a>

&emsp;&emsp;由上文可知，```Integer``` 类型实现的三个接口类型（```Comparable```、```Constable```、```ConstantDesc```）而拥有的三个身份是并列关系。这种现象会为实现多态化特性的过程带来副作用，比如存在二义性等，并且这些问题在 Java 语言本身中就已有体现，如下例：
```Java
public static interface B {
    public default void foo()
    {
        // ...
    }
}

public static interface C {
    public default void foo()
    {
        // ...
    }
}
```
&emsp;&emsp;首先，定义两个接口类型 ```B``` 与 ```C```，并且都含有名称为 ```foo``` 的无返回值缺省函数。
```Java
public class A implements B, C {
    // ...
}
```
&emsp;&emsp;其次，定义一个类型 ```A```，并实现接口类型 ```B``` 与 ```C```，且不重写 ```void foo()``` 函数。此时编译类型 ```A``` 则会得到类似以下的错误信息：
```
Error: java: 类型 B 和 C 不兼容;
        类 A 从类型 B 和 C 中继承了foo() 的不相关默认值
```
&emsp;&emsp;此种多重继承的情况明显带来了二义性，所以 Java 语言规范不会允许此种情况存在。解决方案即是在类 ```A``` 中重写 ```void foo()``` 函数以消除二义性，如下：
```Java
public class A implements B, C {
    @Override
    public void foo()
    {
        // B.super.foo();
        // C.super.foo();
        // ...
    }

    // ...
}
```
&emsp;&emsp;多重继承亦会带来类型冲突的问题，如下例：
```Java
public static interface B {
    public default void foo()
    {
        // ...
    }
}

public static interface C {
    public default int foo()
    {
        // ...
    }
}
```
&emsp;&emsp;同样地，首先定义两个接口类型 ```B``` 与 ```C``` 并包含同名函数 ```foo()```。此时，将类 ```C``` 内的 ```foo()``` 函数的返回值更改为 ```int```。
```Java
public class A implements B, C {
    // ...
}
```
&emsp;&emsp;其次，定义一个类型 ```A```，并实现接口类型 ```B``` 与 ```C```。此时编译类型 ```A``` 则会得到类似以下的错误信息：
```
Error: java: 类型 B 和 C 不兼容;
        两者都定义了 foo(), 但却带有不相关的返回类型
```
&emsp;&emsp;此种错误无法用重载 ```foo()``` 函数的方法解决，且解决方案只有不同时实现接口类型 ```B``` 与 ```C```。  

#### <a id = "2-3-2">2.3.2&nbsp;类型匹配</a>
&emsp;&emsp;**类型匹配**即对数据类型的模式匹配。对于一些为面向切面编程（AOP）设计的多态化特性与功能，会根据数据类型的不同进行不同的操作以及生成不同的代码，故**类型匹配**是必须的。而在本严格模型的环境之下，**类型匹配**的过程中必须要考虑多重继承所带来的副作用，故**类型匹配**的过程必须遵循一些特殊的规则。


// TODO

## <a id = "3">3&nbsp;图结构</a>

### <a id = "3-1">3.1&nbsp;蕴含性</a>

// TODO

### <a id = "3-2">3.2&nbsp;基本表示</a>
&emsp;&emsp;**SRFG-v1-X** 结构的基本表示相关的规则与 **SRFG-v1** （栈相关流第一修订版） 相同，详见 **SRFG-v1** 相关文档。

### <a id = "3-3">3.3&nbsp;基本单位</a>
&emsp;&emsp;**SRFG-v1-X** 中对于 **SRFG-v1** 的原有的基本节点单位进行了扩展，并且对于原有的基本节点单位没有删减，本节将对这些扩展的内容进行详细介绍。  

// TODO


## <a id = "4">4&nbsp;堆栈</a>

// TODO