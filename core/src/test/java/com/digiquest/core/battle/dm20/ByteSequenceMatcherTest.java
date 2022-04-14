package com.digiquest.core.battle.dm20;

import com.digiquest.core.util.ByteSequenceMatcher;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ByteSequenceMatcherTest {
    @Test
    void testMatchesAcrossSequences() {
        byte[] sequence = {(byte) 0xab, (byte) 0x19, (byte) 0xa3, (byte) 0x23};
        byte[] buffer1 = {(byte) 0x00, (byte) 0x01, (byte) 0x23, (byte) 0x35, (byte) 0xa6, (byte) 0xab, (byte) 0x19};
        byte[] buffer2 = {(byte) 0x00, (byte) 0xa3, (byte) 0x23, (byte) 0xab, (byte) 0x19};
        byte[] buffer3 = {(byte) 0xa3, (byte) 0x23, (byte) 0x34};
        byte[] buffer4 = {(byte) 0x00, (byte) 0x34, (byte) 0xf5, (byte) 0xab, (byte) 0x19, (byte) 0xa3, (byte) 0x23, (byte) 0x34};
        ByteSequenceMatcher byteSequenceMatcher = new ByteSequenceMatcher(sequence);
        Assertions.assertEquals(-1, byteSequenceMatcher.checkBytes(buffer1, buffer1.length));
        Assertions.assertEquals(-1, byteSequenceMatcher.checkBytes(buffer2, buffer2.length));
        Assertions.assertEquals(1, byteSequenceMatcher.checkBytes(buffer3, buffer3.length));
        Assertions.assertEquals(buffer4.length-2, byteSequenceMatcher.checkBytes(buffer4, buffer4.length));
    }
}
