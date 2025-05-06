package com.lifedrained.demoexam.controller;

import com.lifedrained.demoexam.Transmitter;
import com.lifedrained.demoexam.entity.AbstractEntity;
import com.lifedrained.demoexam.entity.repo.AbstractRepo;
import com.lifedrained.demoexam.utils.ColumnCreator;
import com.lifedrained.demoexam.utils.StageLauncher;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.text.Font;

//Контроллер таблицы
public class TableViewController {

    @FXML
    private Button controlBtn , checkBtn;


    @FXML
    private TableView<AbstractEntity> table;


    @FXML
    public void initialize() {
        controlBtn.setOnMouseClicked(event -> {
                StageLauncher.launch(controlBtn,"control.fxml" , "Редактирование данных");
        });

        checkBtn.setOnMouseClicked(event -> {
           StageLauncher.launch(checkBtn,"list-view.fxml" , "Просмотр имеющихся материалов");
        });

        initTable();
    }

    private void initTable(){
        AbstractRepo<AbstractEntity> repo = new  AbstractRepo<>(Transmitter.getCurrentTabArgs().getEntityClazz()){};

        table.setItems(FXCollections.observableList(repo.findAll()));
        table.getColumns().addAll(new ColumnCreator(Transmitter.getCurrentTabArgs().getEntityClazz()).createColumns());
    }


}