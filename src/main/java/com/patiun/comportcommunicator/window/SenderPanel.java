package com.patiun.comportcommunicator.window;

import com.fazecast.jSerialComm.SerialPort;
import com.patiun.comportcommunicator.bytecorrputer.ByteCorrupter;
import com.patiun.comportcommunicator.bytecorrputer.FiftyPercentByteCorrupter;
import com.patiun.comportcommunicator.bytecorrputer.NoCorruptionByteCorrupter;
import com.patiun.comportcommunicator.bytestuffing.ByteStuffer;
import com.patiun.comportcommunicator.crc.CrcEncoder;
import com.patiun.comportcommunicator.entity.Packet;
import com.patiun.comportcommunicator.factory.ComponentFactory;
import com.patiun.comportcommunicator.highlighter.BytesHighlighter;
import com.patiun.comportcommunicator.highlighter.COBSStuffedBytesHighlighter;
import com.patiun.comportcommunicator.highlighter.FcsHighlighter;
import org.apache.commons.lang3.ArrayUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class SenderPanel extends JPanel {

    public static final byte JAM_SIGNAL = '$';
    private static final byte COLLISIONS_DELIMITER = ':';
    private static final byte COLLISION_CHARACTER = '*';
    private static final int MAX_SENDING_TRIES = 5;
    private static final int CHANNEL_BUSY_CHANCE_PERCENTAGE = 50;
    private static final int COLLISION_CHANCE_PERCENTAGE = 50;

    private static final String ACCEPTED_CHARACTERS_REGEX = "[ -~]";
    private static final List<Character> forbiddenCharacters = Arrays.asList((char) KeyEvent.VK_BACK_SPACE, (char) KeyEvent.VK_DELETE);

    private final JLabel name;
    private JTextArea inputTextArea;

    private final List<Byte> bufferedBytes = new ArrayList<>();
    private final SerialPort inputPort;

    private final StatsPanel statsPanel;
    private final ByteStuffer byteStuffer;

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
        inputTextArea = ComponentFactory.buildTextArea(true);
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

        Packet packetToSend = formPacket(dataBytes, fcs, byteCorrupter);
        byte[] packetBytes = packetToSend.toBytes();

        inputTextArea.setEditable(false);
        waitUntilChannelIsFree();
        int collisionTries = waitUntilNoCollision();
        if (collisionTries == MAX_SENDING_TRIES) {
            DebugPanel.getInstance().sendMessage("Sender", "Could not send the message because collisions could not be resolved after " + collisionTries + " tries");
        } else {
            List<Byte> bytesToDisplayOnStatusPanel = new ArrayList<>(Arrays.asList(ArrayUtils.toObject(packetBytes)));
            if (collisionTries > 0) {
                bytesToDisplayOnStatusPanel.add(COLLISIONS_DELIMITER);
                IntStream.range(0, collisionTries)
                        .forEach(t -> bytesToDisplayOnStatusPanel.add(COLLISION_CHARACTER));
            }
            statsPanel.updateFrame(bytesToDisplayOnStatusPanel, bytesHighlighter);

            DebugPanel.getInstance().sendMessage("Sender", "Sending " + new String(packetBytes));
            int bytesWritten = inputPort.writeBytes(packetBytes, packetBytes.length);
            DebugPanel.getInstance().sendMessage(name.getText(), "Sent " + bytesWritten + " bytes");
        }
        bufferedBytes.clear();
        inputTextArea.setEditable(true);
    }

    private Packet formPacket(List<Byte> dataBytes, List<Byte> fcsBytes, ByteCorrupter corrupter) {
        List<Byte> corruptedDataBytes = corrupter.corruptByte(dataBytes);
        return new Packet(getPortNumberByte(), corruptedDataBytes, fcsBytes);
    }

    private void waitUntilChannelIsFree() {
        while (randomPercentage() < CHANNEL_BUSY_CHANCE_PERCENTAGE) {
            DebugPanel.getInstance().sendMessage("Sender", "Cannot send the packet as the channel is busy, waiting");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private int waitUntilNoCollision() {
        int tries = 0;
        for (; randomPercentage() < COLLISION_CHANCE_PERCENTAGE && tries < MAX_SENDING_TRIES; tries++) {
            DebugPanel.getInstance().sendMessage("Sender", "A collision has been detected, sending jam-signal and waiting for resolution");
            inputPort.writeBytes(new byte[]{JAM_SIGNAL}, 1);
            waitForCollisionResolution(tries + 1);
        }
        return tries;
    }

    private int randomPercentage() {
        return new Random().nextInt(100);
    }

    private void waitForCollisionResolution(int currentTry) {
        int timeout = (int) Math.pow(3, currentTry);
        try {
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private byte getPortNumberByte() {
        String portName = inputPort.getSystemPortName();
        String portNumber = portName.substring(3);
        return Byte.parseByte(portNumber);
    }

}
