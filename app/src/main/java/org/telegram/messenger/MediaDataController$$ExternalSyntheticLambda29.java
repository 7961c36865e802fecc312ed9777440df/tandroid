package org.telegram.messenger;
/* loaded from: classes.dex */
public final /* synthetic */ class MediaDataController$$ExternalSyntheticLambda29 implements Runnable {
    public final /* synthetic */ MediaDataController f$0;
    public final /* synthetic */ long f$1;

    public /* synthetic */ MediaDataController$$ExternalSyntheticLambda29(MediaDataController mediaDataController, long j) {
        this.f$0 = mediaDataController;
        this.f$1 = j;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$loadPinnedMessages$121(this.f$1);
    }
}