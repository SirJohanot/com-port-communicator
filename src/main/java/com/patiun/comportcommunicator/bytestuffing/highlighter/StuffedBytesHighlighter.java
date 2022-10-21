package com.patiun.comportcommunicator.bytestuffing.highlighter;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.*;

public interface StuffedBytesHighlighter {

    Highlighter.HighlightPainter HIGHLIGHT_PAINTER = new DefaultHighlighter.DefaultHighlightPainter(Color.RED);

    void highlightStuffedBytes(JTextArea textArea, String textAreaBytesDelimiterRegex, String frameBytesDelimiter, int stuffedBytesBeginningIndex, int stuffedBytesEndIndex) throws BadLocationException;
}
