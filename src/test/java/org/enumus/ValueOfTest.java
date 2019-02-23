package org.enumus;

import org.enumus.samples.MathConstant;
import org.enumus.samples.Rgb;
import org.junit.jupiter.api.Test;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValueOfTest {
    @Test
    void valueOfInt() {
        assertEquals(Rgb.RED, Rgb.valueOfMin(635));
        assertEquals(Rgb.RED, Rgb.valueOfMax(700));
    }


    @Test
    void valueOfWrongValue() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> Rgb.valueOfMax(123));
        assertEquals(format("No enum constant %s.%d", Rgb.class.getName(), 123), e.getMessage());
    }


    @Test
    void approximateExact() {
        assertEquals(MathConstant.PI, MathConstant.approximateValueOf(MathConstant.PI.value()));
    }

    @Test
    void approximate() {
        assertEquals(MathConstant.PI, MathConstant.approximateValueOf(3.14));
    }

    @Test
    void approximateWrong() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> MathConstant.approximateValueOf(3));
        assertEquals(format("No enum constant %s.%.1f", MathConstant.class.getName(), 3.0), e.getMessage());
    }

    @Test
    void inRange() {
        assertEquals(Rgb.RED, Rgb.valueByWaveLength(650));
    }

    @Test
    void rangeFloor() {
        assertEquals(Rgb.GREEN, Rgb.valueByWaveLength(520));
    }

    @Test
    void rangeCeiling() {
        assertEquals(Rgb.BLUE, Rgb.valueByWaveLength(490));
    }

    @Test
    void betweenRanges() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> Rgb.valueByWaveLength(500));
        assertEquals(format("No enum constant %s.%d", Rgb.class.getName(), 500), e.getMessage());
    }

    @Test
    void caseInsensitiveExactCase() {
        assertEquals(Rgb.GREEN, Rgb.caseInsensitiveValueOf("GREEN"));
    }

    @Test
    void caseInsensitiveOppositeCase() {
        assertEquals(Rgb.RED, Rgb.caseInsensitiveValueOf("red"));
    }

    @Test
    void caseInsensitiveMixedCase() {
        assertEquals(Rgb.BLUE, Rgb.caseInsensitiveValueOf("BluE"));
    }

    @Test
    void caseInsensitiveNoMatch() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> Rgb.caseInsensitiveValueOf("Black"));
        assertEquals(format("No enum constant %s.%s", Rgb.class.getName(), "Black"), e.getMessage());
    }

    @Test
    void outOfRangeTooLow() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> Rgb.valueByWaveLength(1));
        assertEquals("No enum constant org.enumus.samples.Rgb.1", e.getMessage());
    }

    @Test
    void outOfRangeTooHigh() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> Rgb.valueByWaveLength(999));
        assertEquals("No enum constant org.enumus.samples.Rgb.999", e.getMessage());
    }
}