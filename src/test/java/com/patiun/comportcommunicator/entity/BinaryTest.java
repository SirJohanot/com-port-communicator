package com.patiun.comportcommunicator.entity;

import org.junit.Assert;
import org.junit.Test;

public class BinaryTest {

    @Test
    public void testModShouldReturnTheCorrectMod() {
        //given
        Binary firstBinary = Binary.ofInt(2512);
        Binary secondBinary = Binary.ofInt(25);
        Binary expectedMod = Binary.ofInt(2);
        //when
        Binary actualMod = firstBinary.mod(secondBinary);
        //then
        Assert.assertEquals(expectedMod, actualMod);
    }
}
