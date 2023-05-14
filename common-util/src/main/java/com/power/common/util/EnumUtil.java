package com.power.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.power.common.model.EnumDictionary;

/**
 * @author yu 2019/12/7.
 */
public class EnumUtil {


    /**
     * get enum values
     *
     * @param clazz     class
     * @param codeField code field
     * @param descField desc field
     * @param <T>       subclass of EnumDictionary
     * @return list
     */
    public static <T extends EnumDictionary> List<T> getEnumInformation(Class<?> clazz, String codeField, String descField) {
        if (Objects.isNull(clazz)) {
            throw new RuntimeException("Enum class can't be null.");
        }
        if (!clazz.isEnum()) {
            throw new RuntimeException(clazz.getCanonicalName() + " is not an enum class.");
        }
        if (StringUtil.isEmpty(codeField) || StringUtil.isEmpty(descField)) {
            throw new RuntimeException(clazz.getCanonicalName()
                + ":Please specify the code field name of the dictionary enumeration class and the field name that describes the dictionary code information");
        }
        Class<Enum> enumClass = (Class<Enum>) clazz;
        Enum[] objects = enumClass.getEnumConstants();
        String valueMethodName;
        if (codeField.endsWith("()")) {
            valueMethodName = codeField.replace("()","");
        } else {
            valueMethodName = "get" + StringUtil.firstToUpperCase(codeField);
        }
        String descMethodName;
        if (descField.endsWith("()")) {
            descMethodName = descField.replace("()","");
        } else {
            descMethodName = "get" + StringUtil.firstToUpperCase(descField);
        }
        List<T> enumDictionaryList = new ArrayList<>();
        try {
            Method valueMethod = clazz.getMethod(valueMethodName);
            valueMethod.setAccessible(true);
            Method descMethod = clazz.getMethod(descMethodName);
            descMethod.setAccessible(true);
            for (Enum enumType : objects) {
                Object val = valueMethod.invoke(enumType);
                Object desc = descMethod.invoke(enumType);
                EnumDictionary dataDict = new EnumDictionary();
                String type = ClassUtil.getSimpleTypeName(val);
                dataDict.setType(type);
                dataDict.setDesc(String.valueOf(desc));
                dataDict.setValue(String.valueOf(val));
                String name = enumType.name();
                int ordinal = enumType.ordinal();
                dataDict.setName(name);
                dataDict.setOrdinal(ordinal);
                enumDictionaryList.add((T) dataDict);
            }
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return enumDictionaryList;
    }

    /**
     * Get enum information
     *
     * @param clazz java class
     * @return hash map, Key is class Name ,value is enum Constants.
     */
    public static Map<String, List<Map<String, Object>>> getEnumInformation(Class<?> clazz) {
        if (Objects.isNull(clazz)) {
            throw new RuntimeException("Enum class can't be null.");
        }
        if (!clazz.isEnum()) {
            throw new RuntimeException("It's not an enum class.");
        }
        Map<String, List<Map<String, Object>>> enumTypeMap = new HashMap<>();
        Class<Enum> enumClass = (Class<Enum>) clazz;
        List<Map<String, Object>> list = new ArrayList<>();
        String clazzName = enumClass.getName();
        Enum[] enumConstants = enumClass.getEnumConstants();
        Map<String, Method> methods = getMethods(enumClass, enumConstants);
        for (Enum enumType : enumConstants) {
            Map<String, Object> map = new HashMap<>();
            for (String key : methods.keySet()) {
                try {
                    Method method = methods.get(key);
                    Object invoke = method.invoke(enumType);
                    map.put(key, invoke);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            String name = enumType.name();
            int ordinal = enumType.ordinal();
            map.put("name", name);
            map.put("ordinal", ordinal);
            list.add(map);
        }
        enumTypeMap.put(clazzName, list);
        return enumTypeMap;
    }

    /**
     * Get enum names
     *
     * @param enumClass Enum Class
     * @return List of enum name
     */
    public static List<String> getNames(Class<? extends Enum<?>> enumClass) {
        if (Objects.isNull(enumClass)) {
            throw new RuntimeException("Enum class can't be null.");
        }
        Enum[] enumConstants = enumClass.getEnumConstants();
        if (Objects.isNull(enumConstants)) {
            return new ArrayList<>(0);
        }
        List<String> list = new ArrayList<>(enumConstants.length);
        for (Enum<?> e : enumConstants) {
            list.add(e.name());
        }
        return list;
    }

    private static Map<String, Method> getMethods(Class<Enum> enumClass, Enum[] enumConstants) {
        List<String> enumNames = new ArrayList<>();
        Map<String, Method> methods = new HashMap<>();
        for (Enum enumType : enumConstants) {
            enumNames.add(enumType.name());
        }
        Field[] declaredFields = enumClass.getDeclaredFields();
        for (Field field : declaredFields) {
            String fieldName = field.getName();
            if (!enumNames.contains(fieldName) && !fieldName.equals("$VALUES")) {
                try {
                    Method method = enumClass.getMethod("get" + (fieldName.charAt(0) + "").toUpperCase() + fieldName.substring(1));
                    methods.put(fieldName, method);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        }
        return methods;
    }
}
