package com.patiun.comportcommunicator.window;

import com.fazecast.jSerialComm.SerialPort;
import com.patiun.comportcommunicator.factory.ComponentFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;

public class InputWindow extends JFrame {

    private SerialPort inputPort;

    public InputWindow(int inputPortIndex) throws HeadlessException {
        super();
        setUpCommPort(inputPortIndex);
        setUpMyself();
        add(ComponentFactory.getInstance().buildLabel(inputPort.getDescriptivePortName() + " - Input"), BorderLayout.PAGE_START);
        add(setUpInput(), BorderLayout.CENTER);
        launch();
    }

    private void setUpCommPort(int inputPortIndex) {
        DebugWindow.getInstance().sendMessage(getTitle(), Arrays.toString(SerialPort.getCommPorts()));

        inputPort = SerialPort.getCommPorts()[inputPortIndex];
        inputPort.openPort();
    }

    private void setUpMyself() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ComponentFactory.getInstance().setUpFrame(this, inputPort.getDescriptivePortName() + " - Input");
    }

    private JTextArea setUpInput() {
        JTextArea inputTextField = ComponentFactory.getInstance().buildTextArea(true);
        inputTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char keyChar = e.getKeyChar();
                int bytesWritten = inputPort.writeBytes(new byte[]{(byte) keyChar}, 1);
                StatsWindow.getInstance().incrementBytesTransferred(inputPort.getDescriptivePortName(), bytesWritten);
                pack();
            }
        });
        return inputTextField;
    }

    private void launch() {
        pack();
        setVisible(true);
    }

}
