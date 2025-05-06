package com.lifedrained.demoexam;
import com.lifedrained.demoexam.controller.FullscreenToggler;
import com.lifedrained.demoexam.singleton.ParamConfig;
import com.lifedrained.demoexam.utils.EMUtils;
import com.lifedrained.demoexam.utils.MySqlUrlParams;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


import java.io.IOException;
import java.sql.PreparedStatement;


public  class Main extends Application  {

    public static final String MAIN_VIEW = "main.fxml";


    @Override
    public void start(Stage stage) throws IOException {
        configure();

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(MAIN_VIEW));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Просмотр склада");
        stage.setScene(scene);
        stage.setFullScreen(true);
        scene.setOnKeyPressed(new FullscreenToggler(stage));
        stage.getIcons().add(new Image(Main.class.getResourceAsStream("icon.png")));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        EMUtils.shutdown();
    }

    private void configure(){
        ParamConfig.DB_NAME = "db";
        ParamConfig.getInstance().configure(MySqlUrlParams.values());
        EMUtils.getSession().doWork(connection -> {
            PreparedStatement ps = connection.prepareStatement("set global local_infile=true");
            ps.execute();
        });
    }

}