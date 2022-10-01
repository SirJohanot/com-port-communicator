package com.patiun.comportcommunicator.entity;

import java.util.ArrayList;
import java.util.List;

public class Packet {

    public static final int DATA_BYTES_NUMBER = 23;

    public static final byte FLAG_BYTE = 'a' + DATA_BYTES_NUMBER;
    public static final byte ESCAPE_BYTE = 'h';

    private final byte flag;
    private final byte destinationAddress;
    private final byte sourceAddress;
    private final List<Byte> data;
    private final byte fcs;

    public Packet(byte[] bytes) {
        this.flag = bytes[0];
        this.destinationAddress = bytes[1];
        this.sourceAddress = bytes[2];
        data = new ArrayList<>();
        for (int i = 3; i < bytes.length - 1; i++) {
            data.add(bytes[i]);
        }
        this.fcs = bytes[bytes.length - 1];
    }

    public Packet(byte sourceAddress, List<Byte> data) {
        this.flag = FLAG_BYTE;
        this.destinationAddress = 0;
        this.sourceAddress = sourceAddress;
        this.data = new ArrayList<>(data);
        this.fcs = 0;
    }

    public byte getFlag() {
        return flag;
    }

    public byte getDestinationAddress() {
        return destinationAddress;
    }

    public byte getSourceAddress() {
        return sourceAddress;
    }

    public List<Byte> getData() {
        return data;
    }

    public byte getFcs() {
        return fcs;
    }

    public byte[] toBytes() {
        byte[] bytes = new byte[data.size() + 4];
        bytes[0] = flag;
        bytes[1] = destinationAddress;
        bytes[2] = sourceAddress;
        int i = 3;
        for (byte datum : data) {
            bytes[i] = datum;
            i++;
        }
        bytes[i] = fcs;
        return bytes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Packet packet = (Packet) o;

        if (flag != packet.flag) {
            return false;
        }
        if (destinationAddress != packet.destinationAddress) {
            return false;
        }
        if (sourceAddress != packet.sourceAddress) {
            return false;
        }
        if (fcs != packet.fcs) {
            return false;
        }
        return data.equals(packet.data);
    }

    @Override
    public int hashCode() {
        int result = flag;
        result = 31 * result + (int) destinationAddress;
        result = 31 * result + (int) sourceAddress;
        result = 31 * result + data.hashCode();
        result = 31 * result + (int) fcs;
        return result;
    }

    @Override
    public String toString() {
        return "Packet{" +
                "flag=" + flag +
                ", destinationAddress=" + destinationAddress +
                ", sourceAddress=" + sourceAddress +
                ", data=" + data +
                ", fcs=" + fcs +
                '}';
    }
}
