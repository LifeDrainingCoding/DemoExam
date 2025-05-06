package com.lifedrained.demoexam.controller;

import javafx.application.Platform;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Region;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public interface TooltipNotifier {
    default Tooltip showTooltip(Region view, int duration, String text) {
        Tooltip tooltip = new Tooltip();
        tooltip.setText(text);
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        double x = view.localToScreen(view.getBoundsInLocal()).getMinX();
        double y = view.localToScreen(view.getBoundsInLocal()).getMinY();
        tooltip.show(view, x, y);
        executor.schedule(()-> Platform.runLater(tooltip::hide),duration, TimeUnit.SECONDS);
        return tooltip;
    }
    default void showTooltipPlatform(Region view, int duration, String text) {
        Platform.runLater(() -> showTooltip(view, duration, text));
    }
}
