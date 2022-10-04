package com.patiun.comportcommunicator.util;

import java.util.ArrayList;
import java.util.List;

public class ByteStringFormatter {

    public static List<String> byteListToHexStringList(List<Byte> bytes) {
        List<String> stringList = new ArrayList<>();
        for (byte frameDatum : bytes) {
            stringList.add(String.format("%02X", frameDatum));
        }
        return stringList;
    }

    public static List<Byte> hexStringListToByteList(List<String> hexString) {
        List<Byte> bytes = new ArrayList<>();
        for (String s : hexString) {
            byte hexByte = Byte.parseByte(s, 16);
            bytes.add(hexByte);
        }
        return bytes;
    }
}
