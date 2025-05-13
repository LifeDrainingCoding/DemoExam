package com.lifedrained.demoexam.utils;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.xmlbeans.impl.tool.XsbDumper;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//Утилитарный класс для парсинга аргументов
public class ReflectUtils {
    //Парсит аргументы в указанный класс
    public static <T> Object parseArgument(Class<T> clazz , String value) {
        value = value.trim().replaceAll("[\r\n]", "");
        if (clazz == Boolean.class || clazz == boolean.class) {
            return Boolean.parseBoolean(value);
        }
        if (clazz == Integer.class || clazz == int.class) {

            if (value.contains("."))
                value = value.substring( 0,value.indexOf(".") );
            return Integer.parseInt(value);
        }

        if (clazz == Double.class || clazz == double.class) {
            return Double.parseDouble(value);
        }
        if (clazz == Float.class || clazz == float.class) {
            return Float.parseFloat(value);
        }
        if (clazz == String.class) {
            return value;
        }
        if (clazz == Long.class || clazz == long.class) {
            if (value.contains("."))
                value = value.substring( 0,value.indexOf(".") );
            return Long.parseLong(value);
        }

        if (clazz == LocalDateTime.class ){
            StringBuilder sb = new StringBuilder(value);
            if (!value.contains(":")){

                sb.append(" 00:00");
            }
            return DateUtils.getLDTFromString(sb.toString());
        }
        if (clazz == LocalDate.class ){
            return DateUtils.getLDFromString(value);
        }
        if (clazz == LocalTime.class ){
            return DateUtils.getTimeFromString(value);
        }

        if (clazz == BigDecimal.class ){
            return new BigDecimal(value);
        }


        return value;
    }

    public static List<Object> formArgs(List<Field> fields , List<String> values) {
        List<Object> list = new ArrayList<>();
        for (int i = 0; i < fields.size(); i++) {
            String s = values.get(i);
            Field f = fields.get(i);
            System.out.println("parsing "+s+" for "+f.getType().getSimpleName());
            list.add(parseArgument(f.getType(), s));
        }
        return list;
    }
}
