package com.patiun.comportcommunicator.entity;

import org.junit.Assert;
import org.junit.Test;

public class BinaryTest {

    @Test
    public void testModShouldReturnTheCorrectMod() {
        //given
        Binary firstBinary = Binary.of(2512);
        Binary secondBinary = Binary.of(25);
        Binary expectedMod = Binary.of(2);
        //when
        Binary actualMod = firstBinary.mod(secondBinary);
        //then
        Assert.assertEquals(expectedMod, actualMod);
    }
}
