package com.patiun.comportcommunicator.window;

import com.patiun.comportcommunicator.entity.Packet;
import com.patiun.comportcommunicator.factory.ComponentFactory;
import com.patiun.comportcommunicator.util.StuffedBytesHighlighter;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;

public class StatsPanel extends JPanel {

    private final JTextArea textArea;
    private final StuffedBytesHighlighter stuffedBytesHighlighter;

    public StatsPanel(StuffedBytesHighlighter stuffedBytesHighlighter) {
        super();
        ComponentFactory.setUpPanel(this);
        setLayout(new BorderLayout());

        add(ComponentFactory.buildLabel("Current data frame (stuffed bytes highlighted red)"), BorderLayout.PAGE_START);

        textArea = ComponentFactory.buildTextArea(false);

        add(textArea, BorderLayout.CENTER);

        this.stuffedBytesHighlighter = stuffedBytesHighlighter;
    }

    public void setMessage(String message) {
        textArea.setText(message);
        try {
            stuffedBytesHighlighter.highlightStuffedBytes(textArea, Packet.ESCAPE_BYTE);
        } catch (BadLocationException e) {
            DebugPanel.getInstance().sendMessage("Stats", e.getMessage());
        }
    }

}
