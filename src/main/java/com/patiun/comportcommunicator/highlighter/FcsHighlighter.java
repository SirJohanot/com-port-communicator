package com.patiun.comportcommunicator.highlighter;

import com.patiun.comportcommunicator.entity.Packet;
import com.patiun.comportcommunicator.window.DebugPanel;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Highlighter;
import java.util.Arrays;
import java.util.List;


public class FcsHighlighter extends AbstractChainedHighlighter {

    private final int fcsByteBeginIndex;

    public FcsHighlighter(int fcsByteBeginIndex) {
        this.fcsByteBeginIndex = fcsByteBeginIndex;
    }

    public FcsHighlighter(BytesHighlighter successor, int fcsByteBeginIndex) {
        super(successor);
        this.fcsByteBeginIndex = fcsByteBeginIndex;
    }

    @Override
    protected void personalHighlightBytes(JTextArea textArea, String textAreaBytesDelimiterRegex, String frameBytesDelimiter) throws BadLocationException {
        String text = textArea.getText();
        List<String> stringList = Arrays.asList(text.split(textAreaBytesDelimiterRegex));
        Highlighter highlighter = textArea.getHighlighter();
        int byteStartIndex = charIndexOfByte(fcsByteBeginIndex, stringList, frameBytesDelimiter);
        int byteEndIndex = charIndexOfByte(fcsByteBeginIndex + Packet.FCS_SIZE, stringList, frameBytesDelimiter);
        DebugPanel.getInstance().sendMessage("FCS Highlighter", "Highlighting " + byteStartIndex + " through " + byteEndIndex);
        highlighter.addHighlight(byteStartIndex, byteEndIndex, HIGHLIGHT_PAINTER);
    }
}
