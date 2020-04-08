package helpers;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ObjectHelper {
    private static final Set<String> TYPE_INTEGER = new HashSet<String>(Arrays.asList(
            "byte",
            "short",
            "int",
            "long",
            "java.lang.Byte",
            "java.lang.Short",
            "java.lang.Integer",
            "java.lang.Long"
    ));

    private static final Set<String> TYPE_FLOAT = new HashSet<String>(Arrays.asList(
            "float",
            "double",
            "java.lang.Float",
            "java.lang.Double"
    ));

    private static final Set<String> TYPE_BOOLEAN = new HashSet<String>(Arrays.asList(
            "boolean",
            "java.lang.Boolean"
    ));

    private static final Set<String> TYPE_CHAR = new HashSet<String>(Arrays.asList(
            "char",
            "java.lang.Character"
    ));

    public static boolean isString(String type) {
        return type.equals("java.lang.String");
    }

    public static boolean isArray(Field field) {
        return field.getType().isArray();
    }

    public static boolean isArray(Class clazz) {
        return clazz.isArray();
    }

    public static boolean isCollection(Field field) {
        return List.class.isAssignableFrom(field.getType()) || Set.class.isAssignableFrom(field.getType());
    }

    public static boolean isCollection(Class clazz) {
        return List.class.isAssignableFrom(clazz) || Set.class.isAssignableFrom(clazz);
    }

    public static boolean isInteger(String type) {
        return ObjectHelper.TYPE_INTEGER.contains(type);
    }

    public static boolean isFloat(String type) {
        return ObjectHelper.TYPE_FLOAT.contains(type);
    }

    public static boolean isBoolean(String type) {
        return ObjectHelper.TYPE_BOOLEAN.contains(type);
    }

    public static boolean isChar(String type) {
        return ObjectHelper.TYPE_CHAR.contains(type);
    }
}
