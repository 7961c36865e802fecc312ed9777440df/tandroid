package org.webrtc.audio;

import android.content.Context;
import android.media.AudioDeviceInfo;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioRecordingConfiguration;
import android.os.Build;
import android.os.Process;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import org.webrtc.Logging;
import org.webrtc.ThreadUtils;
import org.webrtc.audio.JavaAudioDeviceModule;

/* loaded from: classes5.dex */
class WebRtcAudioRecord {
    private static final int AUDIO_RECORD_START = 0;
    private static final int AUDIO_RECORD_STOP = 1;
    private static final long AUDIO_RECORD_THREAD_JOIN_TIMEOUT_MS = 2000;
    private static final int BUFFERS_PER_SECOND = 100;
    private static final int BUFFER_SIZE_FACTOR = 2;
    private static final int CALLBACK_BUFFER_SIZE_MS = 10;
    private static final int CHECK_REC_STATUS_DELAY_MS = 100;
    public static final int DEFAULT_AUDIO_FORMAT = 2;
    public static final int DEFAULT_AUDIO_SOURCE = 7;
    private static final String TAG = "WebRtcAudioRecordExternal";
    private static final AtomicInteger nextSchedulerId = new AtomicInteger(0);
    private final int audioFormat;
    private final AudioManager audioManager;
    private AudioRecord audioRecord;
    private final JavaAudioDeviceModule.SamplesReadyCallback audioSamplesReadyCallback;
    private final int audioSource;
    private final AtomicReference<Boolean> audioSourceMatchesRecordingSessionRef;
    private AudioRecordThread audioThread;
    private ByteBuffer byteBuffer;
    private final Context context;
    private final WebRtcAudioEffects effects;
    private byte[] emptyBytes;
    private final JavaAudioDeviceModule.AudioRecordErrorCallback errorCallback;
    private final ScheduledExecutorService executor;
    private ScheduledFuture<String> future;
    private final boolean isAcousticEchoCancelerSupported;
    private final boolean isNoiseSuppressorSupported;
    private volatile boolean microphoneMute;
    private long nativeAudioRecord;
    private AudioDeviceInfo preferredDevice;
    private final JavaAudioDeviceModule.AudioRecordStateCallback stateCallback;

    private class AudioRecordThread extends Thread {
        private volatile boolean keepAlive;

        public AudioRecordThread(String str) {
            super(str);
            this.keepAlive = true;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            Process.setThreadPriority(-19);
            Logging.d(WebRtcAudioRecord.TAG, "AudioRecordThread" + WebRtcAudioUtils.getThreadInfo());
            WebRtcAudioRecord.assertTrue(WebRtcAudioRecord.this.audioRecord.getRecordingState() == 3);
            WebRtcAudioRecord.this.doAudioRecordStateCallback(0);
            System.nanoTime();
            while (this.keepAlive) {
                int read = WebRtcAudioRecord.this.audioRecord.read(WebRtcAudioRecord.this.byteBuffer, WebRtcAudioRecord.this.byteBuffer.capacity());
                if (read == WebRtcAudioRecord.this.byteBuffer.capacity()) {
                    if (WebRtcAudioRecord.this.microphoneMute) {
                        WebRtcAudioRecord.this.byteBuffer.clear();
                        WebRtcAudioRecord.this.byteBuffer.put(WebRtcAudioRecord.this.emptyBytes);
                    }
                    if (this.keepAlive) {
                        WebRtcAudioRecord webRtcAudioRecord = WebRtcAudioRecord.this;
                        webRtcAudioRecord.nativeDataIsRecorded(webRtcAudioRecord.nativeAudioRecord, read);
                    }
                    if (WebRtcAudioRecord.this.audioSamplesReadyCallback != null) {
                        WebRtcAudioRecord.this.audioSamplesReadyCallback.onWebRtcAudioRecordSamplesReady(new JavaAudioDeviceModule.AudioSamples(WebRtcAudioRecord.this.audioRecord.getAudioFormat(), WebRtcAudioRecord.this.audioRecord.getChannelCount(), WebRtcAudioRecord.this.audioRecord.getSampleRate(), Arrays.copyOfRange(WebRtcAudioRecord.this.byteBuffer.array(), WebRtcAudioRecord.this.byteBuffer.arrayOffset(), WebRtcAudioRecord.this.byteBuffer.capacity() + WebRtcAudioRecord.this.byteBuffer.arrayOffset())));
                    }
                } else {
                    String str = "AudioRecord.read failed: " + read;
                    Logging.e(WebRtcAudioRecord.TAG, str);
                    if (read == -3) {
                        this.keepAlive = false;
                        WebRtcAudioRecord.this.reportWebRtcAudioRecordError(str);
                    }
                }
            }
            try {
                if (WebRtcAudioRecord.this.audioRecord != null) {
                    WebRtcAudioRecord.this.audioRecord.stop();
                    WebRtcAudioRecord.this.doAudioRecordStateCallback(1);
                }
            } catch (IllegalStateException e) {
                Logging.e(WebRtcAudioRecord.TAG, "AudioRecord.stop failed: " + e.getMessage());
            }
        }

