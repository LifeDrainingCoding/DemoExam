package com.lifedrained.demoexam.utils;

import com.lifedrained.demoexam.Transmitter;
import com.lifedrained.demoexam.entity.AbstractEntity;
import com.lifedrained.demoexam.entity.repo.AbstractRepo;

import org.hibernate.Session;

import java.io.File;
import java.io.IOException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.lifedrained.demoexam.utils.ExcelParser.DELIMITER;
import static com.lifedrained.demoexam.utils.ExcelParser.LINE_SEPARATOR;

public class CsvImporter {

    //Импортирование CSV данных в таблицу
    public static <T extends AbstractEntity> void importCSV(Class<T> clazz, String content) throws IOException, RuntimeException {
        new Thread(() -> {
            List<Object> entities = new ArrayList<>();

            //Использование рефлексии для универсальности метода
            List<Field> fields = Arrays.stream(clazz.getDeclaredFields()).toList();

            List<String> entries = new ArrayList<>(List.of(content.split(LINE_SEPARATOR)));

            System.out.println("Records count "+entries.size());
            entries.remove(0);
            entries.forEach(row -> {
                System.out.println("Row "+row);
                List<String> values = List.of(row.split(DELIMITER));

                values.forEach(System.out::println);
                List<Object> args = ReflectUtils.formArgs(fields, values);

                Object entity;
                try {
                    System.out.println("arguments size "+args.size());

                    //Создание сущности данных
                    Constructor<?> constructor = clazz.getConstructor();
                    entity = constructor.newInstance();

                    setArgsToObject(entity, fields, args);

                    Field idField = entity.getClass().getSuperclass().getDeclaredField("id");
                    idField.setAccessible(true);
                    System.out.println(idField.get(entity));

                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException |
                         InstantiationException | NoSuchFieldException e) {
                    AlertUtils.showError("Ошибка при парсинге CSV", e);
                    throw new RuntimeException(e);
                }

                entities.add(entity);
            });

            AbstractRepo<AbstractEntity> repo = new AbstractRepo<>(Transmitter.getCurrentTabArgs().getEntityClazz()) {};
            entities.forEach(entity -> {
                repo.save(clazz.cast(entity));
            });
        }).start();

    }

    //метод для установки значей полей сущности
    private static void setArgsToObject(Object entity, List<Field> fields,  List<Object> args) throws IllegalAccessException {
        for (int i = 0; i < fields.size(); i++) {
            Field field = fields.get(i);
            field.setAccessible(true);
            Object arg = args.get(i);
            field.set(entity,arg);
        }
    }
}
