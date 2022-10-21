package com.patiun.comportcommunicator.window;

import com.patiun.comportcommunicator.bytestuffing.highlighter.StuffedBytesHighlighter;
import com.patiun.comportcommunicator.factory.ComponentFactory;
import com.patiun.comportcommunicator.util.ByteStringFormatter;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.util.List;

public class StatsPanel extends JPanel {

    private static final String FRAME_BYTES_DELIMITER = " ";
    private static final String BYTES_DELIMITER_REGEX = "[ \n]";

    private final JTextArea textArea;
    private final StuffedBytesHighlighter stuffedBytesHighlighter;

    public StatsPanel(StuffedBytesHighlighter stuffedBytesHighlighter) {
        super();
        ComponentFactory.setUpPanel(this);
        setLayout(new BorderLayout());

        add(ComponentFactory.buildLabel("Contents of the latest sent packet, hexadecimal (stuffed bytes are highlighted in red)"), BorderLayout.PAGE_START);

        textArea = ComponentFactory.buildTextArea(false);
        textArea.setText("");

        add(ComponentFactory.buildScrollPane(textArea), BorderLayout.CENTER);

        this.stuffedBytesHighlighter = stuffedBytesHighlighter;
    }

    public void updateFrame(List<Byte> frameData, int stuffedBytesBeginningIndex, int stuffedBytesEndIndex) {
        List<String> hexPresentation = ByteStringFormatter.byteListToHexStringList(frameData);
        String message = String.join(FRAME_BYTES_DELIMITER, hexPresentation);
        String currentText = textArea.getText();
        int currentLength = currentText.equals("") ? 0 : currentText.split(BYTES_DELIMITER_REGEX).length;
        textArea.append(message + "\n");
        try {
            stuffedBytesHighlighter.highlightStuffedBytes(textArea, BYTES_DELIMITER_REGEX, FRAME_BYTES_DELIMITER, currentLength + stuffedBytesBeginningIndex, currentLength + stuffedBytesEndIndex);
        } catch (BadLocationException e) {
            DebugPanel.getInstance().sendMessage("Stats", e.getMessage());
        }
    }

}
