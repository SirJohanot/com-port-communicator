package com.patiun.comportcommunicator.bytecorrputer;

import java.util.List;

public class NoCorruptionByteCorrupter implements ByteCorrupter {
    @Override
    public List<Byte> corruptByte(List<Byte> bytes) {
        return bytes;
    }
}
