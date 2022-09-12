package com.patiun.comportcommunicator.window;

import com.patiun.comportcommunicator.factory.ComponentFactory;

import javax.swing.*;
import java.awt.*;

public class DebugWindow extends JFrame {

    private static final String WINDOW_NAME = "Debug";

    private static final DebugWindow INSTANCE = new DebugWindow();

    JTextArea textArea;

    private DebugWindow() {
        super();
        ComponentFactory.getInstance().setUpFrame(this, WINDOW_NAME);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        textArea = ComponentFactory.getInstance().buildTextArea(false);

        add(textArea, BorderLayout.CENTER);

        pack();
        setVisible(true);
    }

    public static DebugWindow getInstance() {
        return INSTANCE;
    }

    public void sendMessage(String windowTitle, String message) {
        textArea.append(windowTitle + ": " + message + "\n");
        pack();
    }
}
