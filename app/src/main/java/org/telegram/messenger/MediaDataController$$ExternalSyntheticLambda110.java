package org.telegram.messenger;
/* loaded from: classes.dex */
public final /* synthetic */ class MediaDataController$$ExternalSyntheticLambda110 implements Runnable {
    public final /* synthetic */ MediaDataController f$0;
    public final /* synthetic */ boolean f$1;
    public final /* synthetic */ int f$2;

    public /* synthetic */ MediaDataController$$ExternalSyntheticLambda110(MediaDataController mediaDataController, boolean z, int i) {
        this.f$0 = mediaDataController;
        this.f$1 = z;
        this.f$2 = i;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$loadRecents$32(this.f$1, this.f$2);
    }
}