package com.lifedrained.demoexam.controller;

import com.lifedrained.demoexam.Main;
import com.lifedrained.demoexam.Transmitter;
import com.lifedrained.demoexam.entity.AbstractEntity;
import com.lifedrained.demoexam.entity.repo.AbstractRepo;
import com.lifedrained.demoexam.utils.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class DataController implements TooltipNotifier {

    @FXML
    private VBox fieldContainer;

    @FXML
    private Button addBtn, delBtn, updateBtn, backBtn, importBtn;

    private AbstractRepo<AbstractEntity> repo;

    private Class<? extends AbstractEntity> clazz;


    private List<Field> fields;

    private ObservableList<TextField> listTF;

    private TextField idTF;


    @FXML
    private void initialize() {

        clazz = Transmitter.getCurrentTabArgs().getEntityClazz();

        repo = new AbstractRepo<>(Transmitter.getCurrentTabArgs().getEntityClazz()) {
        };

        initFields();

        initBtns();
    }

    private void initFields() {

        fieldContainer.getChildren().clear();

        listTF = FXCollections.observableArrayList();
        fields = new ArrayList<>(Arrays.stream(clazz.getDeclaredFields()).toList());
        List<Constructor<?>> constructors = new ArrayList<>(List.of(clazz.getConstructors()));

        constructors.removeIf(constructor -> constructor.getParameterCount() == 0);

        //Comparator.comparingInt() вычитает 1 параметр из 2 параметра (1-2)
        constructors.stream().max(Comparator.comparingInt(Constructor::getParameterCount))
                .ifPresent(constructor -> {

                    System.out.println(constructor.getParameterCount());

                    List<Class<?>> types = new ArrayList<>(Arrays.stream(constructor.getParameterTypes()).toList());

                    if (constructor.getParameterCount() != types.size()) {
                        System.out.println(types.size());
                        throw new RuntimeException("parameter not matches types size");
                    }

                    fields.removeIf(field -> field.getName().equals("id"));

                    if (constructor.getParameterCount() != fields.size()) {
                        System.out.println(constructor.getParameterCount());
                        System.out.println(fields.size());
                        throw new RuntimeException("parameter not matches fields size");
                    }


                    fields.forEach(field -> {
                        String s = field.getName() + "(" + field.getType().getSimpleName() + ")";
                        listTF.add(createTF(s));
                    });
                    fieldContainer.getChildren().addAll(listTF);

                    idTF = createTF("id (long)");
                    fieldContainer.getChildren().addFirst(idTF);
                });

    }

    private void initBtns() {
        importBtn.setOnMouseClicked(event -> {
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser
                    .ExtensionFilter("CSV и XLSX файлы", "*.xlsx", "*.csv");
            fileChooser.setSelectedExtensionFilter(extFilter);
            fileChooser.getExtensionFilters().addAll(extFilter);

            File file = fileChooser.showOpenDialog(importBtn.getScene().getWindow());
            if (file == null) {
                return;
            }
            String ext = FilenameUtils.getExtension(file.getName());

            if (ext.equals("xlsx")) {

                ExcelParser.parseExcelToCSV(file, (entries, ex) -> {

                    if (ex != null) {
                        AlertUtils.showError("Ошибка при импорте эксель файла в базу данных", ex);
                        ex.printStackTrace();
                        throw new RuntimeException(ex);
                    }
                    System.out.println("entries count "+entries.size());

                    entries.forEach(entry -> {
                        try {
                            CsvImporter.importCSV(clazz, entry);

                            showTooltipPlatform(importBtn, 5, "Успешно импортировано!");
                        } catch (IOException e) {
                            AlertUtils.showError("Ошибка при попытке импортировать одной из страниц" +
                                    FilenameUtils.getBaseName(file.getName()), e);

                            throw new RuntimeException(e);
                        }
                    });

                });

                showTooltip(importBtn, 5, "Выполняется импорт эксель файла в базу данных");

            } else if (ext.equals("csv")) {
                Tooltip tooltip =  showTooltip(importBtn, 5, "Выполняется импорт csv файла в базу данных");

                try {
                    String content = FileUtils.readFileToString(file, StandardCharsets.UTF_8);

                    CsvImporter.importCSV(clazz, content);
                    tooltip.hide();
                    showTooltip(importBtn, 3, "Успешно импортировано!");
                } catch (IOException e) {
                    AlertUtils.showError("Ошибка при попытке импортировать " +
                            FilenameUtils.getBaseName(file.getName()), e);
                    throw new RuntimeException(e);
                }
            } else {
                AlertUtils.showError("Выбран файл с недопустимым расширением " + ext, "");
            }

        });

        backBtn.setOnMouseClicked(event -> {
            StageLauncher.launch(backBtn, Main.MAIN_VIEW, "Просмотр склада");
        });

        addBtn.setOnMouseClicked(event -> {

            Constructor<?> constructor = getEmptyConstructor();

            try {
                System.out.println("size fields: " + fields.size());
                Object object = constructor.newInstance(formFields().toArray());

                repo.save(clazz.cast(object));

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        delBtn.setOnMouseClicked(mouseEvent -> {
            long id = Long.parseLong(idTF.getText());
            Object object = repo.findById(id);
            if (object == null) {
                AlertUtils.showError("Искомый объект не найден по id", "Объект не существует с искомым id");
                return;
            }
            repo.delete(clazz.cast(object));

        });

        updateBtn.setOnMouseClicked(event -> {
            long id = Long.parseLong(idTF.getText());
            Object object = repo.findById(id);
            if (object == null) {
                AlertUtils.showError("Искомый объект не найден по id", "Объект не существует с искомым id");
                return;
            }
            try {
                List<Object> results = formFields();
                for (int i = 0; i < fields.size(); i++) {
                    Field field = fields.get(i);
                    Object result = results.get(i);
                    field.setAccessible(true);
                    field.set(object, result);
                }

                repo.save(clazz.cast(object));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

    }

    private TextField createTF(String hint) {
        TextField tf = new TextField();
        VBox.setMargin(tf, new Insets(16, 0, 0, 0));
        tf.setPromptText(hint);
        tf.setAlignment(Pos.CENTER);
        Font font = new Font("Constantia", 20);
        tf.setFont(font);
        String url = Main.class.getResource("style/TF.css").toExternalForm();
        tf.getStylesheets().add(url);
        return tf;
    }

    private List<Object> formFields() {

        List<Object> args = new ArrayList<>();

        System.out.println(fields.size());

        for (int i = 0; i < fields.size(); i++) {
            Field field = fields.get(i);
            TextField tf = listTF.get(i);
            String value = tf.getText();

            try {
                if (field.getType() == Boolean.class || field.getType() == boolean.class) {
                    args.add(Boolean.parseBoolean(value));
                    continue;
                }
                if (field.getType() == Integer.class || field.getType() == int.class) {
                    args.add(Integer.parseInt(value));
                    continue;
                }

                if (field.getType() == Double.class || field.getType() == double.class) {
                    args.add(Double.parseDouble(value));
                    continue;
                }
                if (field.getType() == Float.class || field.getType() == float.class) {
                    args.add(Float.parseFloat(value));
                    continue;
                }
                if (field.getType() == String.class) {
                    args.add(value);
                    continue;
                }
                if (field.getType() == Long.class || field.getType() == long.class) {
                    args.add(Long.parseLong(value));
                    continue;
                }
                if (field.getType() == LocalDateTime.class) {
                    args.add(DateUtils.getLDTFromString(value));
                    continue;
                }
                if (field.getType() == LocalDate.class) {
                    args.add(DateUtils.getLDFromString(value));
                    continue;
                }
                if (field.getType() == LocalTime.class) {
                    args.add(DateUtils.getTimeFromString(value));
                    continue;
                }

                if (field.getType() == BigDecimal.class) {
                    args.add(new BigDecimal(value));
                }
            } catch (Exception e) {
                AlertUtils.showError("Ошибка при парсинге полей!", e);
                throw new RuntimeException(e);
            }
        }
        System.out.println("args: " + args.size());
        return args;
    }

    private Constructor<?> getEmptyConstructor() {
        List<Constructor<?>> constructors = new ArrayList<>(List.of(clazz.getConstructors()));
        constructors.removeIf(constructor -> {
            System.out.println("Constructor param count: " + constructor.getParameterCount());
            return constructor.getParameterCount() <= 0;
        });
        System.out.println(constructors.get(0).getParameterCount());
        return constructors.get(0);
    }

    private File writeEntryToTemp(String s) {
        File csvFile;
        try {
            csvFile = File.createTempFile("csv", "entry");
            FileUtils.writeStringToFile(csvFile, s, StandardCharsets.UTF_8);
            return csvFile;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void removeHeadersFromCSV(File csvFile) throws IOException {
        String s = FileUtils.readFileToString(csvFile, StandardCharsets.UTF_8);
        List<String> records = new ArrayList<>(Arrays.asList(s.split(ExcelParser.LINE_SEPARATOR)));
        records.remove(0);
        FileUtils.writeLines(csvFile, records);
    }

    private String removeHeadersFromCSV(String csv) throws IOException {
        List<String> records = new ArrayList<>(Arrays.asList(csv.split(ExcelParser.LINE_SEPARATOR)));
        records.remove(0);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < records.size(); i++) {
            sb.append(records.get(i));
            if (i != records.size() - 1) {
                sb.append(ExcelParser.LINE_SEPARATOR);
            }
        }
        return sb.toString();
    }

}
