package com.patiun.comportcommunicator.bytecorrputer;

import com.patiun.comportcommunicator.entity.Binary;
import com.patiun.comportcommunicator.window.DebugPanel;

import java.util.List;
import java.util.Random;

public class FiftyPercentByteCorrupter implements ByteCorrupter {
    @Override
    public List<Byte> corruptByte(List<Byte> bytes) {
        return randomCorruption(bytes);
    }

    private List<Byte> randomCorruption(List<Byte> dataBytes) {
        Random random = new Random();
        if (random.nextBoolean()) {
            return corruptRandomBit(dataBytes);
        }
        return dataBytes;
    }

    private List<Byte> corruptRandomBit(List<Byte> dataBytes) {
        Binary dataBinary = Binary.ofBytes(dataBytes);
        int dataBitsNumber = dataBinary.length();
        int randomBitNumber = (int) (Math.random() * dataBitsNumber);
        Binary withReversedBit = dataBinary.reverseBit(randomBitNumber);
        DebugPanel.getInstance().sendMessage("Sender", "Corruption of bit " + randomBitNumber + " occurred!");
        return withReversedBit.toByteList();
    }

}
