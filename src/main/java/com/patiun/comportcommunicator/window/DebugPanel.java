package com.patiun.comportcommunicator.window;

import com.patiun.comportcommunicator.factory.ComponentFactory;

import javax.swing.*;
import java.awt.*;

public class DebugPanel extends JPanel {

    private static final int CHARACTER_LIMIT = 8192;

    private static final DebugPanel INSTANCE = new DebugPanel();

    JTextArea textArea;

    private DebugPanel() {
        super();
        ComponentFactory.setUpPanel(this);
        setLayout(new BorderLayout());

        add(ComponentFactory.buildLabel("Debug"), BorderLayout.PAGE_START);

        textArea = ComponentFactory.buildTextArea(false);
        add(ComponentFactory.buildScrollPane(textArea), BorderLayout.CENTER);
    }

    public void sendMessage(String windowTitle, String message) {
        String currentText = textArea.getText();
        if (currentText.length() > CHARACTER_LIMIT) {
            textArea.setText("");
        }
        textArea.append(windowTitle + ": " + message + "\n");
    }

    public static DebugPanel getInstance() {
        return INSTANCE;
    }

}
