package org.telegram.messenger;
/* loaded from: classes.dex */
public final /* synthetic */ class MediaDataController$$ExternalSyntheticLambda33 implements Runnable {
    public final /* synthetic */ MediaDataController f$0;
    public final /* synthetic */ long f$1;
    public final /* synthetic */ double f$2;

    public /* synthetic */ MediaDataController$$ExternalSyntheticLambda33(MediaDataController mediaDataController, long j, double d) {
        this.f$0 = mediaDataController;
        this.f$1 = j;
        this.f$2 = d;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$increasePeerRaiting$116(this.f$1, this.f$2);
    }
}