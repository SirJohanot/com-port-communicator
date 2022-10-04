package com.patiun.comportcommunicator.bytestuffing.highlighter;

import com.patiun.comportcommunicator.util.ByteStringFormatter;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Highlighter;
import java.util.Arrays;
import java.util.List;

import static com.patiun.comportcommunicator.bytestuffing.COBSByteStuffer.DELIMITER_BYTE;

public class COBSStuffedBytesHighlighter implements StuffedBytesHighlighter {

    public COBSStuffedBytesHighlighter() {
    }

    @Override
    public void highlightStuffedBytes(JTextArea textArea, String bytesDelimiter) throws BadLocationException {
        String text = textArea.getText();
        List<String> hexStringList = Arrays.asList(text.split(bytesDelimiter));
        List<Byte> bytes = ByteStringFormatter.hexStringListToByteList(hexStringList);
        Highlighter highlighter = textArea.getHighlighter();
        int i = 0;
        while (i < bytes.size()) {
            highlighter.addHighlight(i * 2 + i * bytesDelimiter.length(), i * 2 + i * bytesDelimiter.length() + 2, HIGHLIGHT_PAINTER);
            byte byteValue = bytes.get(i);
            if (byteValue == DELIMITER_BYTE) {
                break;
            }
            i += byteValue;
        }
    }
}
