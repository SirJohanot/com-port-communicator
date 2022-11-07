package com.patiun.comportcommunicator.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Binary {

    private static final int BYTE_SIZE = 8;

    private final String bits;

    private Binary(String bits) {
        this.bits = bits;
    }

    public static Binary ofInt(int value) {
        String binaryPresentation = Integer.toBinaryString(value);
        return new Binary(binaryPresentation);
    }

    public static Binary ofByte(byte value) {
        int unsignedValue = Byte.toUnsignedInt(value);
        Binary result = ofInt(unsignedValue);
        if (result.length() < BYTE_SIZE) {
            int missingZerosNumber = BYTE_SIZE - result.length();
            String additionalZeros = "0".repeat(missingZerosNumber);
            String resultBits = result.bits;
            result = new Binary(additionalZeros + resultBits);
        }
        return result;
    }

    public static Binary ofBytes(List<Byte> bytes) {
        StringBuilder bitsBuilder = new StringBuilder();
        for (Byte b : bytes) {
            Binary byteBinary = ofByte(b);
            bitsBuilder.append(byteBinary.bits);
        }
        String bits = bitsBuilder.toString();
        return new Binary(bits);
    }

    public String getBits() {
        return bits;
    }

    public int toInt() {
        return Integer.parseInt(bits, 2);
    }

    public List<Byte> toByteList() {
        List<Byte> result = new ArrayList<>();
        for (int i = length(); i >= 0; i -= BYTE_SIZE) {
            int subListBeginIndex = i - BYTE_SIZE;
            if (subListBeginIndex < 0) {
                subListBeginIndex = 0;
            }
            Binary byteBitsBinary = new Binary(substring(subListBeginIndex, i));
            Byte byteOfBinary = (byte) byteBitsBinary.toInt();
            result.add(0, byteOfBinary);
            if (subListBeginIndex == 0) {
                break;
            }
        }
        return result;
    }

    public Binary mod(Binary anotherBinary) {
        Binary copy = copy();
        while (true) {
            copy = copy.snipZeros();
            int subStringBeginIndex = 0;
            int subStringEndIndex = 1;
            Binary copySubString = new Binary(copy.substring(subStringBeginIndex, subStringEndIndex));
            int copySize = copy.length();
            while (copySubString.compareSizes(anotherBinary) != 0 && subStringEndIndex < copySize) {
                subStringEndIndex++;
                copySubString = new Binary(copy.substring(subStringBeginIndex, subStringEndIndex));
            }
            if (copySubString.compareSizes(anotherBinary) != 0) {
                break;
            }
            Binary subtractionResult = copySubString.xor(anotherBinary);
            copy = new Binary(subtractionResult.bits + copy.substring(subStringEndIndex));
        }
        return copy;
    }

    public int compareSizes(Binary anotherBinary) {
        int firstBinarySize = length();
        int secondBinarySize = anotherBinary.length();
        if (firstBinarySize != secondBinarySize) {
            return firstBinarySize > secondBinarySize ? 1 : -1;
        }
        return 0;
    }

    public Binary xor(Binary anotherBinary) {
        StringBuilder resultBuilder = new StringBuilder();
        for (int i = 0; i < length(); i++) {
            char selfElement = charAt(i);
            char anotherElement = anotherBinary.charAt(i);
            resultBuilder.append((selfElement != anotherElement) ? "1" : "0");
        }
        String resultBits = resultBuilder.toString();
        return new Binary(resultBits);
    }

    public Binary reverseBit(int index) {
        StringBuilder resultBitsBuilder = new StringBuilder(bits);
        char currentBitValue = resultBitsBuilder.charAt(index);
        resultBitsBuilder.setCharAt(index, (currentBitValue == '0') ? '1' : '0');
        String resultBits = resultBitsBuilder.toString();
        return new Binary(resultBits);
    }

    public Binary snipZeros() {
        int i = 0;
        for (; charAt(i) == '0' && i < length() - 1; i++) ;
        return new Binary(substring(i));
    }

    public Binary combine(Binary anotherBinary) {
        return new Binary(bits + anotherBinary.bits);
    }

    public Binary copy() {
        return new Binary(bits);
    }

    public int length() {
        return bits.length();
    }

    private char charAt(int index) {
        return bits.charAt(index);
    }

    private String substring(int beginIndex) {
        return bits.substring(beginIndex);
    }

    private String substring(int beginIndex, int endIndex) {
        return bits.substring(beginIndex, endIndex);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Binary binary = (Binary) o;

        return Objects.equals(bits, binary.bits);
    }

    @Override
    public int hashCode() {
        return bits != null ? bits.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Binary{" +
                "bits='" + bits + '\'' +
                '}';
    }
}
