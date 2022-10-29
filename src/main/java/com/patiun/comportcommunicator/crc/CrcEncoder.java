package com.patiun.comportcommunicator.crc;

import com.patiun.comportcommunicator.entity.Binary;
import com.patiun.comportcommunicator.window.DebugPanel;

import java.util.ArrayList;
import java.util.List;

import static com.patiun.comportcommunicator.entity.Packet.CONTROL_VALUE;
import static com.patiun.comportcommunicator.entity.Packet.FCS_SIZE;

public class CrcEncoder {

    private static final Binary CONTROL_VALUE_BINARY = Binary.ofInt(CONTROL_VALUE);

    static {
        DebugPanel.getInstance().sendMessage("CRC", "Control value is " + CONTROL_VALUE_BINARY);
    }

    public static List<Byte> calculateFcs(List<Byte> frameDataBytes) {
        List<Byte> fcs = new ArrayList<>();
        for (int i = 0; i < frameDataBytes.size(); i += 2) {
            int subListEndIndex = (i < frameDataBytes.size() - 1) ? (i + 2) : i + 1;
            List<Byte> bytesSubList = frameDataBytes.subList(i, subListEndIndex);
            Binary frameBinary = Binary.ofBytes(bytesSubList);
            Binary additionalZerosBinary = Binary.ofByte((byte) 0);
            Binary combinedDataBinary = frameBinary.combine(additionalZerosBinary);
            DebugPanel.getInstance().sendMessage("CRC", "Calculating FCS for " + frameBinary);
            Binary mod = combinedDataBinary.mod(CONTROL_VALUE_BINARY);
            DebugPanel.getInstance().sendMessage("CRC", "Calculated fcs binary is " + mod);
            byte modByte = (byte) mod.toInt();
            DebugPanel.getInstance().sendMessage("CRC", "Calculated fcs is " + modByte);
            fcs.add(modByte);
        }
        while (fcs.size() < FCS_SIZE) {
            fcs.add(Byte.MIN_VALUE);
        }
        return fcs;
    }

    public static Boolean isCorrupted(List<Byte> frameDataBytes, List<Byte> fcs) {
        int fcsIndex = 0;
        for (int i = 0; i < frameDataBytes.size(); i += 2, fcsIndex++) {
            int subListEndIndex = (i < frameDataBytes.size() - 1) ? (i + 2) : i + 1;
            List<Byte> bytesSubList = frameDataBytes.subList(i, subListEndIndex);
            Binary frameDataBinary = Binary.ofBytes(bytesSubList);

            Byte currentFcs = fcs.get(fcsIndex);
            Binary fcsBinary = Binary.ofByte(currentFcs);

            Binary combinedDataBinary = frameDataBinary.combine(fcsBinary);
            Binary mod = combinedDataBinary.mod(CONTROL_VALUE_BINARY);
            byte modByte = (byte) mod.toInt();
            if (modByte != 0) {
                DebugPanel.getInstance().sendMessage("CRC", "Found corruption in bytes " + i + " to " + subListEndIndex + ". Binary is " + frameDataBinary + " and fcs " + fcsBinary);
                return true;
            }
        }
        return false;
    }

    public static List<Byte> restoreData(List<Byte> frameDataBytes, List<Byte> fcs) {
        int fcsIndex = 0;
        List<Byte> restoredData = new ArrayList<>();
        for (int i = 0; i < frameDataBytes.size(); i += 2, fcsIndex++) {
            int subListEndIndex = (i < frameDataBytes.size() - 1) ? (i + 2) : i + 1;
            List<Byte> bytesSubList = frameDataBytes.subList(i, subListEndIndex);
            Binary dataBytesBinary = Binary.ofBytes(bytesSubList);
            int dataBitsNumber = dataBytesBinary.length();

            Byte currentFcs = fcs.get(fcsIndex);
            List<Byte> currentFcsList = List.of(currentFcs);

            if (!isCorrupted(bytesSubList, currentFcsList)) {
                restoredData.addAll(bytesSubList);
                continue;
            }

            for (int j = 0; j < dataBitsNumber; j++) {
                Binary dataBytesBinaryCopy = dataBytesBinary.copy();
                dataBytesBinaryCopy = dataBytesBinaryCopy.reverseBit(j);

                if (!isCorrupted(dataBytesBinaryCopy.toByteList(), currentFcsList)) {
                    DebugPanel.getInstance().sendMessage("CRC", "Restored bit " + (i * 8 + j) + " of the frame");
                    DebugPanel.getInstance().sendMessage("CRC", "Restored data: " + dataBytesBinaryCopy + " with FCS " + currentFcs);
                    restoredData.addAll(dataBytesBinaryCopy.toByteList());
                    break;
                }
            }
        }
        return restoredData;
    }

}
