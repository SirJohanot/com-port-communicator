package com.patiun.comportcommunicator.highlighter;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.*;

public interface BytesHighlighter {

    Highlighter.HighlightPainter HIGHLIGHT_PAINTER = new DefaultHighlighter.DefaultHighlightPainter(Color.RED);

    void highlightBytes(JTextArea textArea, String textAreaBytesDelimiterRegex, String frameBytesDelimiter) throws BadLocationException;
}
