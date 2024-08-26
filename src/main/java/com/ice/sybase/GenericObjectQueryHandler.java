package com.ice.sybase;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GenericObjectQueryHandler<T> implements JDBCUtil.QueryHandler {

    private final Class<T> type;
    private final List<T> results;

    public GenericObjectQueryHandler(Class<T> type) {
        this.type = type;
        this.results = new ArrayList<>();
    }

    @Override
    public void handle(ResultSet rs) throws SQLException {
        try {
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (rs.next()) {
                T obj = type.getDeclaredConstructor().newInstance();

                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    Object value = rs.getObject(i);

                    // 优先检查字段上的注解
                    Field field = getFieldByColumnName(columnName);
                    if (field != null) {
                        field.setAccessible(true);
                        Method setter = getSetterMethodForField(field);
                        if (setter != null) {
                            setter.invoke(obj, value);
                        } else {
                            field.set(obj, value);
                        }
                    } else {
                        // 如果字段上没有找到映射，检查 setter 方法
                        Method setter = getSetterMethodByColumnName(columnName);
                        if (setter != null) {
                            setter.invoke(obj, value);
                        }
                    }
                }
                results.add(obj);
            }
        } catch (Exception e) {
            throw new SQLException("Error mapping ResultSet to object", e);
        }
    }

    public List<T> getResults() {
        return results;
    }

    // 根据列名获取带有注解的字段
    private Field getFieldByColumnName(String columnName) {
        for (Field field : type.getDeclaredFields()) {
            if (field.isAnnotationPresent(ColumnMapping.class)) {
                ColumnMapping mapping = field.getAnnotation(ColumnMapping.class);
                if (mapping.value().equalsIgnoreCase(columnName)) {
                    return field;
                }
            }
            // 如果没有注解，按字段名匹配
            if (field.getName().equalsIgnoreCase(columnName)) {
                return field;
            }
        }
        return null;
    }

    // 根据字段获取对应的 setter 方法
    private Method getSetterMethodForField(Field field) {
        String setterName = "set" + capitalize(field.getName());
        try {
            return type.getDeclaredMethod(setterName, field.getType());
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    // 根据列名获取带有注解的 setter 方法
    private Method getSetterMethodByColumnName(String columnName) {
        for (Method method : type.getDeclaredMethods()) {
            if (method.isAnnotationPresent(ColumnMapping.class)) {
                ColumnMapping mapping = method.getAnnotation(ColumnMapping.class);
                if (mapping.value().equalsIgnoreCase(columnName)) {
                    return method;
                }
            }
            // 如果没有注解，按方法名匹配
            if (method.getName().equalsIgnoreCase("set" + columnName)) {
                return method;
            }
        }
        return null;
    }

    // 工具方法：将字段名的首字母大写
    private String capitalize(String name) {
        if (name == null || name.length() == 0) {
            return name;
        }
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }
}