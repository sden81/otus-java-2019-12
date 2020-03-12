import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;

public class DiyJson {

    public String toJson(Object src) {
        if (src == null) {
            return "null";
        }

        return processObject(src);
    }

    protected String processObject(Object object) {
        if (object == null) {
            return "";
        }

        StringBuilder jsonObject = new StringBuilder("{");

        String fieldResult;
        Class<?> clazz = object.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            fieldResult = processField(field, object);
            if (!fieldResult.equals("")) {
                jsonObject.append(processField(field, object));
                jsonObject.append(",");
            }
        }

        jsonObject.deleteCharAt(jsonObject.length() - 1);
        jsonObject.append("}");

        return jsonObject.toString();
    }

    protected String processField(Field field, Object object) {
        try {
            if (Modifier.isPrivate(field.getModifiers()) || Modifier.isProtected(field.getModifiers())) {
                field.setAccessible(true);
            }

            if (field.get(object) == null) {
                return "";
            }

            String typeName = field.getType().getName();

            if (ObjectHelper.isString(typeName) || ObjectHelper.isChar(typeName)) {
                return String.format("\"%s\":\"%s\"", field.getName(), field.get(object).toString());
            }

            if (ObjectHelper.isInteger(typeName) || ObjectHelper.isFloat(typeName) || ObjectHelper.isBoolean(typeName)) {
                return String.format("\"%s\":%s", field.getName(), field.get(object).toString());
            }

            if (ObjectHelper.isCollection(field)) {
                return String.format("\"%s\":%s", field.getName(), processCollection((Collection) field.get(object)));
            }

            if (ObjectHelper.isArray(field)) {
                return String.format("\"%s\":%s", field.getName(), processArray(field.get(object)));
            }

            //it is Object
            return String.format("\"%s\":%s", field.getName(), processObject(field.get(object)));
        } catch (IllegalAccessException e) {
            throw new RuntimeException("IllegalAccessException");
        }
    }

    protected String processArray(Object array) {
        if (Array.getLength(array) == 0){
            return "[]";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("[");
        String renderResult;
        for (int i = 0; i < Array.getLength(array); i++) {
            renderResult = processArrayItem(Array.get(array, i));
            if (!renderResult.equals("")) {
                sb.append(renderResult).append(",");
            }
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append("]");

        return sb.toString();
    }

    protected String processCollection(Collection collection) {
        if (collection.size() == 0){
            return "[]";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("[");
        String renderResult;
        for (Object item : collection) {
            renderResult = processArrayItem(item);
            if (!renderResult.equals("")) {
                sb.append(renderResult).append(",");
            }
        }

        sb.deleteCharAt(sb.length() - 1);
        sb.append("]");

        return sb.toString();
    }

    protected String processArrayItem(Object itemObject) {
        String typeName = itemObject.getClass().getName();

        if (ObjectHelper.isString(typeName) || ObjectHelper.isChar(typeName)) {
            return String.format("\"%s\"", itemObject.toString());
        }

        if (ObjectHelper.isInteger(typeName) || ObjectHelper.isFloat(typeName) || ObjectHelper.isBoolean(typeName)) {
            return itemObject.toString();
        }

        return processObject(itemObject);
    }
}
