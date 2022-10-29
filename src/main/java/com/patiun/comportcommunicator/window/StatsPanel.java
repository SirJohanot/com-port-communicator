package com.patiun.comportcommunicator.window;

import com.patiun.comportcommunicator.factory.ComponentFactory;
import com.patiun.comportcommunicator.highlighter.BytesHighlighter;
import com.patiun.comportcommunicator.util.ByteStringFormatter;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.util.List;

public class StatsPanel extends JPanel {

    private static final String FRAME_BYTES_DELIMITER = " ";
    private static final String BYTES_DELIMITER_REGEX = "[ \n]";

    private final JTextArea textArea;

    public StatsPanel() {
        super();
        ComponentFactory.setUpPanel(this);
        setLayout(new BorderLayout());

        add(ComponentFactory.buildLabel("Contents of the latest sent packet, hexadecimal (stuffed bytes are highlighted in red)"), BorderLayout.PAGE_START);

        textArea = ComponentFactory.buildTextArea(false);
        textArea.setText("");

        add(ComponentFactory.buildScrollPane(textArea), BorderLayout.CENTER);
    }

    public void updateFrame(List<Byte> frameData, BytesHighlighter bytesHighlighter) {
        List<String> stringPresentation = ByteStringFormatter.byteListToStringList(frameData);
        String message = String.join(FRAME_BYTES_DELIMITER, stringPresentation);
        String currentText = textArea.getText();
        int currentLength = currentText.equals("") ? 0 : currentText.split(BYTES_DELIMITER_REGEX).length;
        textArea.append(message + "\n");
        try {
            bytesHighlighter.highlightBytes(textArea, currentLength, BYTES_DELIMITER_REGEX, FRAME_BYTES_DELIMITER);
        } catch (BadLocationException e) {
            DebugPanel.getInstance().sendMessage("Stats", e.getMessage());
        }
    }

}
