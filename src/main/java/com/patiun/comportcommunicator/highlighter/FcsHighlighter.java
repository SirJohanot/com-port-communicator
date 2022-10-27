package com.patiun.comportcommunicator.highlighter;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Highlighter;
import java.util.Arrays;
import java.util.List;


public class FcsHighlighter extends AbstractChainedHighlighter {

    private final int fcsByteIndex;

    public FcsHighlighter(int fcsByteIndex) {
        this.fcsByteIndex = fcsByteIndex;
    }

    public FcsHighlighter(BytesHighlighter successor, int fcsByteIndex) {
        super(successor);
        this.fcsByteIndex = fcsByteIndex;
    }

    @Override
    protected void personalHighlightBytes(JTextArea textArea, String textAreaBytesDelimiterRegex, String frameBytesDelimiter) throws BadLocationException {
        String text = textArea.getText();
        List<String> stringList = Arrays.asList(text.split(textAreaBytesDelimiterRegex));
        Highlighter highlighter = textArea.getHighlighter();
        int byteStartIndex = charIndexOfByte(fcsByteIndex, stringList, frameBytesDelimiter);
        int byteStringLength = stringList.get(fcsByteIndex).length();
        highlighter.addHighlight(byteStartIndex, byteStartIndex + byteStringLength, HIGHLIGHT_PAINTER);
    }
}
