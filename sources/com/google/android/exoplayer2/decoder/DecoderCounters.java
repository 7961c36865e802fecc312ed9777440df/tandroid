package com.google.android.exoplayer2.decoder;

import com.google.android.exoplayer2.util.Util;
/* loaded from: classes.dex */
public final class DecoderCounters {
    public int decoderInitCount;
    public int decoderReleaseCount;
    public int droppedBufferCount;
    public int droppedInputBufferCount;
    public int droppedToKeyframeCount;
    public int maxConsecutiveDroppedBufferCount;
    public int queuedInputBufferCount;
    public int renderedOutputBufferCount;
    public int skippedInputBufferCount;
    public int skippedOutputBufferCount;
    public long totalVideoFrameProcessingOffsetUs;
    public int videoFrameProcessingOffsetCount;

    public synchronized void ensureUpdated() {
    }

    public void addVideoFrameProcessingOffset(long j) {
        addVideoFrameProcessingOffsets(j, 1);
    }

    private void addVideoFrameProcessingOffsets(long j, int i) {
        this.totalVideoFrameProcessingOffsetUs += j;
        this.videoFrameProcessingOffsetCount += i;
    }

    public String toString() {
        return Util.formatInvariant("DecoderCounters {\n decoderInits=%s,\n decoderReleases=%s\n queuedInputBuffers=%s\n skippedInputBuffers=%s\n renderedOutputBuffers=%s\n skippedOutputBuffers=%s\n droppedBuffers=%s\n droppedInputBuffers=%s\n maxConsecutiveDroppedBuffers=%s\n droppedToKeyframeEvents=%s\n totalVideoFrameProcessingOffsetUs=%s\n videoFrameProcessingOffsetCount=%s\n}", Integer.valueOf(this.decoderInitCount), Integer.valueOf(this.decoderReleaseCount), Integer.valueOf(this.queuedInputBufferCount), Integer.valueOf(this.skippedInputBufferCount), Integer.valueOf(this.renderedOutputBufferCount), Integer.valueOf(this.skippedOutputBufferCount), Integer.valueOf(this.droppedBufferCount), Integer.valueOf(this.droppedInputBufferCount), Integer.valueOf(this.maxConsecutiveDroppedBufferCount), Integer.valueOf(this.droppedToKeyframeCount), Long.valueOf(this.totalVideoFrameProcessingOffsetUs), Integer.valueOf(this.videoFrameProcessingOffsetCount));
    }
}
