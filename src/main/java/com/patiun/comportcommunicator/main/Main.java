package com.patiun.comportcommunicator.main;

import com.fazecast.jSerialComm.SerialPort;
import com.patiun.comportcommunicator.exception.NoPortFoundException;
import com.patiun.comportcommunicator.factory.ComponentFactory;
import com.patiun.comportcommunicator.window.*;

import javax.swing.*;
import java.awt.*;

import static com.patiun.comportcommunicator.config.PortDescriptor.*;

public class Main {

    public static void main(String[] args) {
        setUpAndLaunchWindow();
    }

    private static void setUpAndLaunchWindow() {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setLayout(new BorderLayout());

        try {
            topologyCheck();

            String inputDescriptor;
            String outputDescriptor;

            SerialPort firstInput = SerialPort.getCommPort(FIRST_INPUT_PORT_DESCRIPTOR);
            if (firstInput.openPort()) {
                firstInput.closePort();
                inputDescriptor = FIRST_INPUT_PORT_DESCRIPTOR;
                outputDescriptor = FIRST_OUTPUT_PORT_DESCRIPTOR;
            } else {
                firstInput.closePort();
                inputDescriptor = SECOND_INPUT_PORT_DESCRIPTOR;
                outputDescriptor = SECOND_OUTPUT_PORT_DESCRIPTOR;
            }

            ComponentFactory.getInstance().setUpFrame(window, inputDescriptor + ", " + outputDescriptor + " - COM Port Communicator");

            SerialPort inputSerialPort = SerialPort.getCommPort(inputDescriptor);
            inputSerialPort.openPort();

            SerialPort outputSerialPort = SerialPort.getCommPort(outputDescriptor);
            outputSerialPort.openPort();

            window.add(ControlPanel.getInstance(), BorderLayout.PAGE_START);
            
            StatsPanel statsPanel = new StatsPanel(inputSerialPort.getSystemPortName());
            window.add(statsPanel, BorderLayout.CENTER);

            window.add(new SenderPanel(inputSerialPort, statsPanel), BorderLayout.WEST);
            window.add(new ReceiverPanel(outputSerialPort), BorderLayout.EAST);

        } catch (NoPortFoundException e) {
            DebugPanel.getInstance().sendMessage("System", e.getMessage());
        }

        window.add(DebugPanel.getInstance(), BorderLayout.PAGE_END);

        window.pack();
        window.setVisible(true);
    }

    private static void topologyCheck() throws NoPortFoundException {
        if (SerialPort.getCommPorts().length <= SECOND_INPUT_PORT_NUMBER || !SerialPort.getCommPorts()[FIRST_INPUT_PORT_NUMBER].getSystemPortName().equals(FIRST_INPUT_PORT_DESCRIPTOR) || !SerialPort.getCommPorts()[FIRST_OUTPUT_PORT_NUMBER].getSystemPortName().equals(FIRST_OUTPUT_PORT_DESCRIPTOR) || !SerialPort.getCommPorts()[SECOND_INPUT_PORT_NUMBER].getSystemPortName().equals(SECOND_INPUT_PORT_DESCRIPTOR) || !SerialPort.getCommPorts()[SECOND_OUTPUT_PORT_NUMBER].getSystemPortName().equals(SECOND_OUTPUT_PORT_DESCRIPTOR)) {
            throw new NoPortFoundException("Invalid port topology, the application can not function correctly");
        }
    }
}
