package org.telegram.messenger.voip;
/* loaded from: classes.dex */
public final /* synthetic */ class NativeInstance$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ NativeInstance f$0;
    public final /* synthetic */ boolean f$1;
    public final /* synthetic */ boolean f$2;

    public /* synthetic */ NativeInstance$$ExternalSyntheticLambda2(NativeInstance nativeInstance, boolean z, boolean z2) {
        this.f$0 = nativeInstance;
        this.f$1 = z;
        this.f$2 = z2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onNetworkStateUpdated$0(this.f$1, this.f$2);
    }
}