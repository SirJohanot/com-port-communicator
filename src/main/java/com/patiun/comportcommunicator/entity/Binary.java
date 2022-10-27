package com.patiun.comportcommunicator.entity;

import com.patiun.comportcommunicator.window.DebugPanel;

import java.util.*;
import java.util.function.UnaryOperator;

public class Binary {

    private static final int BYTE_SIZE = 8;

    private final List<Boolean> bits;

    public Binary() {
        bits = new ArrayList<>();
    }

    private Binary(List<Boolean> bits) {
        this.bits = bits;
    }

    public static Binary of(int value) {
        List<Boolean> result = new ArrayList<>();
        while (value > 0) {
            result.add(0, value % 2 == 1);
            value /= 2;
        }
        return new Binary(result);
    }

    public static Binary of(List<Byte> bytes) {
        List<Boolean> bits = new ArrayList<>();
        for (Byte b : bytes) {
            Binary byteBinary = of(Byte.toUnsignedInt(b));
            List<Boolean> byteBits = byteBinary.bits;
            int missingZerosNumber = BYTE_SIZE - byteBits.size();
            List<Boolean> additionalZeros = Collections.nCopies(missingZerosNumber, false);
            byteBits.addAll(0, additionalZeros);
            bits.addAll(byteBits);
        }
        return new Binary(bits);
    }

    public List<Boolean> getBits() {
        return bits;
    }

    public int toInt() {
        int power = 0;
        int result = 0;
        for (int i = bits.size() - 1; i >= 0; i--) {
            Boolean binaryElement = bits.get(i);
            if (binaryElement) {
                result += Math.pow(2, power);
            }
            power++;
        }
        return result;
    }

    public byte toByte() {
        int intResult = toInt();
        if (intResult > Byte.MAX_VALUE) {
            intResult += Byte.MIN_VALUE;
        }
        return (byte) intResult;
    }

    public List<Byte> toByteList() {
//        //DebugPanel.getInstance().sendMessage("Binary Operation", "Began conversion of " + bits.size() + " bits to bytes");
        List<Byte> result = new ArrayList<>();
        for (int i = size(); i >= 0; i -= BYTE_SIZE) {
            int subListBeginIndex = i - BYTE_SIZE;
            if (subListBeginIndex < 0) {
                subListBeginIndex = 0;
            }
            Binary byteBitsBinary = new Binary(subList(subListBeginIndex, i));
            Byte byteOfBinary = byteBitsBinary.toByte();
//            //DebugPanel.getInstance().sendMessage("Binary Operation", "Adding " + byteOfBinary + " to list");
            result.add(0, byteOfBinary);
            if (subListBeginIndex == 0) {
                break;
            }
        }
        return result;
    }

    public Binary mod(Binary anotherBinary) {
        DebugPanel.getInstance().sendMessage("Binary Operation", "Mod " + this + " and " + anotherBinary);
        Binary copy = copy();
        while (true) {
            copy.snipZeros();
            int subListBeginIndex = 0;
            int subListEndIndex = 1;
            Binary copySubList = new Binary(copy.subList(subListBeginIndex, subListEndIndex));
            int copySize = copy.size();
            while (copySubList.compareSizes(anotherBinary) != 0 && subListEndIndex < copySize) {
                subListEndIndex++;
                copySubList = new Binary(copy.subList(subListBeginIndex, subListEndIndex));
            }
            if (copySubList.compareSizes(anotherBinary) != 0) {
                break;
            }
            Binary subtractionResult = copySubList.xor(anotherBinary);
            for (int i = 0; i < subListEndIndex; i++) {
                copy.remove(0);
            }
            copy.addAll(0, subtractionResult.bits);
        }
        DebugPanel.getInstance().sendMessage("Binary Operation", "Calculated mod is " + copy);
        return copy;
    }

    public int compareSizes(Binary anotherBinary) {
//        DebugPanel.getInstance().sendMessage("Binary Operation", "Comparing " + this + " to " + anotherBinary);
        int firstBinarySize = bits.size();
        int secondBinarySize = anotherBinary.size();
        if (firstBinarySize != secondBinarySize) {
//            DebugPanel.getInstance().sendMessage("Binary Operation", "Unequal sizes");
            return firstBinarySize > secondBinarySize ? 1 : -1;
        }
        return 0;
    }

