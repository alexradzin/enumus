package org.enumus.initializer;

import org.enumus.samples.Color;
import org.enumus.samples.Environment;
import org.enumus.samples.Platform;
import org.enumus.samples.Software;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UtilTest {
    @Test
    void nameOfEnumConstant() {
        assertEquals(Color.RED.name(), Util.name(Color.RED));
    }

    @Test
    void nameOfNotEnum() {
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> Util.name(this));
        assertEquals(NoSuchMethodException.class, e.getCause().getClass());
    }

    @Test
    void noAnnotations() {
        assertArrayEquals(new Annotation[0], Util.annotations(Color.RED, Color.RED.name()));
    }

    @Test
    void annotations() {
        Annotation[] annotations = Util.annotations(Software.INTELLIJ, Software.INTELLIJ.name());
        assertNotNull(annotations);
        assertEquals(3, annotations.length);
        assertArrayEquals(new Class[] {Value.class, Platform.class, Environment.class}, Arrays.stream(annotations).map(Annotation::annotationType).toArray(Class[]::new));
    }

    @Test
    void noName() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> Util.annotations(Color.RED, "red"));
        assertTrue(e.getMessage().startsWith("Field red does not exist"));
    }

    @Test
    void goodMethod() {
        assertEquals("toString", Util.method(getClass(), "toString").getName());
    }

    @Test
    void methodDoesNotExist() {
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> Util.method(getClass(), "doesNotExist"));
        assertEquals(NoSuchMethodException.class, e.getCause().getClass());
    }

    @Test
    void goodInvoke() throws NoSuchMethodException {
        assertEquals("lo", Util.invoke("hello", String.class.getMethod("substring", int.class), 3));
    }

    @Test
    void wrongArgumentTypeInvoke() {
        // integer argument is expected but string is provided
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> Util.invoke("hello", String.class.getMethod("substring", int.class), "3"));
        assertEquals("argument type mismatch", e.getMessage());
    }

    @Test
    void npeInvoke() {
        assertThrows(NullPointerException.class, () -> Util.invoke(null, String.class.getMethod("substring", int.class), 3));
    }

    @Test
    void badInvoke() {
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> Util.invoke("hello", String.class.getMethod("substring", int.class), -1));
        assertEquals(InvocationTargetException.class, e.getCause().getClass());
        assertEquals(StringIndexOutOfBoundsException.class, e.getCause().getCause().getClass());
    }
}