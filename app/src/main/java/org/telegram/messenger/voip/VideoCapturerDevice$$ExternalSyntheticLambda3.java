package org.telegram.messenger.voip;
/* loaded from: classes.dex */
public final /* synthetic */ class VideoCapturerDevice$$ExternalSyntheticLambda3 implements Runnable {
    public final /* synthetic */ VideoCapturerDevice f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ VideoCapturerDevice$$ExternalSyntheticLambda3(VideoCapturerDevice videoCapturerDevice, int i) {
        this.f$0 = videoCapturerDevice;
        this.f$1 = i;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onStateChanged$6(this.f$1);
    }
}