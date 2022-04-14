package com.digiquest.core.util;

public class ByteSequenceMatcher {
    private final byte[] sequence;
    private int matchedBytes;

    public ByteSequenceMatcher(byte[] sequence) {
        this.sequence = sequence;
        this.matchedBytes = 0;
    }

    /**
     * Looks for a sequence in a set of bytes. On a match, returns the index of the last byte matching the sequence.
     * If there is no match, returns -1. Remembers data from any previous calls.
     * @param buffer
     * @param bufferSize
     * @return
     */
    public int checkBytes(byte[] buffer, int bufferSize) {
        for(int i = 0; i < bufferSize ; i++) {
            if(buffer[i] == sequence[matchedBytes]) {
                matchedBytes++;
                if(matchedBytes == sequence.length) {
                    matchedBytes = 0;
                    return i;
                }
            } else {
                matchedBytes = 0;
            }
        }
        return -1;
    }

    public void reset() {
        matchedBytes = 0;
    }
}
