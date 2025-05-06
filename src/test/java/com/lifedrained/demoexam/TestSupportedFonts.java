package com.lifedrained.demoexam;

import javafx.scene.text.Font;
import org.junit.jupiter.api.Test;



public class TestSupportedFonts {

    @Test
    public void testFonts() {
        Font.getFamilies().forEach(System.out::println);
    }

}
