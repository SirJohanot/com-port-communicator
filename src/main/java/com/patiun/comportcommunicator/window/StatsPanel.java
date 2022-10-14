package com.patiun.comportcommunicator.window;

import com.patiun.comportcommunicator.bytestuffing.highlighter.StuffedBytesHighlighter;
import com.patiun.comportcommunicator.factory.ComponentFactory;
import com.patiun.comportcommunicator.util.ByteStringFormatter;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.util.List;

public class StatsPanel extends JPanel {

    private static final String BYTES_DELIMITER = " ";

    private final JTextArea textArea;
    private final StuffedBytesHighlighter stuffedBytesHighlighter;

    public StatsPanel(StuffedBytesHighlighter stuffedBytesHighlighter) {
        super();
        ComponentFactory.setUpPanel(this);
        setLayout(new BorderLayout());

        add(ComponentFactory.buildLabel("Contents of the latest sent packet, hexadecimal (stuffed bytes are highlighted in red)"), BorderLayout.PAGE_START);

        textArea = ComponentFactory.buildTextArea(false);

        add(textArea, BorderLayout.CENTER);

        this.stuffedBytesHighlighter = stuffedBytesHighlighter;
    }

    public void updateFrame(List<Byte> frameData, int stuffedBytesBeginningIndex, int stuffedBytesEndIndex) {
        List<String> hexPresentation = ByteStringFormatter.byteListToHexStringList(frameData);
        String message = String.join(BYTES_DELIMITER, hexPresentation);
        textArea.setText(message);
        try {
            stuffedBytesHighlighter.highlightStuffedBytes(textArea, BYTES_DELIMITER, stuffedBytesBeginningIndex, stuffedBytesEndIndex);
        } catch (BadLocationException e) {
            DebugPanel.getInstance().sendMessage("Stats", e.getMessage());
        }
    }

}
