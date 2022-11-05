package com.patiun.comportcommunicator.window;

import com.fazecast.jSerialComm.SerialPort;
import com.patiun.comportcommunicator.bytecorrputer.ByteCorrupter;
import com.patiun.comportcommunicator.bytecorrputer.FiftyPercentByteCorrupter;
import com.patiun.comportcommunicator.bytecorrputer.NoCorruptionByteCorrupter;
import com.patiun.comportcommunicator.bytestuffing.ByteStuffer;
import com.patiun.comportcommunicator.bytestuffing.highlighter.COBSStuffedBytesHighlighter;
import com.patiun.comportcommunicator.crc.CrcEncoder;
import com.patiun.comportcommunicator.entity.Packet;
import com.patiun.comportcommunicator.factory.ComponentFactory;
import com.patiun.comportcommunicator.highlighter.BytesHighlighter;
import com.patiun.comportcommunicator.highlighter.FcsHighlighter;
import org.apache.commons.lang3.ArrayUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class SenderPanel extends JPanel {

    private static final String ACCEPTED_CHARACTERS_REGEX = "[ -~]";
    private static final List<Character> forbiddenCharacters = Arrays.asList((char) KeyEvent.VK_BACK_SPACE, (char) KeyEvent.VK_DELETE);

    private final List<Byte> bufferedBytes = new ArrayList<>();
    private final SerialPort inputPort;

    private final ByteStuffer byteStuffer;

    private final StatsPanel statsPanel;

    private final JLabel name;

    public SenderPanel(SerialPort inputPort, ByteStuffer byteStuffer, StatsPanel statsPanel) throws HeadlessException {
        super();
        ComponentFactory.setUpPanel(this);
        setLayout(new BorderLayout());

        this.inputPort = inputPort;
        ControlPanel.getInstance().registerPort(inputPort);

        this.byteStuffer = byteStuffer;

        this.statsPanel = statsPanel;

        name = ComponentFactory.buildLabel(inputPort.getSystemPortName() + " - Sender");
        add(name, BorderLayout.PAGE_START);

        add(setUpInput(), BorderLayout.CENTER);
    }

    private JScrollPane setUpInput() {
        JTextArea inputTextArea = ComponentFactory.buildTextArea(true);
        inputTextArea.getInputMap().put(KeyStroke.getKeyStroke("control V"), "none");
        inputTextArea.getInputMap().put(KeyStroke.getKeyStroke("control Z"), "none");
        inputTextArea.getInputMap().put(KeyStroke.getKeyStroke("control X"), "none");
        inputTextArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (forbiddenCharacters.contains(e.getKeyChar())) {
                    e.consume();
                }
            }

            @Override
            public void keyTyped(KeyEvent e) {
                if (!Pattern.matches(ACCEPTED_CHARACTERS_REGEX, String.valueOf(e.getKeyChar()))) {
                    e.consume();
                    return;
                }
                inputTextArea.setCaretPosition(inputTextArea.getDocument().getLength());
                byte keyByte = (byte) e.getKeyChar();
                bufferedBytes.add(keyByte);
                if (bufferedBytes.size() == Packet.DATA_BYTES_NUMBER) {
                    sendPacket();
                }
            }
        });

        return ComponentFactory.buildScrollPane(inputTextArea);
    }

    private void sendPacket() {
        List<Byte> dataBytes;
        List<Byte> fcs;
        BytesHighlighter bytesHighlighter;
        ByteCorrupter byteCorrupter;

        if (bufferedBytes.contains(Packet.FLAG_BYTE)) {
            dataBytes = byteStuffer.stuffBytes(bufferedBytes);
            fcs = Collections.nCopies(Packet.FCS_SIZE, Byte.MIN_VALUE);
            bytesHighlighter = new COBSStuffedBytesHighlighter(new FcsHighlighter(dataBytes.size() + 3, Packet.FCS_SIZE), 3, dataBytes.size() + 3);
            byteCorrupter = new NoCorruptionByteCorrupter();
        } else {
            dataBytes = bufferedBytes;
            fcs = CrcEncoder.calculateFcs(bufferedBytes);
            bytesHighlighter = new FcsHighlighter(dataBytes.size() + 3, Packet.FCS_SIZE);
            byteCorrupter = new FiftyPercentByteCorrupter();
        }

        Packet packetToSend = formPacketAndUpdateFrame(dataBytes, fcs, bytesHighlighter, byteCorrupter);
        byte[] packetBytes = packetToSend.toBytes();
        DebugPanel.getInstance().sendMessage("Sender", "Sending " + new String(packetBytes));

        int bytesWritten = inputPort.writeBytes(packetBytes, packetBytes.length);
        DebugPanel.getInstance().sendMessage(name.getText(), "Sent " + bytesWritten + " bytes");
        bufferedBytes.clear();
    }

    private Packet formPacketAndUpdateFrame(List<Byte> dataBytes, List<Byte> fcsBytes, BytesHighlighter highlighter, ByteCorrupter corrupter) {
        List<Byte> randomlyCorruptedDataBytes = corrupter.corruptByte(dataBytes);
        Packet packetToSend = new Packet(getPortNumberByte(), randomlyCorruptedDataBytes, fcsBytes);

        byte[] packetBytes = packetToSend.toBytes();
        List<Byte> packetBytesList = Arrays.asList(ArrayUtils.toObject(packetBytes));
        statsPanel.updateFrame(packetBytesList, highlighter);
        
        return packetToSend;
    }

    private byte getPortNumberByte() {
        String portName = inputPort.getSystemPortName();
        String portNumber = portName.substring(3);
        return Byte.parseByte(portNumber);
    }

}
