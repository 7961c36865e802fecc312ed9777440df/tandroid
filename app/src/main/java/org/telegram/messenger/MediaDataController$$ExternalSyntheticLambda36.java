package org.telegram.messenger;
/* loaded from: classes.dex */
public final /* synthetic */ class MediaDataController$$ExternalSyntheticLambda36 implements Runnable {
    public final /* synthetic */ MediaDataController f$0;
    public final /* synthetic */ long f$1;
    public final /* synthetic */ int f$2;
    public final /* synthetic */ double f$3;

    public /* synthetic */ MediaDataController$$ExternalSyntheticLambda36(MediaDataController mediaDataController, long j, int i, double d) {
        this.f$0 = mediaDataController;
        this.f$1 = j;
        this.f$2 = i;
        this.f$3 = d;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$savePeer$118(this.f$1, this.f$2, this.f$3);
    }
}