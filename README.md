# enumus 

[![CircleCI](https://circleci.com/gh/alexradzin/enumus/tree/master.svg?style=svg)](https://circleci.com/gh/alexradzin/enumus/tree/master) 
[![Build Status](https://travis-ci.com/alexradzin/enumus.svg?branch=master)](https://travis-ci.com/alexradzin/enumus)
[![codecov](https://codecov.io/gh/alexradzin/enumus/branch/master/graph/badge.svg)](https://codecov.io/gh/alexradzin/enumus)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/ea86efcce7224cc294cd8d550fe63a82)](https://www.codacy.com/app/alexradzin/enumus?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=alexradzin/enumus&amp;utm_campaign=Badge_Grade)

A set of utilities that make java enums more powerful.

## Features
  * [Customized `valueOf()` (implementation of `valueOf()` based on any `enum` field and not on name as standard implementation)](README.md#Customized-`valueOf()`)
  * [`valueOf()` based on value range](README.md#Range-based-`valueOf()`)
  * [Enum map validator](README.md#Enum-map-validator)
  * [Mirror `enum`](README.md#Mirror-`enum`)
  * [Hierarchical `enum`](README.md#Hierarchical-`enum`)
  * [Initialization using annotations](README.md#Initialization-using-annotations)

## Customized `valueOf()`
Enum have a very convinient built-in feature - static function `valueOf()` that retrieves enum constant by its name. 
In some cases however we need something similar but based on other field. The implementation is simple: create static map, 
iterate over values of enum and populate the map. 

```java
public enum Color {
    RED("red"), GREEN("green"), BLUE("blue"),;
    private final String title;
    ManualColor(String title) {
        this.title = title;
    }
}
```

Thanks to Java 8 we can now right this using one line only:

```java
private static final Map<String, Color> titles = Arrays.stream(Color.values()).collect(Collectors.toMap(Color::getTitle, e -> e));
```
I found myself writing similar code many times and decidded to implemnent utility that simplifies this. Now the code can look like the following:

```java
private static final ValueOf<Color, String> titles = new ValueOf<>(Color.class, e -> e.title);
```

The static accessor is simple too. It should not care about throwing exception if value is not found.

```java
    public static Color valueOfTitle(String title) {
        return titles.valueOf(title);
    }
```

The utity accepts reference to function that can implement more complicated logic than just accessing a simple field.

### Range based `valueOf()`
The utility supports ranges as well. Visible colors are just electro magnetic waves of certain length.

| Color  | Wave length, nm |
|--------|-----------------|
| Red    | 700–635         |
| Orange | 635–590         |
| Yellow | 590–560         |
| Green  | 560–520         |

 We want to define enum `Rgb` and get enum constant by wave length from the range. It is very easy with enumus:
 
 ```java
 public enum Rgb {
    RED(635, 700), GREEN(520, 560), BLUE(450, 490),;
    private static final ValueOfRange<Rgb, Integer> waveLength = new ValueOfRange<>(Rgb.class, e -> e.min, e -> e.max);
    private final int min;
    private final int max;
    Rgb(int min, int max) {
        this.min = min;
        this.max = max;
    }
    public static Rgb valueByWaveLength(int wave) {
        return waveLength.valueOf(wave);
    }    
```

## Enum map validator
It is very useful practice to use `enum` constants as the map keys. Values may contain either simple data, complex objects or often functions. Very often we want to have entry per each `enum` element. Unfortunately compiler will not help us to detect if new element was added to enum. In this case code can fail at rutime. Enumus helps to solve this problem:

```java
EnumMapValidator.validateKeys(Color.class, map, "Colors map");
```

If for instance `map` does not contain entry `BLUE` the line above will throw `IllegalStateException` with message `"Colors map is not complete: [RED, GREEN, [BLUE]] (keys in squire brackets are absent)"`

## Mirror `enum`
Enums can hold data and implement methods. However not all methods can be implemented in one class. Methods may delegate functionality to other classes that require external dependencies. Often we do not want to put all dependencies into one module but rather separate them among several modules. Sometimes this cause us to hold 2 or more `enum`s with the same constants and different implementation. This can be used instead of classic [Visitor pattern](https://en.wikipedia.org/wiki/Visitor_pattern) but even has advantage: some implementations may be optional and we can check whether the implementation is done or not without calling the actual function. 

Enumus introduces term "morror" `enum`, that is `enum` that has the same entries as its mirror. The feature may be comapred with 2 classes that implement the same interface. In this case compiler checks that all methods are implemented. Mirror enum cannot enjoy the compiler's service but it is enough add one line of code in static initialiazer and exception will be thrown if enum does not reflect its mirror. 

```java
enum MissingColor {
    RED, GREEN,;
    static {
        Mirror.mirrors(MissingColor.class, Color.class); // throws exception because BLUE is absent
    }
}
```

## Hierarchical `enum`

Neither `enum`s nor their elements cannot be inherited from other class. It is becase each `enum` is inherited implicitly from `java.lang.Enum`. Some tasks however can be easier modelled using hierarchy. Enumus helps to implement hieratchical structure with `enum`s. 

```java
public enum OsType {
    OS(null),
        Windows(OS),
            WindowsNT(Windows),
                WindowsNTWorkstation(WindowsNT),
                WindowsNTServer(WindowsNT),
            Windows2000(Windows),
                Windows2000Server(Windows2000),
                Windows2000Workstation(Windows2000),
            WindowsXp(Windows),
            WindowsVista(Windows),
            Windows7(Windows),
            Windows95(Windows),
            Windows98(Windows),
    Unix(OS) {
        @Override
        public boolean supportsXWindowSystem() {
            return true;
        }
    },
    Linux(Unix),
        IOS(Linux),
        Android(Linux),
    AIX(Unix),
    HpUx(Unix),
    SunOs(Unix),;
    private OsType parent;
    OsType(OsType parent) {
        this.parent = parent;
    }
}
```

The structure above is defined using standard Java syntax; there is no need to use external tools. However the following features may be needed here:
  * we cannot easily get list of all children of `Linux`
  * How to check that `WindowsVista` is `Windows`?
  * How to "inherit" method `supportsXWindowSystem()` in all Unix like systems?

All these can be easily done with class `Hierarchy` implemented by enumus. Just add the following line:

```java
    private static Hierarchy<OsType> hierarchy = new Hierarchy<>(OsType.class, e -> e.parent);
```
and implementation of described above methods becomes trivial:

```java
    public OsType[] children() {
        return hierarchy.getChildren(this);
    }
    public boolean supportsXWindowSystem() {
        return hierarchy.invoke(this, args -> false);
    }
    public boolean isA(OsType other) {
        return hierarchy.relate(other, this);
    }
```

## Initialization using annotations
Almost each appllication contains series of constant values like these:

```java
public static final String THREAD_COUNT_PROPERTY = "THREAD_COUNT_PROPERTY";
public static final int THREAD_COUNT_DEFAULT = 10;
public static final String CONNECTION_TIMEOUT_PROPERTY = "CONNECTION_TIMEOUT_PROPERTY";
public static final int CONNECTION_TIMEOUT_DEFAULT = 10_000;
```

It is convenient to organize such constants using enums. For example instead of having a lot of groups of `static final` members we can put them into enum like following:

```java
enum Configuration {
    THREAD_COUNT(10), NETWORK_TIMEOUT(10_000),;
    private final int value;
    Configuration(int value) {this.value = value}
    public int value() {return value;}
    public String property() {return name() + "_PROPERTY";}
}
```

Such enums tend to grow quickly. Sometimes they contain values that match wourse than those in example above. For exampele connection may hold read/write timeouts while threading model may become more flexible and contain minimum, maximum and default thread count. In this case we have either break the enum into separate parts or add all fields needed for all different elements together:

```java
enum Configuration {
    THREAD_COUNT(5, 20, 10, 0, 0), // read/write timeout is irrelevant here, so we pass 0 instead 
    NETWORK_TIMEOUT(0, 0, 10_000, 5_000, 8_000),; // min/max values are irrelevant for timeout
    private final int min;
    private final int max;
    private final int value;

    private final int read;
    private final int write;

    Configuration(int min, int max, int value, int read, int write) {/* initialization code*/}
}
```

The definitions of the enum elements became ugly. We have to pass irrelevant values just to satisfy compiler. 
We can define several overloaded constructors that better suite our cases. But it is not always possible. For example in this example we would liek to have 2 additional constructors:

```java
    Configuration(int min, int max, int value) {/* initialization code*/}
    Configuration(int value, int read, int write) {/* initialization code*/}
```

But we cannot do this because both constructors have the same signature: 3 `int` parameters.
In real life number of parameter variations may be much higher. 
Enumus suggests solution for this. 

Enums are inititated when they are accessed first time, typically on JVM startup. So, performance is not an issue here. This means that the configuration could be done using annotations without impact on application performance. Enumus provides an extendable type safe annotation based framework for configuration of `enum` elements:

Integer value configured using annotation
```java
        @IntValue(name = "number", value = 2)
        TWO(),
```

String value configured using annotation
```java
        @Value(name = "str", value = "two")
        TWO(),;
```

Here is full example that can illustrate the usage:

```java
    public enum OneDoubleParamTestEnum implements Initializable {
        ZERO(),        // using default constructor; the double value will be initialized to 0.0
        PI(3.1415926), // using regular constructor
        @DoubleValue(name = "number", value = 2.718281828)
        E();           // the value is initialiezed using annotation
        
        private final double number;
        
        OneDoubleParamTestEnum() {
            number = $(); // default constructor that calls $() function implemented in interface Initializable
        }
        OneDoubleParamTestEnum(double number) {
            this.number = number; // regular constructor
        }
    }
```

Almost any type of argument can be supported. The following types are supported out of the box:
  * `String`
  * `int`
  * `double`
  * `java.util.Date`
  * `java.util.regex.Pattern`
  * any `enum` type




