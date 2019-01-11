package org.enumus;

import org.enumus.samples.Color;
import org.enumus.samples.Color2;
import org.enumus.samples.RainbowColors1;
import org.enumus.samples.Rgb;
import org.enumus.samples.Shade;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
        assertEquals(String.format("Element #2 of mirror %s.%s does not reflect the source %s.%s", Color2.class.getName(), Color2.Blue.name(), Color.class.getName(), Color.BLUE.name()), e.getMessage());
    }

    @Test
    void goodMultipleMirrors() {
        Mirror.mirrors(RainbowColors1.class, Color.class, Shade.class);
    }

}