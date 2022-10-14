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
    public void highlightStuffedBytes(JTextArea textArea, String bytesDelimiter, int stuffedBytesBeginningIndex, int stuffedBytesEndIndex) throws BadLocationException {
        String text = textArea.getText();
        List<String> hexStringList = Arrays.asList(text.split(bytesDelimiter));
        List<Byte> bytes = ByteStringFormatter.hexStringListToByteList(hexStringList);
//        DebugPanel.getInstance().sendMessage("highlighting", "bytes " + bytes);
        Highlighter highlighter = textArea.getHighlighter();
        int i = stuffedBytesBeginningIndex;
        while (i < stuffedBytesEndIndex) {
//            DebugPanel.getInstance().sendMessage("highlighting", "start " + (i * 2 + i * bytesDelimiter.length()) + " end " + (i * 2 + i * bytesDelimiter.length() + 2));
            highlighter.addHighlight(i * 2 + i * bytesDelimiter.length(), i * 2 + i * bytesDelimiter.length() + 2, HIGHLIGHT_PAINTER);
            int byteValue = Byte.toUnsignedInt(bytes.get(i));
            if (byteValue == END_BYTE) {
                break;
            }
            i += byteValue - DELIMITER_BYTE;
        }
    }
}
