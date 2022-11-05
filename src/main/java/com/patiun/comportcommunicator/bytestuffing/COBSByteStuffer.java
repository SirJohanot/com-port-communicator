package com.patiun.comportcommunicator.bytestuffing;

import com.patiun.comportcommunicator.entity.Packet;

import java.util.ArrayList;
import java.util.List;

public class COBSByteStuffer implements ByteStuffer {

    public static final byte DELIMITER_BYTE = Packet.FLAG_BYTE;
    public static final byte END_BYTE = 0;

    public COBSByteStuffer() {
    }

    @Override
    public List<Byte> stuffBytes(List<Byte> bytes) {
        List<Byte> stuffedBytes = new ArrayList<>(bytes);
        stuffedBytes.add(0, DELIMITER_BYTE);
        stuffedBytes.add(END_BYTE);
        for (int i = 0; i < stuffedBytes.size() - 1; i++) {
            if (stuffedBytes.get(i) == DELIMITER_BYTE) {
                stuffedBytes.set(i, (byte) (DELIMITER_BYTE + offsetUntilDelimiterByte(stuffedBytes, i)));
            }
        }
        return stuffedBytes;
    }

    @Override
    public List<Byte> restoreBytes(List<Byte> stuffedBytes) {
        List<Byte> bytes = new ArrayList<>();
        int bytesUntilDelimiter = Byte.toUnsignedInt(stuffedBytes.get(0)) - DELIMITER_BYTE - 1;
        stuffedBytes.remove(0);
        for (Byte b : stuffedBytes) {
            if (b == END_BYTE) {
                break;
            }
            if (bytesUntilDelimiter == 0) {
                bytesUntilDelimiter = Byte.toUnsignedInt(b) - DELIMITER_BYTE;
                bytes.add(DELIMITER_BYTE);
            } else {
                bytes.add(b);
            }
            bytesUntilDelimiter--;
        }
        return bytes;
    }

    private byte offsetUntilDelimiterByte(List<Byte> bytes, int elementIndex) {
        byte offset = 1;
        for (int i = elementIndex + 1; bytes.get(i) != DELIMITER_BYTE && bytes.get(i) != END_BYTE; i++) {
            offset++;
        }
        return offset;
    }
}
