package org.webrtc;
/* loaded from: classes3.dex */
public final /* synthetic */ class SurfaceViewRenderer$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ SurfaceViewRenderer f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ int f$2;

    public /* synthetic */ SurfaceViewRenderer$$ExternalSyntheticLambda0(SurfaceViewRenderer surfaceViewRenderer, int i, int i2) {
        this.f$0 = surfaceViewRenderer;
        this.f$1 = i;
        this.f$2 = i2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onFrameResolutionChanged$0(this.f$1, this.f$2);
    }
}