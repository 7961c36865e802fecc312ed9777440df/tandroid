package org.telegram.messenger.video;

import android.media.MediaCodec;
import android.media.MediaCrypto;
import android.media.MediaFormat;
import android.os.Build;
import android.view.Surface;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.video.MediaCodecVideoConvertor;
import org.telegram.messenger.video.audio_input.AudioInput;

/* loaded from: classes3.dex */
public class AudioRecoder {
    private static final int BYTES_PER_SHORT = 2;
    ArrayList<AudioInput> audioInputs;
    private final MediaCodec encoder;
    private boolean encoderDone;
    private ByteBuffer[] encoderInputBuffers;
    private ByteBuffer[] encoderOutputBuffers;
    public final MediaFormat format;
    AudioInput mainInput;
    private int sampleRate;
    private long totalDurationUs;
    private final int TIMEOUT_USEC = 2500;
    private final int DEFAULT_SAMPLE_RATE = 44100;
    private final int DEFAULT_BIT_RATE = 128000;
    private final int DEFAULT_CHANNEL_COUNT = 2;
    private final MediaCodec.BufferInfo encoderOutputBufferInfo = new MediaCodec.BufferInfo();
    private boolean extractorDone = false;
    private boolean decoderDone = false;
    private boolean encoderInputDone = false;
    private int pendingAudioDecoderOutputBufferIndex = -1;
    private int channelCount = 2;
    private long encoderInputPresentationTimeUs = 0;

    public AudioRecoder(ArrayList<AudioInput> arrayList, long j) {
        this.sampleRate = 44100;
        this.audioInputs = arrayList;
        this.totalDurationUs = j;
        this.mainInput = arrayList.get(0);
        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i).getSampleRate() > this.sampleRate) {
                this.sampleRate = arrayList.get(i).getSampleRate();
            }
        }
        MediaCodec createEncoderByType = MediaCodec.createEncoderByType(MediaController.AUDIO_MIME_TYPE);
        this.encoder = createEncoderByType;
        MediaFormat createAudioFormat = MediaFormat.createAudioFormat(MediaController.AUDIO_MIME_TYPE, this.sampleRate, this.channelCount);
        this.format = createAudioFormat;
        createAudioFormat.setInteger("bitrate", 128000);
        createEncoderByType.configure(createAudioFormat, (Surface) null, (MediaCrypto) null, 1);
        createEncoderByType.start();
        this.encoderInputBuffers = createEncoderByType.getInputBuffers();
        this.encoderOutputBuffers = createEncoderByType.getOutputBuffers();
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            arrayList.get(i2).start(this.sampleRate, this.channelCount);
        }
    }

    private boolean isInputAvailable() {
        if (this.encoderInputPresentationTimeUs > this.totalDurationUs) {
            return false;
        }
        return this.mainInput.hasRemaining();
    }

    private void mix(ShortBuffer shortBuffer) {
        int remaining = shortBuffer.remaining();
        for (int i = 0; i < remaining && isInputAvailable(); i++) {
            boolean z = false;
            short s = 0;
            for (int i2 = 0; i2 < this.audioInputs.size() && isInputAvailable(); i2++) {
                if (this.audioInputs.get(i2).hasRemaining()) {
                    s = (short) (s + (((short) (r6.getNext() * r6.getVolume())) / this.audioInputs.size()));
                    z = true;
                }
            }
            if (z) {
                shortBuffer.put(s);
            }
        }
    }

    public void release() {
        try {
            this.encoder.stop();
            for (int i = 0; i < this.audioInputs.size(); i++) {
                this.audioInputs.get(i).release();
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public boolean step(MediaCodecVideoConvertor.Muxer muxer, int i) {
        int dequeueInputBuffer;
        if (!this.encoderInputDone && (dequeueInputBuffer = this.encoder.dequeueInputBuffer(2500L)) >= 0) {
            if (isInputAvailable()) {
                ShortBuffer asShortBuffer = (Build.VERSION.SDK_INT >= 21 ? this.encoder.getInputBuffer(dequeueInputBuffer) : this.encoder.getInputBuffers()[dequeueInputBuffer]).asShortBuffer();
                mix(asShortBuffer);
                this.encoder.queueInputBuffer(dequeueInputBuffer, 0, asShortBuffer.position() * 2, this.encoderInputPresentationTimeUs, 1);
                this.encoderInputPresentationTimeUs += AudioConversions.shortsToUs(asShortBuffer.position(), this.sampleRate, this.channelCount);
            } else {
                this.encoder.queueInputBuffer(dequeueInputBuffer, 0, 0, 0L, 4);
                this.encoderInputDone = true;
            }
        }
        if (!this.encoderDone) {
            int dequeueOutputBuffer = this.encoder.dequeueOutputBuffer(this.encoderOutputBufferInfo, 2500L);
            if (dequeueOutputBuffer == -1) {
                return this.encoderDone;
            }
            if (dequeueOutputBuffer == -3) {
                this.encoderOutputBuffers = this.encoder.getOutputBuffers();
            }
            if (dequeueOutputBuffer == -2) {
                return this.encoderDone;
            }
            ByteBuffer byteBuffer = this.encoderOutputBuffers[dequeueOutputBuffer];
            MediaCodec.BufferInfo bufferInfo = this.encoderOutputBufferInfo;
            if ((bufferInfo.flags & 2) != 0) {
                this.encoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                return this.encoderDone;
            }
            if (bufferInfo.size != 0) {
                muxer.writeSampleData(i, byteBuffer, bufferInfo, false);
            }
            if ((this.encoderOutputBufferInfo.flags & 4) != 0) {
                this.encoderDone = true;
            }
            this.encoder.releaseOutputBuffer(dequeueOutputBuffer, false);
        }
        return this.encoderDone;
    }
}
