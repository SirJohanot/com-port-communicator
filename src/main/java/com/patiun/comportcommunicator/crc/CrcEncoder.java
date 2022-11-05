package com.patiun.comportcommunicator.crc;

import com.patiun.comportcommunicator.entity.Binary;
import com.patiun.comportcommunicator.window.DebugPanel;

import java.util.List;

import static com.patiun.comportcommunicator.entity.Packet.CONTROL_VALUE;
import static com.patiun.comportcommunicator.entity.Packet.FCS_SIZE;

public class CrcEncoder {

    private static final Binary CONTROL_VALUE_BINARY = Binary.ofInt(CONTROL_VALUE);

    static {
        DebugPanel.getInstance().sendMessage("CRC", "Control value is " + CONTROL_VALUE_BINARY);
    }

    public static List<Byte> calculateFcs(List<Byte> frameDataBytes) {
        Binary frameBinary = Binary.ofBytes(frameDataBytes);
        Binary combinedDataBinary = frameBinary.copy();
        for (int i = 0; i < FCS_SIZE; i++) {
            Binary additionalZerosBinary = Binary.ofByte((byte) 0);
            combinedDataBinary = combinedDataBinary.combine(additionalZerosBinary);
        }
        DebugPanel.getInstance().sendMessage("CRC", "Calculating FCS for " + frameBinary);
        Binary mod = combinedDataBinary.mod(CONTROL_VALUE_BINARY);
        DebugPanel.getInstance().sendMessage("CRC", "Calculated fcs binary is " + mod);
        int modValue = mod.toInt();
        DebugPanel.getInstance().sendMessage("CRC", "Calculated fcs is " + modValue);
        return mod.toByteList();
    }

    public static Boolean isCorrupted(List<Byte> frameDataBytes, List<Byte> fcs) {
        Binary frameDataBinary = Binary.ofBytes(frameDataBytes);

        Binary fcsBinary = Binary.ofBytes(fcs);

        Binary combinedDataBinary = frameDataBinary.combine(fcsBinary);
        Binary mod = combinedDataBinary.mod(CONTROL_VALUE_BINARY);
        int modValue = mod.toInt();
        if (modValue != 0) {
            DebugPanel.getInstance().sendMessage("CRC", "Found corruption in binary " + frameDataBinary + ". Fcs is " + fcsBinary);
            return true;
        }
        return false;
    }

    public static List<Byte> restoreData(List<Byte> frameDataBytes, List<Byte> fcs) {
        if (!isCorrupted(frameDataBytes, fcs)) {
            return frameDataBytes;
        }

        Binary dataBytesBinary = Binary.ofBytes(frameDataBytes);
        int dataBitsNumber = dataBytesBinary.length();

        for (int i = 0; i < dataBitsNumber; i++) {
            Binary dataBytesBinaryCopy = dataBytesBinary.copy();
            dataBytesBinaryCopy = dataBytesBinaryCopy.reverseBit(i);

            if (!isCorrupted(dataBytesBinaryCopy.toByteList(), fcs)) {
                DebugPanel.getInstance().sendMessage("CRC", "Restored bit " + i + " of the frame");
                return dataBytesBinaryCopy.toByteList();
            }
        }
        return frameDataBytes;
    }

}
