package diyJson;

import helpers.ObjectHelper;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class DiyJson {

    public String toJson(Object src) {
        if (src == null) {
            return "null";
        }

        return processItem(src);
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

        var renderResult = new ArrayList<String>();
        for (int i = 0; i < Array.getLength(array); i++) {
            String result = processItem(Array.get(array, i));
            if (!result.equals("")) {
                renderResult.add(result);
            }
        }

        var sb = new StringBuilder();
        sb.append("[").append(String.join(",",renderResult)).append("]");

        return String.valueOf(sb);
    }

    protected String processCollection(Collection collection) {
        collection.stream().filter(Objects::nonNull);
        if (collection.isEmpty()){
            return "[]";
        }

        var renderResult = new ArrayList<String>();
        collection.stream().forEach(item->renderResult.add(processItem(item)));
        var sb = new StringBuilder();
        sb.append("[").append(String.join(",",renderResult)).append("]");

        return String.valueOf(sb);
    }

    protected String processItem(Object itemObject) {
        String typeName = itemObject.getClass().getName();

        if (ObjectHelper.isString(typeName) || ObjectHelper.isChar(typeName)) {
            return String.format("\"%s\"", itemObject.toString());
        }

        if (ObjectHelper.isInteger(typeName) || ObjectHelper.isFloat(typeName) || ObjectHelper.isBoolean(typeName)) {
            return itemObject.toString();
        }

        if(ObjectHelper.isArray(itemObject.getClass())){
            return processArray(itemObject);
        }

        if(ObjectHelper.isCollection(itemObject.getClass())){
            return processCollection((Collection) itemObject);
        }

        return processObject(itemObject);
    }
}
