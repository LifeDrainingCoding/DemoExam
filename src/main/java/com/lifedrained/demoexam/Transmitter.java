package com.lifedrained.demoexam;

import com.lifedrained.demoexam.tempdata.TabArgs;

public class Transmitter {

    private static Transmitter INSTANCE;

    private TabArgs currentTabArgs;

    private Transmitter() {}

    private static Transmitter getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Transmitter();
        }

        return INSTANCE;
    }

    public static TabArgs getCurrentTabArgs() {
        return getInstance().currentTabArgs;
    }

    public static void setCurrentTabArgs(TabArgs tabArgs) {
        getInstance().currentTabArgs = tabArgs;
    }


}
