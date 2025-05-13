package com.lifedrained.demoexam.controller;

import com.lifedrained.demoexam.Main;
import com.lifedrained.demoexam.entity.Material;
import com.lifedrained.demoexam.entity.repo.AbstractRepo;
import com.lifedrained.demoexam.utils.StageLauncher;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

import java.io.IOException;

//Контроллер списка склада с материалами
public class ListViewController {
   @FXML
    private ListView<Material> list;

   @FXML
   private Button back;

   @FXML
    private void initialize() {
       AbstractRepo<Material> materialRepo = new AbstractRepo<Material>(Material.class) {
       };

       //Обработка ячеек списка
       list.setCellFactory(param -> new ListCell<Material>() {
           @Override
           protected void updateItem(Material material, boolean b) {
               super.updateItem(material, b);

               if (material == null || b) {
                   setText(null);
                   setGraphic(null);
               }else {
                   FXMLLoader loader = new FXMLLoader(Main.class.getResource("list-item.fxml"));
                   try {
                      VBox root = loader.load();
                      ListItemController controller = loader.getController();

                      controller.materialName.setText("Название: "+material.getMaterialName());
                      controller.minCount.setText("Мин. Кол-во: "+String.valueOf(material.getMinCount()));
                      controller.totalCount.setText("Кол-во на складе: "+String.valueOf(material.getCount()));
                      controller.type.setText("Тип: "+material.getMaterialType());
                      controller.priceDim.setText("Цена: "+material.getMaterialPrice().doubleValue() +"/"+material.getDimUnit());
                      controller.countPack.setText("Кол-во: "+String.valueOf(material.getCountInPack().doubleValue()));


                      setGraphic(root);
                   } catch (IOException e) {
                       throw new RuntimeException(e);
                   }
               }
           }
       });
       list.setItems(FXCollections.observableList(materialRepo.findAll()));

       back.setOnMouseClicked(event -> {
          StageLauncher.launch(back, "main.fxml", "Просмотр склада");
       });
   }
}
