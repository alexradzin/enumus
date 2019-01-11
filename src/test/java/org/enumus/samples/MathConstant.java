package org.enumus.samples;

import org.enumus.ValueOf;

public enum MathConstant {
    PI(3.1415926),
    E(2.718281828),
    ;
    private final double value;
    private static ValueOf<MathConstant, Double> approximately = new ValueOf<>(MathConstant.class, e -> e.value, (v1, v2) -> {
        double diff = Math.abs(v1 - v2);
        return diff < 0.01 ? 0 : diff < 0 ? -1 : 1;
    });

    MathConstant(double value) {
        this.value = value;
    }

    public static MathConstant approximateValueOf(double value) {
        return approximately.valueOf(value);
    }

    public double value() {
        return value;
    }
}
