package org.telegram.messenger;

import java.util.Map;
/* loaded from: classes.dex */
public final /* synthetic */ class GcmPushListenerService$$ExternalSyntheticLambda7 implements Runnable {
    public final /* synthetic */ GcmPushListenerService f$0;
    public final /* synthetic */ Map f$1;
    public final /* synthetic */ long f$2;

    public /* synthetic */ GcmPushListenerService$$ExternalSyntheticLambda7(GcmPushListenerService gcmPushListenerService, Map map, long j) {
        this.f$0 = gcmPushListenerService;
        this.f$1 = map;
        this.f$2 = j;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onMessageReceived$3(this.f$1, this.f$2);
    }
}