package com.patiun.comportcommunicator.window;

import javax.swing.*;
import java.awt.*;

import static com.patiun.comportcommunicator.constant.ConstantValues.BACKGROUND_COLOR;
import static com.patiun.comportcommunicator.constant.ConstantValues.MAIN_COLOR;

public class DebugWindow extends JFrame {

    private static final DebugWindow INSTANCE = new DebugWindow();

    JTextArea textArea = new JTextArea();

    private DebugWindow() {
        super();
        setTitle("Debug");
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        getContentPane().setBackground(BACKGROUND_COLOR);
        setBackground(BACKGROUND_COLOR);
        setUndecorated(false);

        textArea.setBackground(BACKGROUND_COLOR);
        textArea.setFont(new Font("Consolas", Font.BOLD, 20));
        textArea.setForeground(MAIN_COLOR);
        textArea.setMinimumSize(new Dimension(700, 400));

        add(textArea);

        pack();
        setVisible(true);
    }

    public static DebugWindow getInstance() {
        return INSTANCE;
    }

    public void sendMessage(String windowTitle, String message) {
        textArea.append(windowTitle + ": " + message + "\n");
    }
}
