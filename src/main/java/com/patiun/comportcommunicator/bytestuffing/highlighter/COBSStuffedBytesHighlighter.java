package com.patiun.comportcommunicator.bytestuffing.highlighter;

import com.patiun.comportcommunicator.util.ByteStringFormatter;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Highlighter;
import java.util.Arrays;
import java.util.List;

import static com.patiun.comportcommunicator.bytestuffing.COBSByteStuffer.DELIMITER_BYTE;
import static com.patiun.comportcommunicator.bytestuffing.COBSByteStuffer.END_BYTE;

public class COBSStuffedBytesHighlighter implements StuffedBytesHighlighter {

    public COBSStuffedBytesHighlighter() {
    }

    @Override
    public void highlightStuffedBytes(JTextArea textArea, String textAreaBytesDelimiterRegex, String frameBytesDelimiter, int stuffedBytesBeginningIndex, int stuffedBytesEndIndex) throws BadLocationException {
        String text = textArea.getText();
        List<String> hexStringList = Arrays.asList(text.split(textAreaBytesDelimiterRegex));
        List<Byte> bytes = ByteStringFormatter.hexStringListToByteList(hexStringList);
        Highlighter highlighter = textArea.getHighlighter();
        int i = stuffedBytesBeginningIndex;
        while (i < stuffedBytesEndIndex) {
            highlighter.addHighlight(i * 2 + i * frameBytesDelimiter.length(), i * 2 + i * frameBytesDelimiter.length() + 2, HIGHLIGHT_PAINTER);
            int byteValue = Byte.toUnsignedInt(bytes.get(i));
            if (byteValue == END_BYTE) {
                break;
            }
            i += byteValue - DELIMITER_BYTE;
        }
    }
}
