package org.webrtc.audio;

import android.media.AudioRecord;
import java.util.concurrent.Callable;
/* loaded from: classes3.dex */
public final /* synthetic */ class WebRtcAudioRecord$$ExternalSyntheticLambda0 implements Callable {
    public final /* synthetic */ WebRtcAudioRecord f$0;
    public final /* synthetic */ AudioRecord f$1;

    public /* synthetic */ WebRtcAudioRecord$$ExternalSyntheticLambda0(WebRtcAudioRecord webRtcAudioRecord, AudioRecord audioRecord) {
        this.f$0 = webRtcAudioRecord;
        this.f$1 = audioRecord;
    }

    @Override // java.util.concurrent.Callable
    public final Object call() {
        String lambda$scheduleLogRecordingConfigurationsTask$0;
        lambda$scheduleLogRecordingConfigurationsTask$0 = this.f$0.lambda$scheduleLogRecordingConfigurationsTask$0(this.f$1);
        return lambda$scheduleLogRecordingConfigurationsTask$0;
    }
}