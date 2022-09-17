package com.patiun.comportcommunicator.window;

import com.fazecast.jSerialComm.SerialPort;
import com.patiun.comportcommunicator.factory.ComponentFactory;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ControlPanel extends JPanel {

    private static final int INITIAL_DATA_BITS = 8;
    private static final int MIN_DATA_BITS = 5;
    private static final int MAX_DATA_BITS = 8;
    private static final int DATA_BITS_STEP = 1;

    private static final ControlPanel INSTANCE = new ControlPanel();

    private final List<SerialPort> ports = new ArrayList<>();

    private ControlPanel() {
        super();
        ComponentFactory.getInstance().setUpPanel(this);
        setLayout(new FlowLayout());

        add(ComponentFactory.getInstance().buildLabel("Change ports data bits (5 - 8): "));

        JSpinner spinner = ComponentFactory.getInstance().buildSpinner(new SpinnerNumberModel(INITIAL_DATA_BITS, MIN_DATA_BITS, MAX_DATA_BITS, DATA_BITS_STEP));
        spinner.addChangeListener(e -> {
            for (SerialPort serialPort : ports) {
                serialPort.closePort();
                serialPort.setNumDataBits((Integer) spinner.getValue());
                DebugPanel.getInstance().sendMessage("Control", "Set data bits of " + serialPort.getSystemPortName() + " to " + spinner.getValue() + ": " + ((Integer) spinner.getValue() == serialPort.getNumDataBits() ? "success" : "failure"));
                serialPort.openPort();
            }
        });

        add(spinner);
    }

    public void registerPort(SerialPort serialPort) {
        ports.add(serialPort);
    }

    public static ControlPanel getInstance() {
        return INSTANCE;
    }

}