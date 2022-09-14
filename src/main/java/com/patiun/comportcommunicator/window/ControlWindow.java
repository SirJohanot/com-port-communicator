package com.patiun.comportcommunicator.window;

import com.fazecast.jSerialComm.SerialPort;
import com.patiun.comportcommunicator.factory.ComponentFactory;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ControlWindow extends JFrame {

    private static final String WINDOW_NAME = "Control";

    private static final int INITIAL_DATA_BITS = 8;
    private static final int MIN_DATA_BITS = 5;
    private static final int MAX_DATA_BITS = 8;
    private static final int DATA_BITS_STEP = 1;

    private static final ControlWindow INSTANCE = new ControlWindow();

    private final List<SerialPort> ports = new ArrayList<>();

    private ControlWindow() {
        super();
        ComponentFactory.getInstance().setUpFrame(this, WINDOW_NAME);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLayout(new FlowLayout());

        add(ComponentFactory.getInstance().buildLabel("Change ports data bits (5 - 8): "));

        JSpinner spinner = ComponentFactory.getInstance().buildSpinner(new SpinnerNumberModel(INITIAL_DATA_BITS, MIN_DATA_BITS, MAX_DATA_BITS, DATA_BITS_STEP));
        spinner.addChangeListener(e -> {
            for (SerialPort serialPort : ports) {
                serialPort.closePort();
                serialPort.setNumDataBits((Integer) spinner.getValue());
                DebugWindow.getInstance().sendMessage(WINDOW_NAME, "Set data bits of " + serialPort.getDescriptivePortName() + " to " + spinner.getValue() + ": " + ((Integer) spinner.getValue() == serialPort.getNumDataBits() ? "success" : "failure"));
                serialPort.openPort();
            }
        });

        add(spinner);

        pack();
        setVisible(true);
    }

    public void registerPort(SerialPort serialPort) {
        ports.add(serialPort);
    }

    public static ControlWindow getInstance() {
        return INSTANCE;
    }

}
