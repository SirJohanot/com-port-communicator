package com.patiun.comportcommunicator.crc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.patiun.comportcommunicator.entity.Packet.CONTROL_VALUE;
import static com.patiun.comportcommunicator.entity.Packet.FCS_SIZE;

public class CrcEncoder {

    private static final List<Boolean> CONTROL_VALUE_BINARY = toBinary(CONTROL_VALUE);

    public Byte calculateFcs(List<Byte> dataBytes) {
        List<Boolean> combinedDataBinary = new ArrayList<>();
        for (Byte datum : dataBytes) {
            combinedDataBinary.addAll(toBinary(datum));
        }
        combinedDataBinary.addAll(Collections.nCopies(FCS_SIZE, false));
        while (true) {
            snipZeros(combinedDataBinary);
            int subListBeginIndex = 0;
            int subListEndIndex = 0;
            List<Boolean> combinedDataSubList = combinedDataBinary.subList(subListBeginIndex, subListEndIndex);
            while (compare(combinedDataSubList, CONTROL_VALUE_BINARY) == -1 && subListEndIndex < combinedDataBinary.size()) {
                subListEndIndex++;
                combinedDataSubList = combinedDataBinary.subList(subListBeginIndex, subListEndIndex);
            }
            if (subListEndIndex == combinedDataBinary.size()) {
                int decimalValue = toValue(combinedDataBinary);
                if (decimalValue > Byte.MAX_VALUE) {
                    decimalValue += Byte.MIN_VALUE;
                }
                return (byte) decimalValue;
            }
        }
    }

    private static List<Boolean> toBinary(int value) {
        List<Boolean> result = new ArrayList<>();
        while (value > 0) {
            result.add(value % 2 == 1);
            value /= 2;
        }
        Collections.reverse(result);
        return result;
    }

    private static int toValue(List<Boolean> binary) {
        int power = 0;
        int result = 0;
        for (int i = binary.size() - 1; i >= 0; i--) {
            Boolean binaryElement = binary.get(i);
            if (binaryElement) {
                result += Math.pow(2, power);
            }
            power++;
        }
        return result;
    }

    private void snipZeros(List<Boolean> value) {
        while (!value.get(0)) {
            value.remove(0);
        }
    }

    private int compare(List<Boolean> firstBinary, List<Boolean> secondBinary) {
        int firstBinarySize = firstBinary.size();
        int secondBinarySize = secondBinary.size();
        if (firstBinarySize != secondBinarySize) {
            return firstBinarySize > secondBinarySize ? 1 : -1;
        }
        for (int elementIndex = 0; elementIndex < firstBinarySize; elementIndex++) {
            Boolean firstBinaryElement = firstBinary.get(elementIndex);
            Boolean secondBinaryElement = secondBinary.get(elementIndex);
            if (firstBinaryElement && !secondBinaryElement) {
                return 1;
            } else if (!firstBinaryElement && secondBinaryElement) {
                return -1;
            }
        }
        return 0;
    }

    private List<Boolean> subtract(List<Boolean> minuend, List<Boolean> subtrahend) {
        List<Boolean> result = new ArrayList<>(minuend);
        int minuendSize = minuend.size();
        int subtrahendSize = subtrahend.size();
        int sizeDifference = minuendSize - subtrahendSize;
        for (int subtrahendIndex = subtrahendSize - 1; subtrahendIndex >= 0; subtrahendIndex--) {
            int minuendIndex = subtrahendIndex + sizeDifference;
            Boolean minuendElement = result.get(minuendIndex);
            Boolean subtrahendElement = subtrahend.get(subtrahendIndex);
            if (minuendElement == subtrahendElement) {
                result.set(subtrahendIndex, false);
            } else if (subtrahendElement) {
                borrow(result, subtrahendIndex);
            }
        }
        return result;
    }

    private void borrow(List<Boolean> minuend, int takerIndex) {
        int loserIndex = takerIndex - 1;
        Boolean loserElement = minuend.get(loserIndex);
        if (loserElement) {
            minuend.set(loserIndex, false);
            minuend.set(takerIndex, true);
        } else {
            borrow(minuend, loserIndex);
        }
    }
}
