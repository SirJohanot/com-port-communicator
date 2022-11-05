package com.patiun.comportcommunicator.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ByteStringFormatter {

    private static final String ASCII_CHARACTER_REGEX = "[ -~]";

    public static List<String> byteListToStringList(List<Byte> bytes) {
        List<String> stringList = new ArrayList<>();
        for (byte frameDatum : bytes) {
            String byteString = String.valueOf((char) frameDatum);
            if (Pattern.matches(ASCII_CHARACTER_REGEX, byteString)) {
                stringList.add(byteString);
            } else {
                int unsignedByteValue = Byte.toUnsignedInt(frameDatum);
                String byteValueString = "0" + unsignedByteValue;
                stringList.add(byteValueString);
            }
        }
        return stringList;
    }

    public static List<Byte> stringListToByteList(List<String> stringList) {
        List<Byte> bytes = new ArrayList<>();
        for (String s : stringList) {
            if (s.length() == 1) {
                bytes.add((byte) s.charAt(0));
            } else {
                bytes.add((byte) Integer.parseInt(s, 10));
            }
        }
        return bytes;
    }
}
