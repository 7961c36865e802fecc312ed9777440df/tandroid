package org.telegram.messenger.voip;

import org.telegram.messenger.voip.VoIPService;
import org.webrtc.VideoSink;
/* loaded from: classes.dex */
public final /* synthetic */ class VoIPService$5$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ VoIPService.AnonymousClass5 f$0;
    public final /* synthetic */ String f$1;
    public final /* synthetic */ VideoSink f$2;
    public final /* synthetic */ boolean f$3;

    public /* synthetic */ VoIPService$5$$ExternalSyntheticLambda0(VoIPService.AnonymousClass5 anonymousClass5, String str, VideoSink videoSink, boolean z) {
        this.f$0 = anonymousClass5;
        this.f$1 = str;
        this.f$2 = videoSink;
        this.f$3 = z;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onFrame$0(this.f$1, this.f$2, this.f$3);
    }
}