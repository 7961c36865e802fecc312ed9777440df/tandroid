package com.google.android.exoplayer2.extractor.avi;

import com.google.android.exoplayer2.util.ParsableByteArray;
/* loaded from: classes.dex */
final class AviMainHeaderChunk implements AviChunk {
    public final int flags;
    public final int frameDurationUs;
    public final int totalFrames;

    @Override // com.google.android.exoplayer2.extractor.avi.AviChunk
    public int getType() {
        return 1751742049;
    }

    public static AviMainHeaderChunk parseFrom(ParsableByteArray parsableByteArray) {
        int readLittleEndianInt = parsableByteArray.readLittleEndianInt();
        parsableByteArray.skipBytes(8);
        int readLittleEndianInt2 = parsableByteArray.readLittleEndianInt();
        int readLittleEndianInt3 = parsableByteArray.readLittleEndianInt();
        parsableByteArray.skipBytes(4);
        int readLittleEndianInt4 = parsableByteArray.readLittleEndianInt();
        parsableByteArray.skipBytes(12);
        return new AviMainHeaderChunk(readLittleEndianInt, readLittleEndianInt2, readLittleEndianInt3, readLittleEndianInt4);
    }

    private AviMainHeaderChunk(int i, int i2, int i3, int i4) {
        this.frameDurationUs = i;
        this.flags = i2;
        this.totalFrames = i3;
    }

    public boolean hasIndex() {
        return (this.flags & 16) == 16;
    }
}
