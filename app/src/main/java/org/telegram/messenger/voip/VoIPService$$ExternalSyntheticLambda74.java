package org.telegram.messenger.voip;

import org.telegram.messenger.voip.NativeInstance;
/* loaded from: classes.dex */
public final /* synthetic */ class VoIPService$$ExternalSyntheticLambda74 implements NativeInstance.RequestBroadcastPartCallback {
    public final /* synthetic */ VoIPService f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ VoIPService$$ExternalSyntheticLambda74(VoIPService voIPService, int i) {
        this.f$0 = voIPService;
        this.f$1 = i;
    }

    @Override // org.telegram.messenger.voip.NativeInstance.RequestBroadcastPartCallback
    public final void run(long j, long j2, int i, int i2) {
        this.f$0.lambda$createGroupInstance$45(this.f$1, j, j2, i, i2);
    }
}