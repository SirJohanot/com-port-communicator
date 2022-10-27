package com.patiun.comportcommunicator.crc;

import com.patiun.comportcommunicator.entity.Binary;
import com.patiun.comportcommunicator.window.DebugPanel;

import java.util.Collections;
import java.util.List;

import static com.patiun.comportcommunicator.entity.Packet.CONTROL_VALUE;
import static com.patiun.comportcommunicator.entity.Packet.FCS_SIZE;

public class CrcEncoder {

    private static final Binary CONTROL_VALUE_BINARY = Binary.of(CONTROL_VALUE);

    static {
        DebugPanel.getInstance().sendMessage("CRC", "Control value is " + CONTROL_VALUE_BINARY);
    }

    public static Byte calculateFcs(List<Byte> frameDataBytes) {
        Binary combinedDataBinary = Binary.of(frameDataBytes);
        combinedDataBinary.addAll(Collections.nCopies(FCS_SIZE, false));
        Binary mod = combinedDataBinary.mod(CONTROL_VALUE_BINARY);
        DebugPanel.getInstance().sendMessage("CRC", "Calculated fcs binary is " + mod);
        byte modByte = mod.toByte();
        DebugPanel.getInstance().sendMessage("CRC", "Calculated fcs is " + modByte);
        return modByte;
    }

    public static Boolean isCorrupted(List<Byte> frameDataBytes, Byte fcs) {
        DebugPanel.getInstance().sendMessage("CRC", "FCS= " + fcs);
        Binary combinedDataBinary = Binary.of(frameDataBytes);
        Binary fcsBinary = Binary.of(fcs - Byte.MIN_VALUE);
        combinedDataBinary.addAll(fcsBinary.getBits());
        DebugPanel.getInstance().sendMessage("CRC", "Analyzing DATA+FCS " + combinedDataBinary);
        Binary mod = combinedDataBinary.mod(CONTROL_VALUE_BINARY);
        byte modByte = mod.toByte();
        DebugPanel.getInstance().sendMessage("CRC", "Mod of DATA+FCS is " + modByte);
        return modByte != 0;
    }

    public static List<Byte> restoreData(List<Byte> frameDataBytes, Byte fcs) {
        Binary dataBytesBinary = Binary.of(frameDataBytes);
        List<Boolean> dataBits = dataBytesBinary.getBits();
        int dataBitsNumber = dataBits.size();

        for (int i = 0; i < dataBitsNumber; i++) {
            Binary dataBytesBinaryCopy = dataBytesBinary.copy();
            dataBytesBinaryCopy = reverseBit(dataBytesBinaryCopy, i);

            DebugPanel.getInstance().sendMessage("CRC", "Inspecting " + i + " bit of frame");
            if (!isCorrupted(dataBytesBinaryCopy.toByteList(), fcs)) {
                DebugPanel.getInstance().sendMessage("CRC", "Restored " + i + " bit of the frame");
                return dataBytesBinaryCopy.toByteList();
            }
        }
        return frameDataBytes;
    }

    private static Binary reverseBit(Binary source, int targetIndex) {
        Binary result = source.copy();
        Boolean targetBit = result.get(targetIndex);
        result.set(targetIndex, !targetBit);
        return result;
    }

}
