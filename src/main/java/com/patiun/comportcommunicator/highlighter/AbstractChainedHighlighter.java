package com.patiun.comportcommunicator.highlighter;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.util.List;

public abstract class AbstractChainedHighlighter implements BytesHighlighter {

    private final BytesHighlighter successor;

    public AbstractChainedHighlighter() {
        successor = null;
    }

    public AbstractChainedHighlighter(BytesHighlighter successor) {
        this.successor = successor;
    }

    @Override
    public void highlightBytes(JTextArea textArea, String textAreaBytesDelimiterRegex, String frameBytesDelimiter) throws BadLocationException {
        this.personalHighlightBytes(textArea, textAreaBytesDelimiterRegex, frameBytesDelimiter);
        if (successor != null) {
            successor.highlightBytes(textArea, textAreaBytesDelimiterRegex, frameBytesDelimiter);
        }
    }

    protected abstract void personalHighlightBytes(JTextArea textArea, String textAreaBytesDelimiterRegex, String frameBytesDelimiter) throws BadLocationException;

    protected int charIndexOfByte(int byteIndex, List<String> byteList, String bytesDelimiter) {
        int delimiterLength = bytesDelimiter.length();
        int result = 0;
        for (int i = 0; i < byteIndex; i++) {
            result += byteList.get(i).length();
            result += delimiterLength;
        }
        return result;
    }
}
