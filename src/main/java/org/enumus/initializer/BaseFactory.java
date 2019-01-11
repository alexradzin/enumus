package org.enumus.initializer;

import java.lang.reflect.AccessibleObject;
import java.util.function.Function;

public abstract class BaseFactory<T> implements Function<Object[], T> {
    protected boolean isInvokable(Class[] params, Object[] args) {
        if (params.length == 1 && params[0].isArray()) {
            return args.length > 0 ? params[0].getComponentType().isAssignableFrom(args[0].getClass()) : true;
        }


        if (params.length != args.length) {
            return false;
        }


        for (int i = 0; i < params.length; i++) {
            if (args[i] != null && !params[i].isAssignableFrom(args[i].getClass())) {
                return false;
            }
        }
        return true;
    }

    protected <A extends AccessibleObject> A makeAccessible(A element) {
        element.setAccessible(true);
        return element;
    }
}