        public void stopThread() {
            Logging.d(WebRtcAudioRecord.TAG, "stopThread");
            this.keepAlive = false;
        }
    }

    WebRtcAudioRecord(Context context, AudioManager audioManager) {
        this(context, newDefaultScheduler(), audioManager, 7, 2, null, null, null, WebRtcAudioEffects.isAcousticEchoCancelerSupported(), WebRtcAudioEffects.isNoiseSuppressorSupported());
    }

    public WebRtcAudioRecord(Context context, ScheduledExecutorService scheduledExecutorService, AudioManager audioManager, int i, int i2, JavaAudioDeviceModule.AudioRecordErrorCallback audioRecordErrorCallback, JavaAudioDeviceModule.AudioRecordStateCallback audioRecordStateCallback, JavaAudioDeviceModule.SamplesReadyCallback samplesReadyCallback, boolean z, boolean z2) {
        this.effects = new WebRtcAudioEffects();
        this.audioSourceMatchesRecordingSessionRef = new AtomicReference<>();
        if (z && !WebRtcAudioEffects.isAcousticEchoCancelerSupported()) {
            throw new IllegalArgumentException("HW AEC not supported");
        }
        if (z2 && !WebRtcAudioEffects.isNoiseSuppressorSupported()) {
            throw new IllegalArgumentException("HW NS not supported");
        }
        this.context = context;
        this.executor = scheduledExecutorService;
        this.audioManager = audioManager;
        this.audioSource = i;
        this.audioFormat = i2;
        this.errorCallback = audioRecordErrorCallback;
        this.stateCallback = audioRecordStateCallback;
        this.audioSamplesReadyCallback = samplesReadyCallback;
        this.isAcousticEchoCancelerSupported = z;
        this.isNoiseSuppressorSupported = z2;
        Logging.d(TAG, "ctor" + WebRtcAudioUtils.getThreadInfo());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void assertTrue(boolean z) {
        if (!z) {
            throw new AssertionError("Expected condition to be true");
        }
    }

    private static String audioStateToString(int i) {
        return i != 0 ? i != 1 ? "INVALID" : "STOP" : "START";
    }

    private int channelCountToConfiguration(int i) {
        return i == 1 ? 16 : 12;
    }

    private static boolean checkDeviceMatch(AudioDeviceInfo audioDeviceInfo, AudioDeviceInfo audioDeviceInfo2) {
        int id;
        int id2;
        int type;
        int type2;
        id = audioDeviceInfo.getId();
        id2 = audioDeviceInfo2.getId();
        if (id == id2) {
            type = audioDeviceInfo.getType();
            type2 = audioDeviceInfo2.getType();
            if (type == type2) {
                return true;
            }
        }
        return false;
    }

    private static AudioRecord createAudioRecordOnLowerThanM(int i, int i2, int i3, int i4, int i5) {
        Logging.d(TAG, "createAudioRecordOnLowerThanM");
        return new AudioRecord(i, i2, i3, i4, i5);
    }

    private static AudioRecord createAudioRecordOnMOrHigher(int i, int i2, int i3, int i4, int i5) {
        AudioRecord.Builder audioSource;
        AudioFormat.Builder encoding;
        AudioFormat.Builder sampleRate;
        AudioFormat.Builder channelMask;
        AudioFormat build;
        AudioRecord.Builder audioFormat;
        AudioRecord.Builder bufferSizeInBytes;
        AudioRecord build2;
        Logging.d(TAG, "createAudioRecordOnMOrHigher");
        audioSource = new AudioRecord.Builder().setAudioSource(i);
        encoding = new AudioFormat.Builder().setEncoding(i4);
        sampleRate = encoding.setSampleRate(i2);
        channelMask = sampleRate.setChannelMask(i3);
        build = channelMask.build();
        audioFormat = audioSource.setAudioFormat(build);
        bufferSizeInBytes = audioFormat.setBufferSizeInBytes(i5);
        build2 = bufferSizeInBytes.build();
        return build2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doAudioRecordStateCallback(int i) {
        Logging.d(TAG, "doAudioRecordStateCallback: " + audioStateToString(i));
        JavaAudioDeviceModule.AudioRecordStateCallback audioRecordStateCallback = this.stateCallback;
        if (audioRecordStateCallback != null) {
            if (i == 0) {
                audioRecordStateCallback.onWebRtcAudioRecordStart();
            } else if (i == 1) {
                audioRecordStateCallback.onWebRtcAudioRecordStop();
            } else {
                Logging.e(TAG, "Invalid audio state");
            }
        }
    }

    private boolean enableBuiltInAEC(boolean z) {
        Logging.d(TAG, "enableBuiltInAEC(" + z + ")");
        return this.effects.setAEC(z);
    }

    private boolean enableBuiltInNS(boolean z) {
        Logging.d(TAG, "enableBuiltInNS(" + z + ")");
        return this.effects.setNS(z);
    }

    private static int getBytesPerSample(int i) {
        if (i == 13 || i == 1 || i == 2) {
            return 2;
        }
        if (i == 3) {
            return 1;
        }
        if (i == 4) {
            return 4;
        }
        throw new IllegalArgumentException("Bad audio format " + i);
    }

    private int initRecording(int i, int i2) {
        Logging.d(TAG, "initRecording(sampleRate=" + i + ", channels=" + i2 + ")");
        if (this.audioRecord != null) {
            reportWebRtcAudioRecordInitError("InitRecording called twice without StopRecording.");
            return -1;
        }
        int i3 = i / 100;
        ByteBuffer allocateDirect = ByteBuffer.allocateDirect(getBytesPerSample(this.audioFormat) * i2 * i3);
        this.byteBuffer = allocateDirect;
        if (!allocateDirect.hasArray()) {
            reportWebRtcAudioRecordInitError("ByteBuffer does not have backing array.");
            return -1;
        }
        Logging.d(TAG, "byteBuffer.capacity: " + this.byteBuffer.capacity());
        this.emptyBytes = new byte[this.byteBuffer.capacity()];
        nativeCacheDirectBufferAddress(this.nativeAudioRecord, this.byteBuffer);
        int channelCountToConfiguration = channelCountToConfiguration(i2);
        int minBufferSize = AudioRecord.getMinBufferSize(i, channelCountToConfiguration, this.audioFormat);
        if (minBufferSize == -1 || minBufferSize == -2) {
            reportWebRtcAudioRecordInitError("AudioRecord.getMinBufferSize failed: " + minBufferSize);
            return -1;
        }
        Logging.d(TAG, "AudioRecord.getMinBufferSize: " + minBufferSize);
        int max = Math.max(minBufferSize * 2, this.byteBuffer.capacity());
        Logging.d(TAG, "bufferSizeInBytes: " + max);
        try {
            if (Build.VERSION.SDK_INT >= 23) {
                this.audioRecord = createAudioRecordOnMOrHigher(this.audioSource, i, channelCountToConfiguration, this.audioFormat, max);
                this.audioSourceMatchesRecordingSessionRef.set(null);
                AudioDeviceInfo audioDeviceInfo = this.preferredDevice;
                if (audioDeviceInfo != null) {
                    setPreferredDevice(audioDeviceInfo);
                }
            } else {
                this.audioRecord = createAudioRecordOnLowerThanM(this.audioSource, i, channelCountToConfiguration, this.audioFormat, max);
                this.audioSourceMatchesRecordingSessionRef.set(null);
            }
            AudioRecord audioRecord = this.audioRecord;
            if (audioRecord == null || audioRecord.getState() != 1) {
                reportWebRtcAudioRecordInitError("Creation or initialization of audio recorder failed.");
                releaseAudioResources();
                return -1;
            }
            this.effects.enable(this.audioRecord.getAudioSessionId());
            logMainParameters();
            logMainParametersExtended();
            int logRecordingConfigurations = logRecordingConfigurations(this.audioRecord, false);
            if (logRecordingConfigurations != 0) {
                Logging.w(TAG, "Potential microphone conflict. Active sessions: " + logRecordingConfigurations);
            }
            return i3;
        } catch (IllegalArgumentException e) {
            e = e;
            reportWebRtcAudioRecordInitError(e.getMessage());
            releaseAudioResources();
            return -1;
        } catch (UnsupportedOperationException e2) {
            e = e2;
            reportWebRtcAudioRecordInitError(e.getMessage());
            releaseAudioResources();
            return -1;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ String lambda$scheduleLogRecordingConfigurationsTask$0(AudioRecord audioRecord) {
        if (this.audioRecord == audioRecord) {
            logRecordingConfigurations(audioRecord, true);
            return "Scheduled task is done";
        }
        Logging.d(TAG, "audio record has changed");
        return "Scheduled task is done";
    }

    private static boolean logActiveRecordingConfigs(int i, List<AudioRecordingConfiguration> list) {
        int clientAudioSource;
        int clientAudioSessionId;
        AudioFormat format;
        int channelCount;
        int channelIndexMask;
        int channelMask;
        int encoding;
        int sampleRate;
        AudioFormat clientFormat;
        int channelCount2;
        int channelIndexMask2;
        int channelMask2;
        int encoding2;
        int sampleRate2;
        AudioDeviceInfo audioDevice;
        boolean isSource;
        int type;
        int id;
        assertTrue(!list.isEmpty());
        Iterator<AudioRecordingConfiguration> it = list.iterator();
        String str = "AudioRecordingConfigurations: ";
        while (true) {
            Logging.d(TAG, str);
            if (!it.hasNext()) {
                return true;
            }
            AudioRecordingConfiguration m = WebRtcAudioRecord$$ExternalSyntheticApiModelOutline5.m(it.next());
            StringBuilder sb = new StringBuilder();
            clientAudioSource = m.getClientAudioSource();
            sb.append("  client audio source=");
            sb.append(WebRtcAudioUtils.audioSourceToString(clientAudioSource));
            sb.append(", client session id=");
            clientAudioSessionId = m.getClientAudioSessionId();
            sb.append(clientAudioSessionId);
            sb.append(" (");
            sb.append(i);
            sb.append(")");
            sb.append("\n");
            format = m.getFormat();
            sb.append("  Device AudioFormat: ");
            sb.append("channel count=");
            channelCount = format.getChannelCount();
            sb.append(channelCount);
            sb.append(", channel index mask=");
            channelIndexMask = format.getChannelIndexMask();
            sb.append(channelIndexMask);
            sb.append(", channel mask=");
            channelMask = format.getChannelMask();
            sb.append(WebRtcAudioUtils.channelMaskToString(channelMask));
            sb.append(", encoding=");
            encoding = format.getEncoding();
            sb.append(WebRtcAudioUtils.audioEncodingToString(encoding));
            sb.append(", sample rate=");
            sampleRate = format.getSampleRate();
            sb.append(sampleRate);
            sb.append("\n");
            clientFormat = m.getClientFormat();
            sb.append("  Client AudioFormat: ");
            sb.append("channel count=");
            channelCount2 = clientFormat.getChannelCount();
            sb.append(channelCount2);
            sb.append(", channel index mask=");
            channelIndexMask2 = clientFormat.getChannelIndexMask();
            sb.append(channelIndexMask2);
            sb.append(", channel mask=");
            channelMask2 = clientFormat.getChannelMask();
            sb.append(WebRtcAudioUtils.channelMaskToString(channelMask2));
            sb.append(", encoding=");
            encoding2 = clientFormat.getEncoding();
            sb.append(WebRtcAudioUtils.audioEncodingToString(encoding2));
            sb.append(", sample rate=");
            sampleRate2 = clientFormat.getSampleRate();
            sb.append(sampleRate2);
            sb.append("\n");
            audioDevice = m.getAudioDevice();
            if (audioDevice != null) {
                isSource = audioDevice.isSource();
                assertTrue(isSource);
                sb.append("  AudioDevice: ");
                sb.append("type=");
                type = audioDevice.getType();
                sb.append(WebRtcAudioUtils.deviceTypeToString(type));
                sb.append(", id=");
                id = audioDevice.getId();
                sb.append(id);
            }
            str = sb.toString();
        }
    }

    private void logMainParameters() {
        Logging.d(TAG, "AudioRecord: session ID: " + this.audioRecord.getAudioSessionId() + ", channels: " + this.audioRecord.getChannelCount() + ", sample rate: " + this.audioRecord.getSampleRate());
    }

    private void logMainParametersExtended() {
        int bufferSizeInFrames;
        if (Build.VERSION.SDK_INT >= 23) {
            StringBuilder sb = new StringBuilder();
            sb.append("AudioRecord: buffer size in frames: ");
            bufferSizeInFrames = this.audioRecord.getBufferSizeInFrames();
            sb.append(bufferSizeInFrames);
            Logging.d(TAG, sb.toString());
        }
    }

    private int logRecordingConfigurations(AudioRecord audioRecord, boolean z) {
        List activeRecordingConfigurations;
        AudioFormat format;
        AudioDeviceInfo routedDevice;
        if (Build.VERSION.SDK_INT < 24) {
            Logging.w(TAG, "AudioManager#getActiveRecordingConfigurations() requires N or higher");
            return 0;
        }
        if (audioRecord == null) {
            return 0;
        }
        activeRecordingConfigurations = this.audioManager.getActiveRecordingConfigurations();
        int size = activeRecordingConfigurations.size();
        Logging.d(TAG, "Number of active recording sessions: " + size);
        if (size > 0) {
            logActiveRecordingConfigs(audioRecord.getAudioSessionId(), activeRecordingConfigurations);
            if (z) {
                AtomicReference<Boolean> atomicReference = this.audioSourceMatchesRecordingSessionRef;
                int audioSource = audioRecord.getAudioSource();
                int audioSessionId = audioRecord.getAudioSessionId();
                format = audioRecord.getFormat();
                routedDevice = audioRecord.getRoutedDevice();
                atomicReference.set(Boolean.valueOf(verifyAudioConfig(audioSource, audioSessionId, format, routedDevice, activeRecordingConfigurations)));
            }
        }
        return size;
    }

    private native void nativeCacheDirectBufferAddress(long j, ByteBuffer byteBuffer);

    /* JADX INFO: Access modifiers changed from: private */
    public native void nativeDataIsRecorded(long j, int i);

    static ScheduledExecutorService newDefaultScheduler() {
        final AtomicInteger atomicInteger = new AtomicInteger(0);
        return Executors.newScheduledThreadPool(0, new ThreadFactory() { // from class: org.webrtc.audio.WebRtcAudioRecord.1
            @Override // java.util.concurrent.ThreadFactory
            public Thread newThread(Runnable runnable) {
                Thread newThread = Executors.defaultThreadFactory().newThread(runnable);
                newThread.setName(String.format("WebRtcAudioRecordScheduler-%s-%s", Integer.valueOf(WebRtcAudioRecord.nextSchedulerId.getAndIncrement()), Integer.valueOf(atomicInteger.getAndIncrement())));
                return newThread;
            }
        });
    }

    private void releaseAudioResources() {
        Logging.d(TAG, "releaseAudioResources");
        AudioRecord audioRecord = this.audioRecord;
        if (audioRecord != null) {
            audioRecord.release();
            this.audioRecord = null;
        }
        this.audioSourceMatchesRecordingSessionRef.set(null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void reportWebRtcAudioRecordError(String str) {
        Logging.e(TAG, "Run-time recording error: " + str);
        WebRtcAudioUtils.logAudioState(TAG, this.context, this.audioManager);
        JavaAudioDeviceModule.AudioRecordErrorCallback audioRecordErrorCallback = this.errorCallback;
        if (audioRecordErrorCallback != null) {
            audioRecordErrorCallback.onWebRtcAudioRecordError(str);
        }
    }

    private void reportWebRtcAudioRecordInitError(String str) {
        Logging.e(TAG, "Init recording error: " + str);
        WebRtcAudioUtils.logAudioState(TAG, this.context, this.audioManager);
        logRecordingConfigurations(this.audioRecord, false);
        JavaAudioDeviceModule.AudioRecordErrorCallback audioRecordErrorCallback = this.errorCallback;
        if (audioRecordErrorCallback != null) {
            audioRecordErrorCallback.onWebRtcAudioRecordInitError(str);
        }
    }

    private void reportWebRtcAudioRecordStartError(JavaAudioDeviceModule.AudioRecordStartErrorCode audioRecordStartErrorCode, String str) {
        Logging.e(TAG, "Start recording error: " + audioRecordStartErrorCode + ". " + str);
        WebRtcAudioUtils.logAudioState(TAG, this.context, this.audioManager);
        logRecordingConfigurations(this.audioRecord, false);
        JavaAudioDeviceModule.AudioRecordErrorCallback audioRecordErrorCallback = this.errorCallback;
        if (audioRecordErrorCallback != null) {
            audioRecordErrorCallback.onWebRtcAudioRecordStartError(audioRecordStartErrorCode, str);
        }
    }

    private void scheduleLogRecordingConfigurationsTask(final AudioRecord audioRecord) {
        Logging.d(TAG, "scheduleLogRecordingConfigurationsTask");
        if (Build.VERSION.SDK_INT < 24) {
            return;
        }
        Callable callable = new Callable() { // from class: org.webrtc.audio.WebRtcAudioRecord$$ExternalSyntheticLambda22
            @Override // java.util.concurrent.Callable
            public final Object call() {
                String lambda$scheduleLogRecordingConfigurationsTask$0;
                lambda$scheduleLogRecordingConfigurationsTask$0 = WebRtcAudioRecord.this.lambda$scheduleLogRecordingConfigurationsTask$0(audioRecord);
                return lambda$scheduleLogRecordingConfigurationsTask$0;
            }
        };
        ScheduledFuture<String> scheduledFuture = this.future;
        if (scheduledFuture != null && !scheduledFuture.isDone()) {
            this.future.cancel(true);
        }
        this.future = this.executor.schedule(callable, 100L, TimeUnit.MILLISECONDS);
    }

    private boolean startRecording() {
        Logging.d(TAG, "startRecording");
        assertTrue(this.audioRecord != null);
        assertTrue(this.audioThread == null);
        try {
            this.audioRecord.startRecording();
            if (this.audioRecord.getRecordingState() == 3) {
                AudioRecordThread audioRecordThread = new AudioRecordThread("AudioRecordJavaThread");
                this.audioThread = audioRecordThread;
                audioRecordThread.start();
                scheduleLogRecordingConfigurationsTask(this.audioRecord);
                return true;
            }
            reportWebRtcAudioRecordStartError(JavaAudioDeviceModule.AudioRecordStartErrorCode.AUDIO_RECORD_START_STATE_MISMATCH, "AudioRecord.startRecording failed - incorrect state: " + this.audioRecord.getRecordingState());
            return false;
        } catch (IllegalStateException e) {
            reportWebRtcAudioRecordStartError(JavaAudioDeviceModule.AudioRecordStartErrorCode.AUDIO_RECORD_START_EXCEPTION, "AudioRecord.startRecording failed: " + e.getMessage());
            return false;
        }
    }

    private boolean stopRecording() {
        Logging.d(TAG, "stopRecording");
        assertTrue(this.audioThread != null);
        ScheduledFuture<String> scheduledFuture = this.future;
        if (scheduledFuture != null) {
            if (!scheduledFuture.isDone()) {
                this.future.cancel(true);
            }
            this.future = null;
        }
        this.audioThread.stopThread();
        if (!ThreadUtils.joinUninterruptibly(this.audioThread, AUDIO_RECORD_THREAD_JOIN_TIMEOUT_MS)) {
            Logging.e(TAG, "Join of AudioRecordJavaThread timed out");
            WebRtcAudioUtils.logAudioState(TAG, this.context, this.audioManager);
        }
        this.audioThread = null;
        this.effects.release();
        releaseAudioResources();
        return true;
    }

    private static boolean verifyAudioConfig(int i, int i2, AudioFormat audioFormat, AudioDeviceInfo audioDeviceInfo, List<AudioRecordingConfiguration> list) {
        AudioDeviceInfo audioDevice;
        int clientAudioSource;
        int clientAudioSessionId;
        AudioFormat clientFormat;
        int encoding;
        int encoding2;
        AudioFormat clientFormat2;
        int sampleRate;
        int sampleRate2;
        AudioFormat clientFormat3;
        int channelMask;
        int channelMask2;
        AudioFormat clientFormat4;
        int channelIndexMask;
        int channelIndexMask2;
        AudioFormat format;
        int encoding3;
        AudioFormat format2;
        int sampleRate3;
        AudioFormat format3;
        int channelMask3;
        AudioFormat format4;
        int channelIndexMask3;
        assertTrue(!list.isEmpty());
        Iterator<AudioRecordingConfiguration> it = list.iterator();
        while (it.hasNext()) {
            AudioRecordingConfiguration m = WebRtcAudioRecord$$ExternalSyntheticApiModelOutline5.m(it.next());
            audioDevice = m.getAudioDevice();
            if (audioDevice != null) {
                clientAudioSource = m.getClientAudioSource();
                if (clientAudioSource == i) {
                    clientAudioSessionId = m.getClientAudioSessionId();
                    if (clientAudioSessionId == i2) {
                        clientFormat = m.getClientFormat();
                        encoding = clientFormat.getEncoding();
                        encoding2 = audioFormat.getEncoding();
                        if (encoding == encoding2) {
                            clientFormat2 = m.getClientFormat();
                            sampleRate = clientFormat2.getSampleRate();
                            sampleRate2 = audioFormat.getSampleRate();
                            if (sampleRate == sampleRate2) {
                                clientFormat3 = m.getClientFormat();
                                channelMask = clientFormat3.getChannelMask();
                                channelMask2 = audioFormat.getChannelMask();
                                if (channelMask == channelMask2) {
                                    clientFormat4 = m.getClientFormat();
                                    channelIndexMask = clientFormat4.getChannelIndexMask();
                                    channelIndexMask2 = audioFormat.getChannelIndexMask();
                                    if (channelIndexMask == channelIndexMask2) {
                                        format = m.getFormat();
                                        encoding3 = format.getEncoding();
                                        if (encoding3 != 0) {
                                            format2 = m.getFormat();
                                            sampleRate3 = format2.getSampleRate();
                                            if (sampleRate3 > 0) {
                                                format3 = m.getFormat();
                                                channelMask3 = format3.getChannelMask();
                                                if (channelMask3 == 0) {
                                                    format4 = m.getFormat();
                                                    channelIndexMask3 = format4.getChannelIndexMask();
                                                    if (channelIndexMask3 == 0) {
                                                        continue;
                                                    }
                                                }
                                                if (checkDeviceMatch(audioDevice, audioDeviceInfo)) {
                                                    Logging.d(TAG, "verifyAudioConfig: PASS");
                                                    return true;
                                                }
                                            } else {
                                                continue;
                                            }
                                        } else {
                                            continue;
                                        }
                                    } else {
                                        continue;
                                    }
                                } else {
                                    continue;
                                }
                            } else {
                                continue;
                            }
                        } else {
                            continue;
                        }
                    } else {
                        continue;
                    }
                } else {
                    continue;
                }
            }
        }
        Logging.e(TAG, "verifyAudioConfig: FAILED");
        return false;
    }

    boolean isAcousticEchoCancelerSupported() {
        return this.isAcousticEchoCancelerSupported;
    }

    boolean isAudioConfigVerified() {
        return this.audioSourceMatchesRecordingSessionRef.get() != null;
    }

    boolean isAudioSourceMatchingRecordingSession() {
        Boolean bool = this.audioSourceMatchesRecordingSessionRef.get();
        if (bool != null) {
            return bool.booleanValue();
        }
        Logging.w(TAG, "Audio configuration has not yet been verified");
        return false;
    }

    boolean isNoiseSuppressorSupported() {
        return this.isNoiseSuppressorSupported;
    }

    public void setMicrophoneMute(boolean z) {
        Logging.w(TAG, "setMicrophoneMute(" + z + ")");
        this.microphoneMute = z;
    }

    public void setNativeAudioRecord(long j) {
        this.nativeAudioRecord = j;
    }

    void setPreferredDevice(AudioDeviceInfo audioDeviceInfo) {
        Integer num;
        boolean preferredDevice;
        int id;
        StringBuilder sb = new StringBuilder();
        sb.append("setPreferredDevice ");
        if (audioDeviceInfo != null) {
            id = audioDeviceInfo.getId();
            num = Integer.valueOf(id);
        } else {
            num = null;
        }
        sb.append(num);
        Logging.d(TAG, sb.toString());
        this.preferredDevice = audioDeviceInfo;
        AudioRecord audioRecord = this.audioRecord;
        if (audioRecord != null) {
            preferredDevice = audioRecord.setPreferredDevice(audioDeviceInfo);
            if (preferredDevice) {
                return;
            }
            Logging.e(TAG, "setPreferredDevice failed");
        }
    }
}
