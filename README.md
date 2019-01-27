# enumus

As set of utilities that make java enums more powerful.

# Features
* [Customized `valueOf()` (implementation of `valueOf()` based on any `enum` field and not on name as standard implementation.
* `valueOf()` based on value range](README.md#Customized-`valueOf()`)
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

# Mirror `enum`

# Hierarchical `enum`

# Initialization using annotations



