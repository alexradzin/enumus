package org.enumus;

import org.enumus.initializer.Argument;
import org.enumus.initializer.BooleanValue;
import org.enumus.initializer.ByteValue;
import org.enumus.initializer.DateValue;
import org.enumus.initializer.DoubleValue;
import org.enumus.initializer.Initializable;
import org.enumus.initializer.IntValue;
import org.enumus.initializer.LongValue;
import org.enumus.initializer.PatternValue;
import org.enumus.initializer.ShortValue;
import org.enumus.initializer.Value;
import org.enumus.samples.OsType;
import org.enumus.samples.Platform;
import org.enumus.samples.Software;
import org.enumus.samples.article.IsoAlpha2;
import org.enumus.samples.article.enumwitannotations.Country;
import org.junit.jupiter.api.Test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import static org.enumus.samples.OsType.Unix;
import static org.enumus.samples.OsType.Windows;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
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
        assertTrue(OneDateParamTestEnum.values().length > 0);
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
        assertTrue(OnePatternTestEnum.values().length > 0);
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

    @Test
    void byteArray() {
        assertArrayEquals(new byte[] {1, 2, 3}, ArrayArgumentEnum.BYTE.getBytearr());
    }

    @Test
    void shortArray() {
        assertArrayEquals(new short[] {1, 2, 3}, ArrayArgumentEnum.SHORT.getShortarr());
    }

    @Test
    void intArray() {
        assertArrayEquals(new int[] {1, 2, 3}, ArrayArgumentEnum.INT.getIntarr());
    }

    @Test
    void longArray() {
        assertArrayEquals(new long[] {11111111111L, 2222222222L, 3333333333L}, ArrayArgumentEnum.LONG.getLongarr());
    }

    @Test
    void stringArray() {
        assertArrayEquals(new String[] {"red", "green", "blue"}, ArrayArgumentEnum.STRING.getStrarr());
    }

    @Test
    void booleanArray() {
        assertArrayEquals(new boolean[] {true, false}, ArrayArgumentEnum.BOOLEAN.getBoolarr());
    }

    @Test
    void enumArray() {
        assertArrayEquals(new OsType[] {Windows, Unix}, ArrayArgumentEnum.WINDOWS_UNIX.getOperatingSystems());
    }

    @Test
    void countriesArray() {
        assertArrayEquals(new Country[] {Country.ENGLAND, Country.WELSH, Country.SCOTLAND}, Country.UNITED_KINDOM.getFederatedState());
        assertEquals(IsoAlpha2.GB, Country.UNITED_KINDOM.getIso());
    }

    //TODO: this test should throw exception
    @Test
    void wrongNameInAnnotation() {
        assertTrue(WrongArgumentNameEnum.values().length > 0);
    }

    @Test
    void wrongDateFormat() {
        ExceptionInInitializerError e = assertThrows(ExceptionInInitializerError.class, WrongAnnotationEnum::values);
        assertEquals(IllegalStateException.class, e.getCause().getClass());
        assertEquals(InvocationTargetException.class, e.getCause().getCause().getClass());
        assertEquals(IllegalArgumentException.class, e.getCause().getCause().getCause().getClass());
    }

    @Test
    void wrongArgumentType() {
        NoSuchMethodError e = assertThrows(NoSuchMethodError.class, WrongArgType::values);
        assertTrue(e.getMessage().contains("parse"));
    }

    @Test
    void wrongNumberOfArguements() {
        ExceptionInInitializerError e = assertThrows(ExceptionInInitializerError.class, WrongNumberOfArguments::values);
        assertEquals(IllegalStateException.class, e.getCause().getClass());
        assertEquals(NoSuchMethodException.class, e.getCause().getCause().getClass());
    }

    @Test
    void wrongNumberOfArguementsOfFactoryMethod() {
        ExceptionInInitializerError e = assertThrows(ExceptionInInitializerError.class, WrongNumberOfArguments2::values);
        assertEquals(IllegalStateException.class, e.getCause().getClass());
        assertEquals(NoSuchMethodException.class, e.getCause().getCause().getClass());
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


    public enum ArrayArgumentEnum implements Initializable {
        @ByteValue(name = "bytearr", value = {1, 2, 3})
        BYTE,
        @ShortValue(name = "shortarr", value = {1, 2, 3})
        SHORT,
        @IntValue(name = "intarr", value = {1, 2, 3})
        INT,
        @LongValue(name = "longarr", value = {11111111111L, 2222222222L, 3333333333L})
        LONG,
        @BooleanValue(name = "boolarr", value = {true, false})
        BOOLEAN,
        @Value(name = "strarr", value = {"red", "green", "blue"})
        STRING,


        @Platform(name = "operatingSystems", os = {Windows, Unix})
        WINDOWS_UNIX,

        ;

        private byte[] bytearr;
        private short[] shortarr;
        private int[] intarr;
        private long[] longarr;
        private boolean[] boolarr;
        private String[] strarr;
        private OsType[] operatingSystems;


        ArrayArgumentEnum() {
            this.bytearr = argument();
            this.shortarr = argument();
            this.intarr = argument();
            this.longarr = argument();
            this.boolarr = argument();
            this.strarr = argument();
            this.operatingSystems = argument();
        }

        public byte[] getBytearr() {
            return bytearr;
        }

        public short[] getShortarr() {
            return shortarr;
        }

        public int[] getIntarr() {
            return intarr;
        }

        public long[] getLongarr() {
            return longarr;
        }

        public boolean[] getBoolarr() {
            return boolarr;
        }

        public String[] getStrarr() {
            return strarr;
        }

        public OsType[] getOperatingSystems() {
            return operatingSystems;
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

    enum WrongArgumentNameEnum implements Initializable {
        @Value(name = "string", value = "wrong")
        ONE();

        @SuppressWarnings("unused") // this is failure test; the field cannot be used
        private final String str;

        WrongArgumentNameEnum() {
            this.str = argument();
        }
    }


    enum WrongAnnotationEnum implements Initializable {
        @DateValue(name = "date", value = "1789-07-14", format = "wrong format")
        WRONG_DATE_FORMAT(),
        ;

        @SuppressWarnings("unused") // this is failure test; the field cannot be used
        private final Date date;

        WrongAnnotationEnum() {
            this.date = argument();
        }
    }


    @Argument
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @Value(name = "${name}", value = "${value}", type = Date.class, factory = SimpleDateFormat.class, factoryMethod = "parse")
    @Value(name = "factoryArgument", value = "${format}")
    public @interface WrongDateValue {
        String name();
        int value(); // wrong type
        String format() default "yyyy-MM-dd'T'HH:mm:ss.SSSZ"; // example: 2001-07-04T12:08:56.235-0700
    }


    enum WrongArgType implements Initializable {
        @WrongDateValue(name = "date", value = 17890714, format = "yyyyMMdd")
        WRONG_DATE_TYPE(),
        ;

        @SuppressWarnings("unused") // this is failure test; the field cannot be used
        private final Date date;

        WrongArgType() {
            this.date = argument();
        }
    }



    @Argument
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @Value(name = "${name}", value = "${value}", type = Date.class, factory = SimpleDateFormat.class, factoryMethod = "parse")
    @Value(name = "factoryArgument", value = "${format}")
    @Value(name = "factoryArgument", value = "${format2}")
    public @interface ExtraArgumentDateValue {
        String name();
        String value();
        String format() default "yyyy-MM-dd'T'HH:mm:ss.SSSZ"; // example: 2001-07-04T12:08:56.235-0700
        String format2() default "yyyy-MM-dd'T'HH:mm:ss.SSSZ"; // example: 2001-07-04T12:08:56.235-0700
    }

    enum WrongNumberOfArguments implements Initializable {
        @ExtraArgumentDateValue(name = "date", value = "17890714", format = "yyyyMMdd", format2 = "yyyyMMdd")
        DATE(),
        ;

        @SuppressWarnings("unused") // this is failure test; the field cannot be used
        private final Date date;

        WrongNumberOfArguments() {
            this.date = argument();
        }
    }


    @Argument
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @Value(name = "${name}", value = "${value}", type = Date.class, factory = SimpleDateFormat.class, factoryMethod = "parse")
    @Value(name = "factoryArgument", value = "${format}")
    @Value(name = "factoryArgument", value = "${format2}")
    public @interface ExtraArgumentParseDateValue {
        String name();
        String[] value();
        String format() default "yyyy-MM-dd'T'HH:mm:ss.SSSZ"; // example: 2001-07-04T12:08:56.235-0700
    }

    enum WrongNumberOfArguments2 implements Initializable {
        @ExtraArgumentParseDateValue(name = "date", value = {"17890714", "17890714"}, format = "yyyyMMdd")
        DATE(),
        ;

        @SuppressWarnings("unused") // this is failure test; the field cannot be used
        private final Date date;

        WrongNumberOfArguments2() {
            this.date = argument();
        }
    }
}
