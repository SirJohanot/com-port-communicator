package com.patiun.comportcommunicator.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Packet {

    public static final int DATA_BYTES_NUMBER = 23;

    public static final byte FLAG_BYTE = 'a' + DATA_BYTES_NUMBER;

    public static final int CONTROL_VALUE = 997;
    public static final int FCS_SIZE = 2;

    private final byte flag;
    private final byte destinationAddress;
    private final byte sourceAddress;
    private final List<Byte> data;
    private final List<Byte> fcs;

    public Packet(byte[] bytes) {
        this.flag = bytes[0];
        this.destinationAddress = bytes[1];
        this.sourceAddress = bytes[2];
        data = new ArrayList<>();
        int i = 3;
        for (; i < bytes.length - FCS_SIZE; i++) {
            data.add(bytes[i]);
        }
        fcs = new ArrayList<>();
        for (; i < bytes.length; i++) {
            fcs.add(bytes[i]);
        }
    }

    public Packet(byte sourceAddress, List<Byte> data, List<Byte> fcs) {
        this.flag = FLAG_BYTE;
        this.destinationAddress = 0;
        this.sourceAddress = sourceAddress;
        this.data = new ArrayList<>(data);
        this.fcs = fcs;
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

    public List<Byte> getFcs() {
        return fcs;
    }

    public byte[] toBytes() {
        byte[] bytes = new byte[data.size() + FCS_SIZE + 3];
        bytes[0] = flag;
        bytes[1] = destinationAddress;
        bytes[2] = sourceAddress;
        int i = 3;
        for (byte datum : data) {
            bytes[i] = datum;
            i++;
        }
        for (byte datum : fcs) {
            bytes[i] = datum;
            i++;
        }
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
        if (!Objects.equals(data, packet.data)) {
            return false;
        }
        return Objects.equals(fcs, packet.fcs);
    }

    @Override
    public int hashCode() {
        int result = flag;
        result = 31 * result + (int) destinationAddress;
        result = 31 * result + (int) sourceAddress;
        result = 31 * result + (data != null ? data.hashCode() : 0);
        result = 31 * result + (fcs != null ? fcs.hashCode() : 0);
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
