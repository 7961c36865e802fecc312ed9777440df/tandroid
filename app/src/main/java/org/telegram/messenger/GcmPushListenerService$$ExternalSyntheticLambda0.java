package org.telegram.messenger;
/* loaded from: classes.dex */
public final /* synthetic */ class GcmPushListenerService$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ int f$0;

    public /* synthetic */ GcmPushListenerService$$ExternalSyntheticLambda0(int i) {
        this.f$0 = i;
    }

    @Override // java.lang.Runnable
    public final void run() {
        GcmPushListenerService.lambda$onMessageReceived$1(this.f$0);
    }
}