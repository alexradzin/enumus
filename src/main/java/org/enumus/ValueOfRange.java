package org.enumus;

import java.util.Arrays;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Objects;
import java.util.TreeMap;
import java.util.function.Function;

import static java.lang.String.format;

public class ValueOfRange<T extends Enum<T>, F extends Comparable>  {
    private static final String ERROR_MESSAGE = "No enum constant %s.%s";
    private final Class<T> type;
    private final NavigableMap<F, T> values;
    public ValueOfRange(Class<T> type, Function<T, F> floorAccessor, Function<T, F> ceilingAccessor) {
        this.type = type;
        values = new TreeMap<>();
        fill(values, type.getEnumConstants(), floorAccessor);
        fill(values, type.getEnumConstants(), ceilingAccessor);
    }

    private static <F, T> void fill(Map<F, T> map, T[] values, Function<T, F> keyCreator) {
        Arrays.stream(values).forEach(t -> map.put(keyCreator.apply(t), t));
    }

    public T valueOf(F key) {
        Map.Entry<F, T> f = values.floorEntry(key);
        Map.Entry<F, T> c = values.ceilingEntry(key);
        if (f != null && c != null && Objects.equals(f.getValue(), c.getValue())) {
            return f.getValue();
        }
        throw new IllegalArgumentException(format(ERROR_MESSAGE, type.getName(), key));
    }
}
