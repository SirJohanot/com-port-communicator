package com.patiun.comportcommunicator.bytestuffing;

import java.util.List;

public interface ByteStuffer {

    List<Byte> stuffBytes(List<Byte> bytes);

    List<Byte> restoreBytes(List<Byte> stuffedBytes);
}
