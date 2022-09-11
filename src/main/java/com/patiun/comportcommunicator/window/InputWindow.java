package com.patiun.comportcommunicator.window;

import com.fazecast.jSerialComm.SerialPort;
import com.patiun.comportcommunicator.factory.ComponentFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;

import static com.patiun.comportcommunicator.constant.ConstantValues.BACKGROUND_COLOR;

public class InputWindow extends JFrame {

    private SerialPort inputPort;

    public InputWindow() throws HeadlessException {
        super();
    }

    private void setUpCommPort(int inputPortIndex) {
        DebugWindow.getInstance().sendMessage(getTitle(), Arrays.toString(SerialPort.getCommPorts()));

        inputPort = SerialPort.getCommPorts()[inputPortIndex];
        inputPort.openPort();
        ControlWindow.getInstance().registerPort(inputPort);
    }

    private void setUpMyself() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        setTitle(inputPort.getDescriptivePortName() + " - Input");
        getContentPane().setBackground(BACKGROUND_COLOR);
        setBackground(BACKGROUND_COLOR);
        setUndecorated(false);
    }

    private JTextArea setUpInput() {
        JTextArea inputTextField = ComponentFactory.getInstance().buildTextArea(true);
        inputTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char keyChar = e.getKeyChar();
                inputPort.writeBytes(new byte[]{(byte) keyChar}, 1);
            }
        });
        return inputTextField;
    }

    private void launch() {
        pack();
        setVisible(true);
    }

    public void setupAndLaunch(int inputPortIndex) {
        setUpCommPort(inputPortIndex);
        setUpMyself();
        add(ComponentFactory.getInstance().buildLabel(inputPort.getDescriptivePortName() + " - Input"));
        add(setUpInput());
        launch();
    }

}
