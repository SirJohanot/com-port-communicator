package com.patiun.comportcommunicator.bytestuffing.highlighter;

import com.patiun.comportcommunicator.highlighter.AbstractChainedHighlighter;
import com.patiun.comportcommunicator.highlighter.BytesHighlighter;
import com.patiun.comportcommunicator.util.ByteStringFormatter;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Highlighter;
import java.util.Arrays;
import java.util.List;

import static com.patiun.comportcommunicator.bytestuffing.COBSByteStuffer.DELIMITER_BYTE;
import static com.patiun.comportcommunicator.bytestuffing.COBSByteStuffer.END_BYTE;

public class COBSStuffedBytesHighlighter extends AbstractChainedHighlighter {

    private final int stuffedBytesBeginningIndex;
    private final int stuffedBytesEndIndex;

    public COBSStuffedBytesHighlighter(int stuffedBytesBeginningIndex, int stuffedBytesEndIndex) {
        this.stuffedBytesBeginningIndex = stuffedBytesBeginningIndex;
        this.stuffedBytesEndIndex = stuffedBytesEndIndex;
    }

    public COBSStuffedBytesHighlighter(BytesHighlighter successor, int stuffedBytesBeginningIndex, int stuffedBytesEndIndex) {
        super(successor);
        this.stuffedBytesBeginningIndex = stuffedBytesBeginningIndex;
        this.stuffedBytesEndIndex = stuffedBytesEndIndex;
    }

    @Override
    protected void personalHighlightBytes(JTextArea textArea, int oldLength, String textAreaBytesDelimiterRegex, String frameBytesDelimiter) throws BadLocationException {
        String text = textArea.getText();
        List<String> stringList = Arrays.asList(text.split(textAreaBytesDelimiterRegex));
        List<Byte> bytes = ByteStringFormatter.stringListToByteList(stringList);
        Highlighter highlighter = textArea.getHighlighter();
        int i = oldLength + stuffedBytesBeginningIndex;
        while (i < oldLength + stuffedBytesEndIndex) {
            int byteStartIndex = charIndexOfByte(i, stringList, frameBytesDelimiter);
            int byteStringLength = stringList.get(i).length();
            highlighter.addHighlight(byteStartIndex, byteStartIndex + byteStringLength, HIGHLIGHT_PAINTER);
            int byteValue = Byte.toUnsignedInt(bytes.get(i));
            if (byteValue == END_BYTE) {
                break;
            }
//            DebugPanel.getInstance().sendMessage("Highlighting", "byteValue= " + byteValue + " DELIMITER= " + DELIMITER_BYTE);
            i += byteValue - DELIMITER_BYTE;
        }
    }

}
