package org.telegram.messenger;
/* loaded from: classes.dex */
public final /* synthetic */ class MediaDataController$$ExternalSyntheticLambda21 implements Runnable {
    public final /* synthetic */ MediaDataController f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ int f$2;

    public /* synthetic */ MediaDataController$$ExternalSyntheticLambda21(MediaDataController mediaDataController, int i, int i2) {
        this.f$0 = mediaDataController;
        this.f$1 = i;
        this.f$2 = i2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$processLoadedStickers$74(this.f$1, this.f$2);
    }
}