package org.enumus;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.lang.String.format;

public class EnumMapValidator {
    public static <K extends Enum<K>, V> void validateKeys(Class<K> clazz, Map<K, V> map, String mapName) {
        validate(clazz, mapName, "keys", map::size, map::containsKey, Enum::name);
    }

    public static <K, V extends Enum<V>> void validateValues(Class<V> clazz, Map<K, V> map, String mapName) {
        validate(clazz, mapName, "values", map::size, map::containsValue, Enum::name);
    }

    public static <T extends Enum<T>> void validateElements(Class<T> clazz, Set<T> set, String mapName) {
        validate(clazz, mapName, "elements", set::size, set::contains, Enum::name);
    }



    public static <T extends Enum<T>> void validate(Class<T> clazz, String containerTitle, String elementTitle, Supplier<Integer> size, Predicate<T> contains, Function<T, String> name) {
        T[] elements = clazz.getEnumConstants();
        if (elements.length != size.get()) {
            throw new IllegalStateException(format("%s is not complete: %s (%s in squire brackets are absent)",
                    containerTitle,
                    Arrays.stream(elements).map(e -> contains.test(e) ? name.apply(e) : "[" + name.apply(e) + "]").collect(Collectors.toList()),
                    elementTitle));
        }
    }

}
