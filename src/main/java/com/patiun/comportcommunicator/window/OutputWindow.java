package com.patiun.comportcommunicator.window;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.patiun.comportcommunicator.factory.ComponentFactory;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class OutputWindow extends JFrame {

    private SerialPort outputPort;

    private JTextArea outputTextArea;

    public OutputWindow(String outputPortDescriptor) throws HeadlessException {
        super();
        setUpCommPort(outputPortDescriptor);
        setUpMyself();
        add(ComponentFactory.getInstance().buildLabel(outputPort.getDescriptivePortName() + " - Output"), BorderLayout.PAGE_START);
        add(setUpOutput(), BorderLayout.CENTER);
        launch();
    }

    private void setUpCommPort(String outputPortDescriptor) {
        DebugWindow.getInstance().sendMessage(getTitle(), "Found the following ports: " + Arrays.toString(SerialPort.getCommPorts()));

        outputPort = SerialPort.getCommPort(outputPortDescriptor);
        outputPort.openPort();
        outputPort.addDataListener(new SerialPortDataListener() {
            @Override
            public int getListeningEvents() {
                return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
            }

            @Override
            public void serialEvent(SerialPortEvent serialPortEvent) {
                if (serialPortEvent.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE) {
                    return;
                }
                int bytesAvailable = outputPort.bytesAvailable();
                if (bytesAvailable > 0) {
                    DebugWindow.getInstance().sendMessage(getTitle(), "Bytes available to read: " + bytesAvailable);
                    byte[] bytes = new byte[bytesAvailable];
                    int bytesRead = outputPort.readBytes(bytes, bytesAvailable);
                    DebugWindow.getInstance().sendMessage(getTitle(), "Bytes read: " + bytesRead);
                    StatsWindow.getInstance().incrementBytesTransferred(outputPort.getDescriptivePortName(), bytesRead);
                    outputTextArea.append(new String(bytes));
                    pack();
                }
            }
        });
    }

    private void setUpMyself() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ComponentFactory.getInstance().setUpFrame(this, outputPort.getDescriptivePortName() + " - Output");
    }

    private JTextArea setUpOutput() {
        outputTextArea = ComponentFactory.getInstance().buildTextArea(false);
        return outputTextArea;
    }

    private void launch() {
        pack();
        setVisible(true);
    }
}
