package com.patiun.comportcommunicator.factory;

import javax.swing.*;
import java.awt.*;

public class ComponentFactory {

    private static final Color MAIN_COLOR = Color.GREEN;
    private static final Color BACKGROUND_COLOR = Color.BLACK;
    private static final Color CARET_COLOR = Color.CYAN;
    private static final Font FONT = new Font("Consolas", Font.BOLD, 20);
    private static final int TEXT_AREA_ROWS = 15;
    private static final int TEXT_AREA_COLUMNS = 50;

    private static final ComponentFactory INSTANCE = new ComponentFactory();

    private ComponentFactory() {
    }

    public static ComponentFactory getInstance() {
        return INSTANCE;
    }

    private void styleComponent(JComponent component) {
        component.setBackground(BACKGROUND_COLOR);
        component.setFont(FONT);
        component.setForeground(MAIN_COLOR);
        component.setOpaque(true);
    }

    public JLabel buildLabel(String text) {
        JLabel label = new JLabel(text);
        label.setHorizontalTextPosition(SwingConstants.CENTER);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        styleComponent(label);
        return label;
    }

    public JTextArea buildTextArea(boolean editable) {
        JTextArea textArea = new JTextArea(TEXT_AREA_ROWS, TEXT_AREA_COLUMNS);
        textArea.setEditable(editable);
        styleComponent(textArea);
        textArea.setCaretColor(CARET_COLOR);
        return textArea;
    }

    public void setUpFrame(JFrame frame, String title) {
        frame.setTitle(title);
        frame.getContentPane().setBackground(BACKGROUND_COLOR);
        frame.setBackground(BACKGROUND_COLOR);
        frame.setUndecorated(false);
    }

    public JSpinner buildSpinner(SpinnerModel model) {
        JSpinner spinner = new JSpinner(model);
        styleComponent(spinner);
        return spinner;
    }
}
