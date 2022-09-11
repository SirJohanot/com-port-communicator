package com.patiun.comportcommunicator.main;

import com.patiun.comportcommunicator.window.InputWindow;
import com.patiun.comportcommunicator.window.OutputWindow;

public class Main {

    public static void main(String[] args) {
        new InputWindow().setupAndLaunch(6);
        new OutputWindow().setupAndLaunch(7);
    }
}
