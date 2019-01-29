package org.enumus.samples;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum  ManualColor {
    RED("red"), GREEN("green"), BLUE("blue"),;

    private final String title;
    private static final Map<String, ManualColor> titles = Arrays.stream(ManualColor.values()).collect(Collectors.toMap(ManualColor::getTitle, e -> e));

    ManualColor(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public static ManualColor valueOfTitle(String title) {
        return titles.get(title);
    }
}
