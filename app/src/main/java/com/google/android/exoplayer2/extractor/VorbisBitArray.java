package com.google.android.exoplayer2.extractor;

import com.google.android.exoplayer2.util.Assertions;
/* loaded from: classes3.dex */
public final class VorbisBitArray {
    private int bitOffset;
    private final int byteLimit;
    private int byteOffset;
    private final byte[] data;

    public VorbisBitArray(byte[] data) {
        this.data = data;
        this.byteLimit = data.length;
    }

    public void reset() {
        this.byteOffset = 0;
        this.bitOffset = 0;
    }

    public boolean readBit() {
        boolean returnValue = (((this.data[this.byteOffset] & 255) >> this.bitOffset) & 1) == 1;
        skipBits(1);
        return returnValue;
    }

    public int readBits(int numBits) {
        int tempByteOffset = this.byteOffset;
        int bitsRead = Math.min(numBits, 8 - this.bitOffset);
        int tempByteOffset2 = tempByteOffset + 1;
        int returnValue = ((this.data[tempByteOffset] & 255) >> this.bitOffset) & (255 >> (8 - bitsRead));
        while (bitsRead < numBits) {
            returnValue |= (this.data[tempByteOffset2] & 255) << bitsRead;
            bitsRead += 8;
            tempByteOffset2++;
        }
        int returnValue2 = returnValue & ((-1) >>> (32 - numBits));
        skipBits(numBits);
        return returnValue2;
    }

    public void skipBits(int numBits) {
        int numBytes = numBits / 8;
        int i = this.byteOffset + numBytes;
        this.byteOffset = i;
        int i2 = this.bitOffset + (numBits - (numBytes * 8));
        this.bitOffset = i2;
        if (i2 > 7) {
            this.byteOffset = i + 1;
            this.bitOffset = i2 - 8;
        }
        assertValidOffset();
    }

    public int getPosition() {
        return (this.byteOffset * 8) + this.bitOffset;
    }

    public void setPosition(int position) {
        int i = position / 8;
        this.byteOffset = i;
        this.bitOffset = position - (i * 8);
        assertValidOffset();
    }

    public int bitsLeft() {
        return ((this.byteLimit - this.byteOffset) * 8) - this.bitOffset;
    }

    private void assertValidOffset() {
        int i;
        int i2 = this.byteOffset;
        Assertions.checkState(i2 >= 0 && (i2 < (i = this.byteLimit) || (i2 == i && this.bitOffset == 0)));
    }
}
