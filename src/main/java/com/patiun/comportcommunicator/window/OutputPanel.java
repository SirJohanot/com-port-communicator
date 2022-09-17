package com.patiun.comportcommunicator.window;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.patiun.comportcommunicator.factory.ComponentFactory;

import javax.swing.*;
import java.awt.*;

public class OutputPanel extends JPanel {

    private final SerialPort outputPort;

    private final JLabel name;
    private final JTextArea outputTextArea;

    public OutputPanel(SerialPort outputPort) throws HeadlessException {
        super();
        ComponentFactory.getInstance().setUpPanel(this);
        setLayout(new BorderLayout());

        this.outputPort = outputPort;
        setUpDataListener();
        ControlPanel.getInstance().registerPort(outputPort);

        name = ComponentFactory.getInstance().buildLabel(outputPort.getSystemPortName() + " - Output");
        add(name, BorderLayout.PAGE_START);

        outputTextArea = ComponentFactory.getInstance().buildTextArea(false);
        add(ComponentFactory.getInstance().buildScrollPane(outputTextArea), BorderLayout.CENTER);
    }

    private void setUpDataListener() {
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
                    DebugPanel.getInstance().sendMessage(name.getText(), "Bytes available to read: " + bytesAvailable);
                    byte[] bytes = new byte[bytesAvailable];
                    int bytesRead = outputPort.readBytes(bytes, bytesAvailable);
                    DebugPanel.getInstance().sendMessage(name.getText(), "Read message '" + new String(bytes) + "' as " + bytesRead + " bytes");
                    StatsPanel.getInstance().incrementBytesTransferred(outputPort.getSystemPortName(), bytesRead);
                    outputTextArea.append(new String(bytes));
                }
            }
        });
    }

}
