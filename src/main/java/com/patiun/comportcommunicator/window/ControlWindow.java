package com.patiun.comportcommunicator.window;

import com.fazecast.jSerialComm.SerialPort;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static com.patiun.comportcommunicator.constant.ConstantValues.BACKGROUND_COLOR;
import static com.patiun.comportcommunicator.constant.ConstantValues.MAIN_COLOR;

public class ControlWindow extends JFrame {

    private static final ControlWindow INSTANCE = new ControlWindow();

    private final List<SerialPort> ports = new ArrayList<>();

    public ControlWindow() {
        super();
        setTitle("Control");
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        getContentPane().setBackground(BACKGROUND_COLOR);
        setBackground(BACKGROUND_COLOR);
        setUndecorated(false);
        setLayout(new FlowLayout());

        JLabel label = new JLabel("Change ports data bits (5 - 8): ");
        label.setOpaque(true);
        label.setSize(new Dimension(100, 100));
        label.setBackground(BACKGROUND_COLOR);
        label.setFont(new Font("Consolas", Font.BOLD, 20));
        label.setForeground(MAIN_COLOR);

        add(label);

        JSpinner spinner = new JSpinner(new SpinnerNumberModel(8, 5, 8, 1));
        spinner.setBackground(BACKGROUND_COLOR);
        spinner.setFont(new Font("Consolas", Font.BOLD, 20));
        spinner.setForeground(MAIN_COLOR);
        spinner.setMinimumSize(new Dimension(700, 400));
        spinner.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                for (SerialPort serialPort : ports) {
                    serialPort.setNumDataBits((Integer) spinner.getValue());
                }
            }
        });

        add(spinner);

        pack();
        setVisible(true);
    }

    public static ControlWindow getInstance() {
        return INSTANCE;
    }

    public void registerPort(SerialPort serialPort) {
        ports.add(serialPort);
    }
}