    public Binary xor(Binary anotherBinary) {
//        DebugPanel.getInstance().sendMessage("Binary Operation", "Xor " + this + " and " + anotherBinary);
        Binary result = copy();
        int minuendSize = bits.size();
        int subtrahendSize = anotherBinary.size();
        int sizeDifference = minuendSize - subtrahendSize;
        for (int subtrahendIndex = subtrahendSize - 1; subtrahendIndex >= 0; subtrahendIndex--) {
            int minuendIndex = subtrahendIndex + sizeDifference;
            Boolean minuendElement = result.get(minuendIndex);
            Boolean subtrahendElement = anotherBinary.get(subtrahendIndex);
            result.set(minuendIndex, minuendElement != subtrahendElement);
        }
        result.snipZeros();
        return result;
    }

    public void snipZeros() {
        while (!bits.get(0) && bits.size() > 1) {
            bits.remove(0);
        }
    }

//    public int compare(Binary anotherBinary) {
////        //DebugPanel.getInstance().sendMessage("Binary Operation", "Comparing " + this + " to " + anotherBinary);
//        int firstBinarySize = bits.size();
//        int secondBinarySize = anotherBinary.size();
//        if (firstBinarySize != secondBinarySize) {
////            //DebugPanel.getInstance().sendMessage("Binary Operation", "Unequal sizes");
//            return firstBinarySize > secondBinarySize ? 1 : -1;
//        }
//        for (int elementIndex = 0; elementIndex < firstBinarySize; elementIndex++) {
//            Boolean firstBinaryElement = bits.get(elementIndex);
//            Boolean secondBinaryElement = anotherBinary.get(elementIndex);
//            if (firstBinaryElement && !secondBinaryElement) {
////                //DebugPanel.getInstance().sendMessage("Binary Operation", "Greater");
//                return 1;
//            } else if (!firstBinaryElement && secondBinaryElement) {
////                //DebugPanel.getInstance().sendMessage("Binary Operation", "Lesser");
//                return -1;
//            }
//        }
////        //DebugPanel.getInstance().sendMessage("Binary Operation", "Equal");
//        return 0;
//    }

//    public Binary subtract(Binary subtrahend) {
//        //DebugPanel.getInstance().sendMessage("Binary Operation", "Subtracting " + subtrahend + " from " + this);
//        Binary result = copy();
//        int minuendSize = bits.size();
//        int subtrahendSize = subtrahend.size();
//        int sizeDifference = minuendSize - subtrahendSize;
//        for (int subtrahendIndex = subtrahendSize - 1; subtrahendIndex >= 0; subtrahendIndex--) {
//            int minuendIndex = subtrahendIndex + sizeDifference;
//            Boolean minuendElement = result.get(minuendIndex);
//            Boolean subtrahendElement = subtrahend.get(subtrahendIndex);
////            //DebugPanel.getInstance().sendMessage("Binary Operation", "Subtracting subtrahend element " + (subtrahendElement ? "(1)" : "(0)") + " (index=" + subtrahendIndex + ") from minuend element " + (minuendElement ? "(1)" : "(0)") + " (index=" + minuendIndex + ")");
//            if (minuendElement == subtrahendElement) {
////                //DebugPanel.getInstance().sendMessage("Binary Operation", "The elements are equal, result is 0");
//                result.set(minuendIndex, false);
//            } else if (subtrahendElement) {
////                //DebugPanel.getInstance().sendMessage("Binary Operation", "Borrowing for " + minuendIndex + " element of minuend");
//                result.borrow(minuendIndex);
//            }
////            //DebugPanel.getInstance().sendMessage("Binary Operation", "Now minuend is: " + result);
//        }
//        result.snipZeros();
//        return result;
//    }

//    private void borrow(int takerIndex) {
//        int loserIndex = takerIndex - 1;
//        Boolean loserElement = get(loserIndex);
//        if (loserElement) {
//            //DebugPanel.getInstance().sendMessage("Binary Operation", "Loser element is 1. Setting it to 0 and borrowing for lesser element");
//            set(loserIndex, false);
//        } else {
//            //DebugPanel.getInstance().sendMessage("Binary Operation", "Loser element is 0. Borrowing for greater element");
//            borrow(loserIndex);
//        }
//        set(takerIndex, true);
//    }

//    public Binary add(Binary addition) {
//        boolean carry = false;
//        Binary result = new Binary();
//        int selfSize = size();
//        int additionSize = addition.size();
//        int sizeDifference = selfSize - additionSize;
//        for (int additionIndex = additionSize - 1; additionIndex >= 0; additionIndex--) {
//            int selfIndex = additionIndex + sizeDifference;
//            if (selfIndex < 0) {
//                break;
//            }
//            Boolean selfElement = result.get(selfIndex);
//            Boolean additionElement = addition.get(additionIndex);
//            boolean additionResult = solveAddition(selfElement, additionElement, carry);
//            carry = solveCarry(selfElement, additionElement, carry);
//            result.add(0, additionResult);
//        }
//        result.add(carry);
//        result.snipZeros();
//        return result;
//    }
//
//    private boolean solveAddition(boolean firstElement, boolean secondElement, boolean carry) {
//        if (carry) {
//            return firstElement == secondElement;
//        }
//        return (firstElement != secondElement);
//    }
//
//    private boolean solveCarry(boolean firstElement, boolean secondElement, boolean carry) {
//        if (carry) {
//            return firstElement || secondElement;
//        }
//        return firstElement && secondElement;
//    }

