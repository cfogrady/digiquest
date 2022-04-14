package com.digiquest.core.util;

public class RingBuffer {
    private int startingIndex;
    private int dataSize;
    private final byte[] buffer;

    public RingBuffer(byte[] buffer) {
        this.buffer = buffer;
        this.startingIndex = 0;
        this.dataSize = 0;
    }

    private byte getElement(int offset) {
        return buffer[(startingIndex + offset) % buffer.length];
    }

    public void addToBuffer(byte element) {
        if(dataSize < buffer.length) {
            dataSize++;
        } else {
            startingIndex++;
        }
        buffer[(startingIndex + dataSize) % buffer.length] = element;
    }

    public boolean matchesSequence(byte[] sequence) {
        for(int i = 0; i < sequence.length; i++) {
            if(getElement(i) != sequence[i]) {
                return false;
            }
        }
        return true;
    }

    public byte[] getRawBuffer() {
        return buffer;
    }
}
