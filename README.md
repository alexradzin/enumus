# enumus

As set of utilities that make java enums more powerful.

# Features
* [Customized `valueOf()` (implementation of `valueOf()` based on any `enum` field and not on name as standard implementation)](README.md#Customized-`valueOf()`)
* [`valueOf()` based on value range](README.md#Range-based-`valueOf()`)
* [Enum map validator](README.md#Enum-map-validator)
* [Mirror `enum`](README.md#Mirror-`enum`)
* [Hierarchical `enum`](README.md#Hierarchical-`enum`)
* [Initialization using annotations](README.md#Initialization-using-annotations)

# Customized `valueOf()`
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


## Range based `valueOf()`
The utility supports ranges as well. Visible colors are just electro magnetic waves of certain length. 
* Red	~ 700–635 nm
* Orange	~ 635–590 nm	
* Yellow	~ 590–560 nm	
* Green	~ 560–520 nm
 
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



# Enum map validator
It is very useful practice to use `enum` constants as the map keys. Values may contain either simple data, complex objects or often functions. Very often we want to have entry per each `enum` element. Unfortunately compiler will not help us to detect if new element was added to enum. In this case code can fail at rutime. Enumus helps to solve this problem:

```java
EnumMapValidator.validateKeys(Color.class, map, "Colors map");
```

If for instance `map` does not contain entry `BLUE` the line above will throw `IllegalStateException` with message `"Colors map is not complete: [RED, GREEN, [BLUE]] (keys in squire brackets are absent)"`


# Mirror `enum`
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


# Hierarchical `enum`

Neither `enum`s nor their elements cannot be inherited from other class. It is becase each `enum` is inherited implicitly from `java.lang.Enum`. Some tasks however can be easier modelled using hierarchy. Enumus helps to implement hieratchical structure with `enum`s. 





# Initialization using annotations



