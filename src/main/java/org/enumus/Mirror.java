package org.enumus;

import static java.lang.String.format;
import static java.util.Arrays.stream;

public class Mirror {
    @SuppressWarnings("unchecked")
    @SafeVarargs
    public static <E extends Enum<E>> void of(Class<E> image, Class<E> ... images) throws IllegalStateException {
        Class<? extends Enum> caller = discoverCaller();
        if (!Enum.class.isAssignableFrom(caller)) {
            throw new IllegalStateException(format("Caller of Mirror.of() %s is not enum", caller.getName()));
        }
        Mirror.mirrors(caller, image, images);
    }

    @SuppressWarnings("unchecked")
    private static <E extends Enum<E>> Class<E> discoverCaller() {
        try {
            return (Class<E>)Class.forName(stream(new Throwable().getStackTrace()).filter(e -> !e.getClassName()
                    .equals(Mirror.class.getName()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException(format("Cannot discover caller's class. It seems that %s calls itself", Mirror.class.getName())))
                    .getClassName());
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }


    @SuppressWarnings({"SuspiciousSystemArraycopy", "WeakerAccess"})
    @SafeVarargs
    public static <T extends Enum<T>, M extends Enum<M>> void mirrors(Class<T> mirror, Class<M> image, Class<? extends Enum> ... images) {
        T[] mirrorConstants = mirror.getEnumConstants();
        @SuppressWarnings("unchecked") // the luck of generic arrays.
        Class<M>[] allImages = new Class[images.length + 1];
        allImages[0] = image;
        System.arraycopy(images, 0, allImages, 1, images.length);

        int i = 0;
        for (Class<M> img : allImages) {
            if (mirror.equals(img)) {
                throw new IllegalArgumentException(format("Class %s cannot be mirror of itself", mirror.getName()));
            }
            M[] imgEnumConstants = img.getEnumConstants();
            for (int j = 0; i < mirrorConstants.length && j < imgEnumConstants.length; j++, i++) {
                if (!mirrorConstants[i].name().equals(imgEnumConstants[j].name())) {
                    throw new IllegalStateException(format("Element #%d of mirror %s.%s does not reflect the source %s.%s", j, image.getName(), imgEnumConstants[j], mirror.getName(), mirrorConstants[i]));
                }
            }
        }
        if (i != mirrorConstants.length) {
            throw new IllegalStateException(format("Source and mirror enums have different number of elements: %s#%d vs. %s#%d", mirror.getName(), mirrorConstants.length, image.getName(), i));
        }

    }
}
