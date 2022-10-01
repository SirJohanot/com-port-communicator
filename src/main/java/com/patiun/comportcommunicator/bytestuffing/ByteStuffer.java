package com.patiun.comportcommunicator.bytestuffing;

import java.util.ArrayList;
import java.util.List;

public class ByteStuffer {

    public ByteStuffer() {
    }

    public static List<Byte> stuffBytes(List<Byte> bytes, Byte flagByte, Byte escapeByte) {
        List<Byte> stuffedBytes = new ArrayList<>();
        for (Byte b : bytes) {
            if (b.equals(flagByte) || b.equals(escapeByte)) {
                stuffedBytes.add(escapeByte);
            }
            stuffedBytes.add(b);
        }
        return stuffedBytes;
    }

    public static List<Byte> emptyBytes(List<Byte> stuffedBytes, Byte escapeByte) {
        List<Byte> bytes = new ArrayList<>();
        boolean escapedByte = false;
        for (Byte b : stuffedBytes) {
            if (b.equals(escapeByte) && !escapedByte) {
                escapedByte = true;
                continue;
            }
            bytes.add(b);
            escapedByte = false;
        }
        return bytes;
    }
}
