package org.enumus.initializer;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class RegexOptionsParser {
    private static final Map<Character, Integer> optionValues = new HashMap<>();
    static {
        optionValues.put('i', Pattern.CASE_INSENSITIVE);
        optionValues.put('m', Pattern.MULTILINE);
        optionValues.put('d', Pattern.DOTALL);
    }

    public static int parse(String options) {
        if (PatternValue.NO_OPTIONS.equals(options)) {
            return 0;
        }
        int opt = 0;
        for (char c : options.toCharArray()) {
            opt |= optionValues.get(c);
        }
        return opt;
    }
}
