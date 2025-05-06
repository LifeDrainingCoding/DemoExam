package com.lifedrained.demoexam.utils;

import com.lifedrained.demoexam.entity.AbstractEntity;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.text.Font;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.time.DateTimeException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//Создает столбцы для отображения данных
public class ColumnCreator {
    private final Class<? extends AbstractEntity> entityClass;

    public <T extends AbstractEntity> ColumnCreator(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public <T extends AbstractEntity> List<TableColumn<T,String>> createColumns(){
        List<TableColumn<T,String>> columns = new ArrayList<>();

        List<Field> fields = collectFields();

        fields.forEach(field -> {
            TableColumn<T,String> column = new TableColumn<>();
            column.setCellValueFactory(cellData -> {
                Object value = cellData.getValue();
                field.setAccessible(true);
                try {
                    String dateValue = DateUtils.parseLocalTemporal(field.get(value));
                    if (!dateValue.isEmpty()) {
                        return new SimpleStringProperty(dateValue);
                    }

                    return new SimpleStringProperty(String.valueOf(field.get(value)));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    return new SimpleStringProperty("N/A");

                }catch (DateTimeException  e) {
                    e.printStackTrace();
                    return new SimpleStringProperty("Date error");
                }
            });
            column.setText(field.getName());
            column.setCellFactory(cell -> new TableCell<>(){
                @Override
                protected void updateItem(String s, boolean b) {
                    super.updateItem(s, b);
                    if (s == null || b){
                        setGraphic(null);
                        setText(null);
                    }else {
                        setFont(new Font("Constantia", 18));
                        setText(s);
                    }
                }
            });


            columns.add(column);
        });

        return columns;
    }
    private List<Field> collectFields(){
        List<Field> fields = new ArrayList<>();
        fields.addAll(Arrays.stream(entityClass.getSuperclass().getDeclaredFields()).toList());
        fields.addAll(Arrays.stream(entityClass.getDeclaredFields()).toList());
        return fields;

    }
}
