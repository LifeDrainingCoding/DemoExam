package com.lifedrained.demoexam.controller;

import com.lifedrained.demoexam.Main;
import com.lifedrained.demoexam.Transmitter;
import com.lifedrained.demoexam.tempdata.TabArgs;
import com.lifedrained.demoexam.utils.AlertUtils;
import com.lifedrained.demoexam.utils.EMUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;


import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
//контроллер для вкладок
public class MainController {

    public static final String TABLE_VIEW = "table-view.fxml";

    private List<TabArgs> args;

    @FXML
    private TabPane tableSelector;


    @FXML
    public void initialize() {
        //Логика обработчка вкладок с таблицами
        tableSelector.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            TabArgs tabArgs = args.get(tableSelector.getSelectionModel().getSelectedIndex());
            Transmitter.setCurrentTabArgs(tabArgs);
            try {
                FXMLLoader loader = new FXMLLoader(Main.class.getResource(TABLE_VIEW));
                VBox node = loader.load();

                newValue.setGraphic(null);
                newValue.setContent(node);
                newValue.setText(tabArgs.getEntityName());
            }catch (IOException ex){
                AlertUtils.showError("Error during loading fxml: " ,ex);
                throw new RuntimeException(ex);
            }
        });
        tableSelector.getSelectionModel().selectFirst();

        tableSelector.getTabs().addAll(createTabs());
    }

//Метод для непосредственного формирования вкладок
    private List<Tab> createTabs() {
        args = EMUtils.getTableArgs();
        return args.stream().map(tabArgs -> new Tab(tabArgs.getEntityName())).collect(Collectors.toList());
    }
}
