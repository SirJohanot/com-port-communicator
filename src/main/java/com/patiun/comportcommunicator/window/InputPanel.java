package com.patiun.comportcommunicator.window;

import com.fazecast.jSerialComm.SerialPort;
import com.patiun.comportcommunicator.config.AcceptedCharactersRegex;
import com.patiun.comportcommunicator.factory.ComponentFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.regex.Pattern;

public class InputPanel extends JPanel {

    private final SerialPort inputPort;
    private final StatsPanel statsPanel;

    private final JLabel name;

    public InputPanel(SerialPort inputPort, StatsPanel statsPanel) throws HeadlessException {
        super();
        ComponentFactory.getInstance().setUpPanel(this);
        setLayout(new BorderLayout());

        this.inputPort = inputPort;
        ControlPanel.getInstance().registerPort(inputPort);

        this.statsPanel = statsPanel;

        name = ComponentFactory.getInstance().buildLabel(inputPort.getSystemPortName() + " - Input");
        add(name, BorderLayout.PAGE_START);

        add(setUpInput(), BorderLayout.CENTER);
    }

    private JScrollPane setUpInput() {
        JTextArea inputTextArea = ComponentFactory.getInstance().buildTextArea(true);
        inputTextArea.getInputMap().put(KeyStroke.getKeyStroke("control V"), "none");
        inputTextArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (!Pattern.matches(AcceptedCharactersRegex.REGEX, String.valueOf(e.getKeyChar()))) {
                    e.consume();
                    return;
                }
                inputTextArea.setCaretPosition(inputTextArea.getDocument().getLength());
                char keyChar = e.getKeyChar();
                DebugPanel.getInstance().sendMessage(name.getText(), "Sending " + keyChar + " in " + inputPort.getNumDataBits() + " data bits");
                int bytesWritten = inputPort.writeBytes(new byte[]{(byte) keyChar}, 1);
                DebugPanel.getInstance().sendMessage(name.getText(), "Sent " + bytesWritten + " bytes");
                statsPanel.incrementBytesTransferred(inputPort.getSystemPortName(), bytesWritten);
            }
        });

        return ComponentFactory.getInstance().buildScrollPane(inputTextArea);
    }

}
