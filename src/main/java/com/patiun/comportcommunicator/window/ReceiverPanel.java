package com.patiun.comportcommunicator.window;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.patiun.comportcommunicator.bytestuffing.ByteStuffer;
import com.patiun.comportcommunicator.entity.Packet;
import com.patiun.comportcommunicator.factory.ComponentFactory;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class ReceiverPanel extends JPanel {

    private final SerialPort outputPort;

    private final ByteStuffer byteStuffer;

    private final JLabel name;
    private final JTextArea outputTextArea;

    public ReceiverPanel(SerialPort outputPort, ByteStuffer byteStuffer) throws HeadlessException {
        super();
        ComponentFactory.setUpPanel(this);
        setLayout(new BorderLayout());

        this.outputPort = outputPort;
        setUpDataListener();
        ControlPanel.getInstance().registerPort(outputPort);

        this.byteStuffer = byteStuffer;

        name = ComponentFactory.buildLabel(outputPort.getSystemPortName() + " - Receiver");
        add(name, BorderLayout.PAGE_START);

        outputTextArea = ComponentFactory.buildTextArea(false);
        add(ComponentFactory.buildScrollPane(outputTextArea), BorderLayout.CENTER);
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
                    Packet receivedPacket = new Packet(bytes);
                    List<Byte> dataBytes = receivedPacket.getData();
                    if (dataBytes.size() > Packet.DATA_BYTES_NUMBER) {
                        dataBytes = byteStuffer.restoreBytes(dataBytes);
                    }
                    String bytesString = dataBytes.stream()
                            .map(b -> String.valueOf((char) b.byteValue()))
                            .collect(Collectors.joining());
                    outputTextArea.append(bytesString);
                }
            }
        });
    }

}
