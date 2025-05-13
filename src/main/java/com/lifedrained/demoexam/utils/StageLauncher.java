package com.lifedrained.demoexam.utils;


import com.lifedrained.demoexam.Main;
import com.lifedrained.demoexam.controller.FullscreenToggler;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class StageLauncher {
    public static void launch(Node node, String fxmlPath, String title){
        Platform.runLater(()->{
            Stage currentStage = (Stage) node.getScene().getWindow();

            FXMLLoader loader = new FXMLLoader(Main.class.getResource(fxmlPath));
            Stage loadStage = new Stage();
            loadStage.initModality(Modality.APPLICATION_MODAL);
            try {
                Scene scene =new Scene(loader.load());
                loadStage.setTitle(title);
                loadStage.setScene(scene);
                scene.setOnKeyPressed(new FullscreenToggler(loadStage));
                loadStage.show();
                currentStage.close();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });

    }
}
