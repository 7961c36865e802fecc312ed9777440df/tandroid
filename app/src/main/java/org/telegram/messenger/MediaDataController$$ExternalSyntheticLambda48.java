package org.telegram.messenger;
/* loaded from: classes.dex */
public final /* synthetic */ class MediaDataController$$ExternalSyntheticLambda48 implements Runnable {
    public final /* synthetic */ MediaDataController f$0;
    public final /* synthetic */ long f$1;
    public final /* synthetic */ int[] f$2;

    public /* synthetic */ MediaDataController$$ExternalSyntheticLambda48(MediaDataController mediaDataController, long j, int[] iArr) {
        this.f$0 = mediaDataController;
        this.f$1 = j;
        this.f$2 = iArr;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$getMediaCounts$89(this.f$1, this.f$2);
    }
}