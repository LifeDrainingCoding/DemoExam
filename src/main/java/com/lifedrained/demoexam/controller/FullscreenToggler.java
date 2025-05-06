package com.lifedrained.demoexam.controller;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class FullscreenToggler implements EventHandler<KeyEvent> {
    private Stage stage;

    public FullscreenToggler(Stage stage) {
        this.stage = stage;
    }
    @Override
    public void handle(KeyEvent event) {
        if (event.getCode()  == KeyCode.F1) {
            stage.setFullScreen(!stage.isFullScreen());
        }
    }
}
