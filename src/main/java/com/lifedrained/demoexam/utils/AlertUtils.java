package com.lifedrained.demoexam.utils;

import javafx.application.Platform;
import javafx.scene.control.Alert;

public class AlertUtils {
    public static void showError(String title, Throwable e) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);

            alert.setTitle(title);
            alert.setHeaderText(title);
            StringBuilder builder = new StringBuilder();
            builder.append(e.getMessage());
            builder.append("\n");
            if (e.getCause() != null) {
                builder.append(e.getCause().toString());
            }
            alert.setContentText(builder.toString());
            alert.showAndWait();
        });

    }

    public static void showSuccess(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText(title);
            alert.setContentText(message);
            alert.showAndWait();

        });
    }


    public static void showError(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);

            alert.setTitle(title);
            alert.setHeaderText(title);
            alert.setContentText(message);

            alert.showAndWait();
        });

    }

    public static void showWarning(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(title);
            alert.setHeaderText(title);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
}

