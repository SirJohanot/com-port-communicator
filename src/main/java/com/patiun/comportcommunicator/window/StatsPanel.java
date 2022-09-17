package com.patiun.comportcommunicator.window;

import com.fazecast.jSerialComm.SerialPort;
import com.patiun.comportcommunicator.config.PortDescriptor;
import com.patiun.comportcommunicator.factory.ComponentFactory;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;

public class StatsPanel extends JPanel {

    private static final StatsPanel INSTANCE = new StatsPanel();

    private final LinkedHashMap<String, Integer> portNamesToBytes = new LinkedHashMap<>();

    private final JTextArea textArea;

    private StatsPanel() {
        super();
        ComponentFactory.getInstance().setUpPanel(this);
        setLayout(new BorderLayout());

        for (String serialPortDescriptor : new String[]{PortDescriptor.FIRST_INPUT_PORT_DESCRIPTOR, PortDescriptor.SECOND_OUTPUT_PORT_DESCRIPTOR, PortDescriptor.SECOND_INPUT_PORT_DESCRIPTOR, PortDescriptor.FIRST_OUTPUT_PORT_DESCRIPTOR}) {
            SerialPort serialPort = SerialPort.getCommPort(serialPortDescriptor);
            portNamesToBytes.put(serialPort.getSystemPortName(), 0);
        }

        add(ComponentFactory.getInstance().buildLabel("Stats"), BorderLayout.PAGE_START);

        textArea = ComponentFactory.getInstance().buildTextArea(false);
        updateTextArea();

        add(textArea, BorderLayout.CENTER);
    }

    public void incrementBytesTransferred(String portName, int value) {
        int currentValue = portNamesToBytes.get(portName);
        portNamesToBytes.put(portName, currentValue + value);
        updateTextArea();
    }

    private void updateTextArea() {
        StringBuilder newText = new StringBuilder();
        boolean outputPort = false;
        int previousBytes = 0;
        for (String portName : portNamesToBytes.keySet()) {
            newText.append(portName);
            if (outputPort) {
                newText.append(" - Bytes transferred: ").append(portNamesToBytes.get(portName) + previousBytes).append("\n");
            } else {
                newText.append(" -> ");
                previousBytes = portNamesToBytes.get(portName);
            }
            outputPort = !outputPort;
        }
        textArea.setText(newText.toString());
    }

    public static StatsPanel getInstance() {
        return INSTANCE;
    }

}
