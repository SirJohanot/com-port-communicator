package com.patiun.comportcommunicator.bytestuffing.highlighter;

import com.patiun.comportcommunicator.util.ByteStringFormatter;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Highlighter;
import java.util.Arrays;
import java.util.List;

public class HDLCStuffedBytesHighlighter implements StuffedBytesHighlighter {

    private final byte escapeByte;

    public HDLCStuffedBytesHighlighter(byte escapeByte) {
        this.escapeByte = escapeByte;
    }

    @Override
    public void highlightStuffedBytes(JTextArea textArea, String bytesDelimiter) throws BadLocationException {
        String text = textArea.getText();
        List<String> hexStringList = Arrays.asList(text.split(bytesDelimiter));
        List<Byte> bytes = ByteStringFormatter.hexStringListToByteList(hexStringList);
        Highlighter highlighter = textArea.getHighlighter();
        boolean escapedChar = false;
        for (int i = 0; i < bytes.size(); i++) {
            byte byteValue = bytes.get(i);
            if (!escapedChar && byteValue == escapeByte) {
                highlighter.addHighlight(i * 2 + i * bytesDelimiter.length(), i * 2 + i * bytesDelimiter.length() + 2, HIGHLIGHT_PAINTER);
                escapedChar = true;
            } else {
                escapedChar = false;
            }
        }
    }
}
