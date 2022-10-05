package com.patiun.comportcommunicator.util;

import org.apache.commons.lang3.ArrayUtils;

import java.util.List;

public class ByteListCaster {

    public ByteListCaster() {
    }

    public static byte[] byteListToPrimitiveArray(List<Byte> byteList) {
        int byteListSize = byteList.size();
        Byte[] bytesArray = byteList.toArray(new Byte[byteListSize]);
        return ArrayUtils.toPrimitive(bytesArray);
    }
    
}
