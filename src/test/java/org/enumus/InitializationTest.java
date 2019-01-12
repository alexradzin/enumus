package org.enumus;

import org.enumus.initializer.DateValue;
import org.enumus.initializer.DoubleValue;
import org.enumus.initializer.Initializable;
import org.enumus.initializer.IntValue;
import org.enumus.initializer.PatternValue;
import org.enumus.initializer.Value;
import org.enumus.samples.Software;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Function;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InitializationTest {
    @Test
    void testSoftware() {
        assertTrue(Software.class.getEnumConstants().length > 0);
    }

    @Test
    void oneNullString() {
        assertNull(OneStringParamTestEnum.ZERO.str);
    }

    @Test
    void oneStringParameterSentDirectly() {
        assertEquals("one", OneStringParamTestEnum.ONE.str);
    }

    @Test
    void oneStringParameterSentViaValueAnnotation() {
        assertEquals("two", OneStringParamTestEnum.TWO.str);
    }


    @Test
    void oneDefaultInt() {
        assertEquals(0, OneIntParamTestEnum.ZERO.number);
    }

    @Test
    void oneIntParameterSentDirectly() {
        assertEquals(1, OneIntParamTestEnum.ONE.number);
    }

    @Test
    void oneIntParameterSentViaValueAnnotation() {
        assertEquals(2, OneIntParamTestEnum.TWO.number);
    }

    @Test
    void oneDefaultDouble() {
        assertEquals(0, OneDoubleParamTestEnum.ZERO.number);
    }
    @Test
    void oneDoubleParameterSentDirectly() {
        assertEquals(3.1415926, OneDoubleParamTestEnum.PI.number, 0.01);
    }

    @Test
    void oneDoubleParameterSentViaValueAnnotation() {
        assertEquals(2.718281828, OneDoubleParamTestEnum.E.number, 0.01);
    }


    @Test
    void oneDateParameterValues() {
        OneDateParamTestEnum.values();
    }

    @Test
    void oneDateParameterArgument() {
        assertEquals(date("1917-11-07"), OneDateParamTestEnum.AURORA.date);
    }

    @Test
    void oneDateParameterAnnotation() {
        assertEquals(date("1789-07-14"), OneDateParamTestEnum.BASTILLE.date);
    }

    @Test
    void onePatternParameterValues() {
        OnePatternTestEnum.values();
    }

    @Test
    void onePatternParameterArgument() {
        assertTrue(OnePatternTestEnum.DIGITS.pattern.matcher("12345").find());
    }

    @Test
    void onePatternParameterAnnotation() {
        assertTrue(OnePatternTestEnum.IP.pattern.matcher("127.0.0.1").find());
    }

    @Test
    void notEnum() {
        assertThrows(IllegalStateException.class, MyTestClass::new, String.format("Class %s is not an enum", MyTestClass.class.getName()));
    }

    public enum OneStringParamTestEnum implements Initializable {
        ZERO(),
        ONE("one"),

        @Value(name = "str", value = "two")
        TWO(),;

        private final String str;

        OneStringParamTestEnum() {
            str = $();
        }
        OneStringParamTestEnum(String str) {
            this.str = str;
        }
    }

    public enum OneIntParamTestEnum implements Initializable {
        ZERO(),
        ONE(1),

        @IntValue(name = "number", value = 2)
        TWO(),
        ;

        private final int number;

        OneIntParamTestEnum() {
            number = $();
        }
        OneIntParamTestEnum(int number) {
            this.number = number;
        }
    }

    public enum OneDoubleParamTestEnum implements Initializable {
        ZERO(),
        PI(3.1415926),

        @DoubleValue(name = "number", value = 2.718281828)
        E(),
        ;

        private final double number;

        OneDoubleParamTestEnum() {
            number = $();
        }
        OneDoubleParamTestEnum(double number) {
            this.number = number;
        }
    }

    public enum OneDateParamTestEnum implements Initializable {
        @DateValue(name = "date", value = "1789-07-14", format = "yyyy-MM-dd")
        BASTILLE(),
        AURORA(date("1917-11-07")),
        ;

        private final Date date;

        OneDateParamTestEnum(Date date) {
            this.date = date;
        }

        OneDateParamTestEnum() {
            this.date = $();
        }
    }


    public enum OnePatternTestEnum implements Initializable {
        DIGITS(Pattern.compile("^\\d+$")),

        @PatternValue(name = "pattern", regex = "^\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}$")
        IP(),

        @PatternValue(name = "pattern", regex = "hello", options = Pattern.CASE_INSENSITIVE)
        CASE_INSENSITIVE(),
        ;

        private final Pattern pattern;

        OnePatternTestEnum(Pattern pattern) {
            this.pattern = pattern;
        }

        OnePatternTestEnum() {
            pattern = $();
        }
    }

    class MyTestClass implements Initializable {
        MyTestClass() {
            $();
        }
    }

    private static Date date(String str) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(str);
        } catch (ParseException e) {
            throw new IllegalArgumentException(str, e);
        }
    }
}
