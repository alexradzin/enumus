package org.enumus;

import org.enumus.samples.Color;
import org.enumus.samples.Color2;
import org.enumus.samples.RainbowColors1;
import org.enumus.samples.Rgb;
import org.enumus.samples.Shade;
import org.junit.jupiter.api.Test;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MirrorTest {
    @Test
    void goodMirror() {
        // trivial assertions just to touch the enum Color
        assertNotNull(Rgb.RED);
        assertNotNull(Rgb.GREEN);
        assertNotNull(Rgb.BLUE);
    }

    @Test
    void fieldWithWrongCase() {
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> Mirror.mirrors(Color.class, Color2.class));
        assertEquals(format("Element #2 of mirror %s.%s does not reflect the source %s.%s", Color2.class.getName(), Color2.Blue.name(), Color.class.getName(), Color.BLUE.name()), e.getMessage());
    }

    @Test
    void goodMultipleMirrors() {
        Mirror.mirrors(RainbowColors1.class, Color.class, Shade.class);
        assertEquals(RainbowColors1.values().length, Color.values().length + Shade.values().length);
    }


    @Test
    void missingMirrors() {
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> Mirror.mirrors(RainbowColors1.class, Color.class));
        assertTrue(e.getMessage().contains("Source and mirror enums have different number of elements"));
    }

    @Test
    void discoverCaller() {
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> Mirror.of(Color.class));
        assertEquals(format("Caller of Mirror.of() %s is not enum", getClass().getName()), e.getMessage());
    }

    @Test
    void mirrorOfItself() {
        ExceptionInInitializerError e = assertThrows(ExceptionInInitializerError.class, TestEnum::values);
        assertEquals(format("Class %s cannot be mirror of itself", TestEnum.class.getName()), e.getCause().getMessage());
    }

    enum TestEnum {
        ONE;
        static {
            Mirror.of(TestEnum.class);
        }
    }
}