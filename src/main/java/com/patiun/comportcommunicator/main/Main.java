package com.patiun.comportcommunicator.main;

import com.fazecast.jSerialComm.SerialPort;
import com.patiun.comportcommunicator.window.ControlWindow;
import com.patiun.comportcommunicator.window.InputWindow;
import com.patiun.comportcommunicator.window.OutputWindow;
import com.patiun.comportcommunicator.window.StatsWindow;

public class Main {

    public static void main(String[] args) {
        ControlWindow.getInstance();
        StatsWindow.getInstance();
        SerialPort serialPort = SerialPort.getCommPorts()[4];
        if (serialPort.openPort()) {
            serialPort.closePort();
            new InputWindow(4);
            new OutputWindow(7);
        } else {
            serialPort.closePort();
            new InputWindow(6);
            new OutputWindow(5);
        }
    }
}
