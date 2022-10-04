package com.patiun.comportcommunicator.bytestuffing;

import java.util.List;

public interface ByteStuffer {

    public List<Byte> stuffBytes(List<Byte> bytes);

    public List<Byte> emptyBytes(List<Byte> stuffedBytes);
}
