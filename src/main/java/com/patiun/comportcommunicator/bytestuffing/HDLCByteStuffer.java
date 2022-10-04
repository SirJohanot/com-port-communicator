package com.patiun.comportcommunicator.bytestuffing;

import java.util.ArrayList;
import java.util.List;

public class HDLCByteStuffer implements ByteStuffer {

    private final Byte flagByte;
    private final Byte escapeByte;

    public HDLCByteStuffer(Byte flagByte, Byte escapeByte) {
        this.flagByte = flagByte;
        this.escapeByte = escapeByte;
    }

    @Override
    public List<Byte> stuffBytes(List<Byte> bytes) {
        List<Byte> stuffedBytes = new ArrayList<>();
        for (Byte b : bytes) {
            if (b.equals(flagByte) || b.equals(escapeByte)) {
                stuffedBytes.add(escapeByte);
            }
            stuffedBytes.add(b);
        }
        return stuffedBytes;
    }

    @Override
    public List<Byte> emptyBytes(List<Byte> stuffedBytes) {
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
