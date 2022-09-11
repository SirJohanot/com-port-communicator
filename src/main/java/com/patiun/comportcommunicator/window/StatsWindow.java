package com.patiun.comportcommunicator.window;

public class StatsWindow {

    private static final StatsWindow INSTANCE = new StatsWindow();

    private StatsWindow() {
    }

    public static StatsWindow getInstance() {
        return INSTANCE;
    }
}
