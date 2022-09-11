package com.patiun.comportcommunicator.factory;

import javax.swing.*;
import java.awt.*;

import static com.patiun.comportcommunicator.constant.ConstantValues.*;

public class ComponentFactory {

    private static final ComponentFactory INSTANCE = new ComponentFactory();

    private ComponentFactory() {
    }

    public static ComponentFactory getInstance() {
        return INSTANCE;
    }

    public JLabel buildLabel(String text) {
        JLabel label = new JLabel(text);
        label.setHorizontalTextPosition(SwingConstants.CENTER);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setOpaque(true);
        label.setBackground(BACKGROUND_COLOR);
        label.setFont(new Font("Consolas", Font.BOLD, 20));
        label.setForeground(MAIN_COLOR);
        return label;
    }

    public JTextArea buildTextArea(boolean editable) {
        JTextArea textArea = new JTextArea();
        textArea.setEditable(editable);
        textArea.setBackground(BACKGROUND_COLOR);
        textArea.setFont(new Font("Consolas", Font.BOLD, 20));
        textArea.setForeground(MAIN_COLOR);
        textArea.setCaretColor(CARET_COLOR);
        return textArea;
    }
}
