package com.patiun.comportcommunicator.main;

import com.fazecast.jSerialComm.SerialPort;
import com.patiun.comportcommunicator.config.PortDescriptor;
import com.patiun.comportcommunicator.exception.NoPortFoundException;
import com.patiun.comportcommunicator.factory.ComponentFactory;
import com.patiun.comportcommunicator.window.*;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {
        SerialPort serialPort = SerialPort.getCommPort(PortDescriptor.FIRST_INPUT_PORT_DESCRIPTOR);
        if (serialPort.openPort()) {
            serialPort.closePort();
            setUpAndLaunchWindow(PortDescriptor.FIRST_INPUT_PORT_DESCRIPTOR, PortDescriptor.FIRST_OUTPUT_PORT_DESCRIPTOR);
        } else {
            serialPort.closePort();
            setUpAndLaunchWindow(PortDescriptor.SECOND_INPUT_PORT_DESCRIPTOR, PortDescriptor.SECOND_OUTPUT_PORT_DESCRIPTOR);
        }
    }

    private static void setUpAndLaunchWindow(String inputPort, String outputPort) {
        JFrame window = new JFrame();
        ComponentFactory.getInstance().setUpFrame(window, inputPort + ", " + outputPort + " - COM Port Communicator");
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setLayout(new BorderLayout());

        window.add(ControlPanel.getInstance(), BorderLayout.PAGE_START);

        try {
            window.add(new InputPanel(setUpCommPort(inputPort)), BorderLayout.WEST);
            window.add(new OutputPanel(setUpCommPort(outputPort)), BorderLayout.EAST);
        } catch (NoPortFoundException e) {
            DebugPanel.getInstance().sendMessage("System", e.getMessage());
        }

        window.add(StatsPanel.getInstance(), BorderLayout.CENTER);

        window.add(DebugPanel.getInstance(), BorderLayout.PAGE_END);

        window.pack();
        window.setVisible(true);
    }

    private static SerialPort setUpCommPort(String portDescriptor) throws NoPortFoundException {
        SerialPort serialPort = SerialPort.getCommPort(portDescriptor);
        if (!serialPort.openPort()) {
            throw new NoPortFoundException(portDescriptor + " port is not present in the system, the application can not function correctly");
        }
        return serialPort;
    }
}
