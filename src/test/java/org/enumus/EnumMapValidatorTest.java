package org.enumus;

import org.enumus.samples.Color;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EnumMapValidatorTest {
    @Test
    void validateEmpty() {
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> EnumMapValidator.validate(Color.class, "Set", "colors", () -> 0, color -> false, Color::name));
        assertEquals("Set is not complete: [[RED], [GREEN], [BLUE]] (colors in squire brackets are absent)", e.getMessage());
    }

    @Test
    void validateFull() {
        EnumMapValidator.validate(Color.class, "Set", "colors", () -> Color.values().length, color -> true, Color::name);
        assertTrue(Color.values().length > 0);
    }

    @Test
    void validateFullSet() {
        EnumMapValidator.validateElements(Color.class, new HashSet<>(Arrays.asList(Color.values())), "Colors list");
        assertTrue(Color.values().length > 0);
    }

    @Test
    void validateMissingSet() {
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> EnumMapValidator.validateElements(Color.class, new HashSet<>(Arrays.asList(Color.RED, Color.BLUE)), "Colors list"));
        assertEquals("Colors list is not complete: [RED, [GREEN], BLUE] (elements in squire brackets are absent)", e.getMessage());
    }

    @Test
    void validateKeysInFullMap() {
        Map<Color, Integer> map = Arrays.stream(Color.values()).collect(Collectors.toMap(c -> c, Enum::ordinal));
        EnumMapValidator.validateKeys(Color.class, map, "Colors map");
        assertEquals(Color.values().length, map.size());
    }

    @Test
    void validateKeysInMissingMap() {
        Map<Color, Integer> map = Arrays.stream(Color.values())
                .filter(c -> !Color.BLUE.equals(c))
                .collect(Collectors.toMap(c -> c, Enum::ordinal));

        IllegalStateException e = assertThrows(IllegalStateException.class, () -> EnumMapValidator.validateKeys(Color.class, map, "Colors map"));
        assertEquals("Colors map is not complete: [RED, GREEN, [BLUE]] (keys in squire brackets are absent)", e.getMessage());
    }

    @Test
    void validateValuesInFullMap() {
        Map<String, Color> map = Arrays.stream(Color.values()).collect(Collectors.toMap(Enum::name, c -> c));
        EnumMapValidator.validateValues(Color.class, map, "Colors map");
    }

    @Test
    void validateValuesInMissingMap() {
        Map<Integer, Color> map = Arrays.stream(Color.values())
                .filter(c -> !Color.RED.equals(c))
                .collect(Collectors.toMap(Enum::ordinal, c -> c));

        IllegalStateException e = assertThrows(IllegalStateException.class, () -> EnumMapValidator.validateValues(Color.class, map, "Colors map"));
        assertEquals("Colors map is not complete: [[RED], GREEN, BLUE] (values in squire brackets are absent)", e.getMessage());
    }

}