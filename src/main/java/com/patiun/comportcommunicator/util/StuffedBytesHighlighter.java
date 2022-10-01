package com.patiun.comportcommunicator.util;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.*;

public class StuffedBytesHighlighter {

    private static final Highlighter.HighlightPainter HIGHLIGHT_PAINTER = new DefaultHighlighter.DefaultHighlightPainter(Color.RED);

    public StuffedBytesHighlighter() {
    }

    public void highlightStuffedBytes(JTextArea textArea, byte escapeByte) throws BadLocationException {
        String text = textArea.getText();
        Highlighter highlighter = textArea.getHighlighter();
        boolean escapedChar = false;
        for (int i = 0; i < text.length(); i++) {
            if (!escapedChar && text.charAt(i) == (char) escapeByte) {
                highlighter.addHighlight(i, i + 1, HIGHLIGHT_PAINTER);
                escapedChar = true;
            } else {
                escapedChar = false;
            }
        }
    }
}
