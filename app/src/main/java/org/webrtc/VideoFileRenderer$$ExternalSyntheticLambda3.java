package org.webrtc;
/* loaded from: classes3.dex */
public final /* synthetic */ class VideoFileRenderer$$ExternalSyntheticLambda3 implements Runnable {
    public final /* synthetic */ VideoFileRenderer f$0;
    public final /* synthetic */ VideoFrame f$1;

    public /* synthetic */ VideoFileRenderer$$ExternalSyntheticLambda3(VideoFileRenderer videoFileRenderer, VideoFrame videoFrame) {
        this.f$0 = videoFileRenderer;
        this.f$1 = videoFrame;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onFrame$0(this.f$1);
    }
}