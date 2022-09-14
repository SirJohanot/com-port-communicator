package com.patiun.comportcommunicator.main;

import com.fazecast.jSerialComm.SerialPort;
import com.patiun.comportcommunicator.config.PortDescriptors;
import com.patiun.comportcommunicator.window.InputWindow;
import com.patiun.comportcommunicator.window.OutputWindow;
import com.patiun.comportcommunicator.window.StatsWindow;

public class Main {

    public static void main(String[] args) {
        StatsWindow.getInstance();
        SerialPort serialPort = SerialPort.getCommPort(PortDescriptors.FIRST_INPUT_PORT_DESCRIPTOR);
        if (serialPort.openPort()) {
            serialPort.closePort();
            new InputWindow(PortDescriptors.FIRST_INPUT_PORT_DESCRIPTOR);
            new OutputWindow(PortDescriptors.FIRST_OUTPUT_PORT_DESCRIPTOR);
        } else {
            serialPort.closePort();
            new InputWindow(PortDescriptors.SECOND_INPUT_PORT_DESCRIPTOR);
            new OutputWindow(PortDescriptors.SECOND_OUTPUT_PORT_DESCRIPTOR);
        }
    }
}