    public Binary copy() {
        return new Binary(new ArrayList<>(bits));
    }

    public int size() {
        return bits.size();
    }

    public boolean isEmpty() {
        return bits.isEmpty();
    }

    public boolean contains(Object o) {
        return bits.contains(o);
    }

    public Iterator<Boolean> iterator() {
        return bits.iterator();
    }

    public Object[] toArray() {
        return bits.toArray();
    }

    public <T> T[] toArray(T[] a) {
        return bits.toArray(a);
    }

    public boolean add(Boolean aBoolean) {
        return bits.add(aBoolean);
    }

    public boolean remove(Object o) {
        return bits.remove(o);
    }

    public boolean containsAll(Collection<?> c) {
        return bits.containsAll(c);
    }

    public boolean addAll(Collection<? extends Boolean> c) {
        return bits.addAll(c);
    }

    public boolean addAll(int index, Collection<? extends Boolean> c) {
        return bits.addAll(index, c);
    }

    public boolean removeAll(Collection<?> c) {
        return bits.removeAll(c);
    }

    public boolean retainAll(Collection<?> c) {
        return bits.retainAll(c);
    }

    public void replaceAll(UnaryOperator<Boolean> operator) {
        bits.replaceAll(operator);
    }

    public void sort(Comparator<? super Boolean> c) {
        bits.sort(c);
    }

    public void clear() {
        bits.clear();
    }

    public Boolean get(int index) {
        return bits.get(index);
    }

    public Boolean set(int index, Boolean element) {
        return bits.set(index, element);
    }

    public void add(int index, Boolean element) {
        bits.add(index, element);
    }

    public Boolean remove(int index) {
        return bits.remove(index);
    }

    public int indexOf(Object o) {
        return bits.indexOf(o);
    }

    public int lastIndexOf(Object o) {
        return bits.lastIndexOf(o);
    }

    public ListIterator<Boolean> listIterator() {
        return bits.listIterator();
    }

    public ListIterator<Boolean> listIterator(int index) {
        return bits.listIterator(index);
    }

    public List<Boolean> subList(int fromIndex, int toIndex) {
        return bits.subList(fromIndex, toIndex);
    }

    public Spliterator<Boolean> spliterator() {
        return bits.spliterator();
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
        StringBuilder builder = new StringBuilder("Binary {");
        bits.stream()
                .map(b -> {
                    if (b) {
                        return "1";
                    }
                    return "0";
                })
                .forEach(builder::append);
        builder.append("}");
        return builder.toString();
    }
}
