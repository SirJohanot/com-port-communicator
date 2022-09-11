package com.patiun.comportcommunicator.window;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.patiun.comportcommunicator.factory.ComponentFactory;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

import static com.patiun.comportcommunicator.constant.ConstantValues.BACKGROUND_COLOR;

public class OutputWindow extends JFrame {

    private SerialPort outputPort;

    private JTextArea outputTextArea;

    public OutputWindow() throws HeadlessException {
        super();
    }

    private void setUpCommPort(int outputPortIndex) {
        DebugWindow.getInstance().sendMessage(getTitle(), Arrays.toString(SerialPort.getCommPorts()));

        outputPort = SerialPort.getCommPorts()[outputPortIndex];
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
                    outputTextArea.append(new String(bytes));
                }
            }
        });
        ControlWindow.getInstance().registerPort(outputPort);
    }

    private void setUpMyself() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        setTitle(outputPort.getDescriptivePortName() + " - Output");
        getContentPane().setBackground(BACKGROUND_COLOR);
        setBackground(BACKGROUND_COLOR);
        setUndecorated(false);
    }

    private JTextArea setUpOutput() {
        outputTextArea = ComponentFactory.getInstance().buildTextArea(false);
        return outputTextArea;
    }

    private void launch() {
        pack();
        setVisible(true);
    }

    public void setupAndLaunch(int outputPortIndex) {
        setUpCommPort(outputPortIndex);
        setUpMyself();
        add(ComponentFactory.getInstance().buildLabel(outputPort.getDescriptivePortName() + " - Output"));
        add(setUpOutput());
        launch();
    }
}
