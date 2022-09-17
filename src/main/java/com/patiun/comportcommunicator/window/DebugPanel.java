package com.patiun.comportcommunicator.window;

import com.patiun.comportcommunicator.factory.ComponentFactory;

import javax.swing.*;
import java.awt.*;

public class DebugPanel extends JPanel {

    private static final DebugPanel INSTANCE = new DebugPanel();

    JTextArea textArea;

    private DebugPanel() {
        super();
        ComponentFactory.getInstance().setUpPanel(this);
        setLayout(new BorderLayout());

        add(ComponentFactory.getInstance().buildLabel("Debug"), BorderLayout.PAGE_START);

        textArea = ComponentFactory.getInstance().buildTextArea(false);
        add(ComponentFactory.getInstance().buildScrollPane(textArea), BorderLayout.CENTER);
    }

    public void sendMessage(String windowTitle, String message) {
        textArea.append(windowTitle + ": " + message + "\n");
    }

    public static DebugPanel getInstance() {
        return INSTANCE;
    }

}
