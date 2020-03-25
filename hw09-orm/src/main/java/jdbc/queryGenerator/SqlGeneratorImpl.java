package jdbc.queryGenerator;

import helpers.ObjectHelper;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class SqlGeneratorImpl<T> implements SqlGenerator {
    public final String DB_FIELD_TYPE_STRING = "string";
    public final String DB_FIELD_TYPE_NUMBER = "num";
    public final String DB_FIELD_TYPE_BOOL = "bool";
    private Class clazz;
    private String fieldIdName;
    private Map<String, String> fieldTypes;

    public SqlGeneratorImpl(Class clazz) {
        this.clazz = clazz;
        this.fieldIdName = getIdFieldName();
        this.fieldTypes = getFieldTypes();
    }

    protected String getIdFieldName() {
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                return field.getName();
            }
        }

        throw new SqlGeneratorException("Can't find id annotation");
    }

    protected Map<String, String> getFieldTypes() {
        var fieldTypesMap = new HashMap<String, String>();
        for (Field field : clazz.getDeclaredFields()) {
            String typeName = field.getType().getName();
            if (ObjectHelper.isString(typeName) || ObjectHelper.isChar(typeName)) {
                fieldTypesMap.put(field.getName(), this.DB_FIELD_TYPE_STRING);
                continue;
            }

            if (ObjectHelper.isInteger(typeName) || ObjectHelper.isFloat(typeName)) {
                fieldTypesMap.put(field.getName(), this.DB_FIELD_TYPE_NUMBER);
                continue;
            }

            if (ObjectHelper.isBoolean(typeName)) {
                fieldTypesMap.put(field.getName(), this.DB_FIELD_TYPE_BOOL);
                continue;
            }
        }

        if (fieldTypesMap.isEmpty()) {
            throw new SqlGeneratorException("Can't find fields");
        }

        return fieldTypesMap;
    }

    public String getInsertQuery() {
        String fieldsWithoutIdSeparatedByComma = getFieldsWithoutId(false);
        String tableName = clazz.getSimpleName().toLowerCase();

        return String.format("INSERT INTO %s(%s) VALUES (%s)",
                tableName,
                fieldsWithoutIdSeparatedByComma,
                getQuestionMarksFromFieldsString(fieldsWithoutIdSeparatedByComma));
    }

    public String getSelectQuery() {
        var allFieldsName = String.join(",", fieldTypes.keySet());

        String tableName = clazz.getSimpleName().toLowerCase();

        return String.format("SELECT %s FROM %s WHERE %s = ?",
                allFieldsName,
                tableName,
                fieldIdName);
    }

    public String getUpdateQuery() {
        String tableName = clazz.getSimpleName().toLowerCase();

        return String.format("UPDATE %s SET %s WHERE %s = ?",
                tableName,
                getFieldsWithoutId(true),
                fieldIdName);
    }

    protected String getFieldsWithoutId(boolean isAddQuestionMark) {
        return fieldTypes
                .keySet()
                .stream()
                .filter(fieldName -> !fieldName.equals(fieldIdName))
                .map(fieldName -> !isAddQuestionMark ? fieldName : fieldName + " = ?")
                .collect(Collectors.joining(","));
    }

    protected String getQuestionMarksFromFieldsString(String fieldsSeparatedByComma) {
        var count = (int) fieldsSeparatedByComma.chars().filter(ch -> ch == ',').count();
        if (count == 0) {
            return "?";
        }
        count++;
        String[] tmpArray = new String[count];
        Arrays.fill(tmpArray, 0, count, "?");

        return String.join(",", tmpArray);
    }
}
