package com.patiun.comportcommunicator.highlighter;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Highlighter;
import java.util.Arrays;
import java.util.List;


public class FcsHighlighter extends AbstractChainedHighlighter {

    private final int fcsByteBeginIndex;
    private final int fcsBytesNumber;

    public FcsHighlighter(int fcsByteBeginIndex, int fcsBytesNumber) {
        this.fcsByteBeginIndex = fcsByteBeginIndex;
        this.fcsBytesNumber = fcsBytesNumber;
    }

    public FcsHighlighter(BytesHighlighter successor, int fcsByteBeginIndex, int fcsBytesNumber) {
        super(successor);
        this.fcsByteBeginIndex = fcsByteBeginIndex;
        this.fcsBytesNumber = fcsBytesNumber;
    }

    @Override
    protected void personalHighlightBytes(JTextArea textArea, int oldLength, String textAreaBytesDelimiterRegex, String frameBytesDelimiter) throws BadLocationException {
        String text = textArea.getText();
        List<String> stringList = Arrays.asList(text.split(textAreaBytesDelimiterRegex));
        Highlighter highlighter = textArea.getHighlighter();
        for (int i = 0; i < fcsBytesNumber; i++) {
            int currentByteIndex = oldLength + fcsByteBeginIndex + i;
            int byteStartIndex = charIndexOfByte(currentByteIndex, stringList, frameBytesDelimiter);
            int byteEndIndex = byteStartIndex + stringList.get(currentByteIndex).length();
            highlighter.addHighlight(byteStartIndex, byteEndIndex, HIGHLIGHT_PAINTER);
        }
    }
}
