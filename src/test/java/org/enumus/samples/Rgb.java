package org.enumus.samples;

import org.enumus.Mirror;
import org.enumus.ValueOf;
import org.enumus.ValueOfRange;

public enum Rgb {
    RED(635, 700),
    GREEN(520, 560),
    BLUE(450, 490),
    ;

    private static final ValueOf<Rgb, Integer> minimum = new ValueOf<>(Rgb.class, e -> e.min);
    private static final ValueOf<Rgb, Integer> maximum = new ValueOf<>(Rgb.class, e -> e.max);
    private static final ValueOfRange<Rgb, Integer> waveLength = new ValueOfRange<>(Rgb.class, e -> e.min, e -> e.max);
    private static final ValueOf<Rgb, String> caseInsensitive = new ValueOf<>(Rgb.class, Enum::name, String.CASE_INSENSITIVE_ORDER);
    static {
        Mirror.of(Color.class);
    }

    /**
     * min wavelength, nm
     */
    private final int min;

    /**
     * max wavelength, nm
     */
    private final int max;

    Rgb(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public int min() {
        return min;
    }

    public int max() {
        return max;
    }


    public static Rgb valueOfMin(int value) {
        return minimum.valueOf(value);
    }

    public static Rgb valueOfMax(int value) {
        return maximum.valueOf(value);
    }

    public static Rgb valueByWaveLength(int wave) {
        return waveLength.valueOf(wave);
    }

    public static Rgb caseInsensitiveValueOf(String name) {
        return caseInsensitive.valueOf(name);
    }
}
