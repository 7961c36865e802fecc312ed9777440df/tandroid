package org.telegram.ui.Components;
/* loaded from: classes3.dex */
public final /* synthetic */ class VideoSeekPreviewImage$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ VideoSeekPreviewImage f$0;
    public final /* synthetic */ float f$1;
    public final /* synthetic */ long f$2;

    public /* synthetic */ VideoSeekPreviewImage$$ExternalSyntheticLambda2(VideoSeekPreviewImage videoSeekPreviewImage, float f, long j) {
        this.f$0 = videoSeekPreviewImage;
        this.f$1 = f;
        this.f$2 = j;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$setProgress$1(this.f$1, this.f$2);
    }
}