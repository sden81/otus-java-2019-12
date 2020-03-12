import java.lang.reflect.Field;
import java.util.*;

public class ObjectHelper {
    public static final Set<String> TYPE_INTEGER = new HashSet<String>(Arrays.asList(
            "byte",
            "short",
            "int",
            "long",
            "java.lang.Byte",
            "java.lang.Short",
            "java.lang.Integer",
            "java.lang.Long"
    ));

    public static final Set<String> TYPE_FLOAT = new HashSet<String>(Arrays.asList(
            "float",
            "double",
            "java.lang.Float",
            "java.lang.Double"
    ));

    public static final Set<String> TYPE_BOOLEAN = new HashSet<String>(Arrays.asList(
            "boolean",
            "java.lang.Boolean"
    ));

    public static final Set<String> TYPE_CHAR = new HashSet<String>(Arrays.asList(
            "char",
            "java.lang.Character"
    ));

    protected static boolean isString(String type) {
        return type.equals("java.lang.String");
    }

    protected static boolean isArray(Field field) {
        return field.getType().isArray();
    }

    protected static boolean isCollection(Field field) {
        return List.class.isAssignableFrom(field.getType()) || Set.class.isAssignableFrom(field.getType());
    }

    protected static boolean isInteger(String type) {
        return ObjectHelper.TYPE_INTEGER.contains(type);
    }

    protected static boolean isFloat(String type) {
        return ObjectHelper.TYPE_FLOAT.contains(type);
    }

    protected static boolean isBoolean(String type) {
        return ObjectHelper.TYPE_BOOLEAN.contains(type);
    }

    protected static boolean isChar(String type) {
        return ObjectHelper.TYPE_CHAR.contains(type);
    }
}
