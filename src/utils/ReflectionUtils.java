package utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

public final class ReflectionUtils {
    private ReflectionUtils() {}

    // Reflection / RTTI
    public static String inspect(Object obj) {
        if (obj == null) return "null";

        Class<?> cl = obj.getClass();

        String fields = Arrays.stream(cl.getDeclaredFields())
                .map(Field::getName)
                .collect(Collectors.joining(", "));

        String methods = Arrays.stream(cl.getDeclaredMethods())
                .map(Method::getName)
                .distinct()
                .sorted()
                .collect(Collectors.joining(", "));

        return "Class: " + cl.getName() + "\n" +
                "Fields: " + fields + "\n" +
                "Methods: " + methods;
    }
}
