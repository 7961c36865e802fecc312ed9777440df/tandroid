package com.google.android.exoplayer2.extractor.mp3;

import com.google.android.exoplayer2.audio.MpegAudioUtil;
import com.google.android.exoplayer2.extractor.SeekMap;
import com.google.android.exoplayer2.extractor.SeekPoint;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.Util;

/* loaded from: classes.dex */
final class XingSeeker implements Seeker {
    private final long dataEndPosition;
    private final long dataSize;
    private final long dataStartPosition;
    private final long durationUs;
    private final long[] tableOfContents;
    private final int xingFrameSize;

    private XingSeeker(long j, int i, long j2) {
        this(j, i, j2, -1L, null);
    }

    private XingSeeker(long j, int i, long j2, long j3, long[] jArr) {
        this.dataStartPosition = j;
        this.xingFrameSize = i;
        this.durationUs = j2;
        this.tableOfContents = jArr;
        this.dataSize = j3;
        this.dataEndPosition = j3 != -1 ? j + j3 : -1L;
    }

    public static XingSeeker create(long j, long j2, MpegAudioUtil.Header header, ParsableByteArray parsableByteArray) {
        int readUnsignedIntToInt;
        int i = header.samplesPerFrame;
        int i2 = header.sampleRate;
        int readInt = parsableByteArray.readInt();
        if ((readInt & 1) != 1 || (readUnsignedIntToInt = parsableByteArray.readUnsignedIntToInt()) == 0) {
            return null;
        }
        long scaleLargeTimestamp = Util.scaleLargeTimestamp(readUnsignedIntToInt, i * 1000000, i2);
        if ((readInt & 6) != 6) {
            return new XingSeeker(j2, header.frameSize, scaleLargeTimestamp);
        }
        long readUnsignedInt = parsableByteArray.readUnsignedInt();
        long[] jArr = new long[100];
        for (int i3 = 0; i3 < 100; i3++) {
            jArr[i3] = parsableByteArray.readUnsignedByte();
        }
        if (j != -1) {
            long j3 = j2 + readUnsignedInt;
            if (j != j3) {
                Log.w("XingSeeker", "XING data size mismatch: " + j + ", " + j3);
            }
        }
        return new XingSeeker(j2, header.frameSize, scaleLargeTimestamp, readUnsignedInt, jArr);
    }

    private long getTimeUsForTableIndex(int i) {
        return (this.durationUs * i) / 100;
    }

    @Override // com.google.android.exoplayer2.extractor.mp3.Seeker
    public long getDataEndPosition() {
        return this.dataEndPosition;
    }

    @Override // com.google.android.exoplayer2.extractor.SeekMap
    public long getDurationUs() {
        return this.durationUs;
    }

    @Override // com.google.android.exoplayer2.extractor.SeekMap
    public SeekMap.SeekPoints getSeekPoints(long j) {
        if (!isSeekable()) {
            return new SeekMap.SeekPoints(new SeekPoint(0L, this.dataStartPosition + this.xingFrameSize));
        }
        long constrainValue = Util.constrainValue(j, 0L, this.durationUs);
        double d = constrainValue;
        Double.isNaN(d);
        double d2 = this.durationUs;
        Double.isNaN(d2);
        double d3 = (d * 100.0d) / d2;
        double d4 = 0.0d;
        if (d3 > 0.0d) {
            if (d3 >= 100.0d) {
                d4 = 256.0d;
            } else {
                int i = (int) d3;
                double d5 = ((long[]) Assertions.checkStateNotNull(this.tableOfContents))[i];
                double d6 = i == 99 ? 256.0d : r3[i + 1];
                double d7 = i;
                Double.isNaN(d7);
                Double.isNaN(d5);
                Double.isNaN(d5);
                d4 = d5 + ((d3 - d7) * (d6 - d5));
            }
        }
        double d8 = this.dataSize;
        Double.isNaN(d8);
        return new SeekMap.SeekPoints(new SeekPoint(constrainValue, this.dataStartPosition + Util.constrainValue(Math.round((d4 / 256.0d) * d8), this.xingFrameSize, this.dataSize - 1)));
    }

    @Override // com.google.android.exoplayer2.extractor.mp3.Seeker
    public long getTimeUs(long j) {
        double d;
        long j2 = j - this.dataStartPosition;
        if (!isSeekable() || j2 <= this.xingFrameSize) {
            return 0L;
        }
        long[] jArr = (long[]) Assertions.checkStateNotNull(this.tableOfContents);
        double d2 = j2;
        Double.isNaN(d2);
        double d3 = this.dataSize;
        Double.isNaN(d3);
        double d4 = (d2 * 256.0d) / d3;
        int binarySearchFloor = Util.binarySearchFloor(jArr, (long) d4, true, true);
        long timeUsForTableIndex = getTimeUsForTableIndex(binarySearchFloor);
        long j3 = jArr[binarySearchFloor];
        int i = binarySearchFloor + 1;
        long timeUsForTableIndex2 = getTimeUsForTableIndex(i);
        long j4 = binarySearchFloor == 99 ? 256L : jArr[i];
        if (j3 == j4) {
            d = 0.0d;
        } else {
            double d5 = j3;
            Double.isNaN(d5);
            double d6 = j4 - j3;
            Double.isNaN(d6);
            d = (d4 - d5) / d6;
        }
        double d7 = timeUsForTableIndex2 - timeUsForTableIndex;
        Double.isNaN(d7);
        return timeUsForTableIndex + Math.round(d * d7);
    }

    @Override // com.google.android.exoplayer2.extractor.SeekMap
    public boolean isSeekable() {
        return this.tableOfContents != null;
    }
}
