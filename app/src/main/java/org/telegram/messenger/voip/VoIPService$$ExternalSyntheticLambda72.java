package org.telegram.messenger.voip;

import org.telegram.messenger.voip.NativeInstance;
/* loaded from: classes.dex */
public final /* synthetic */ class VoIPService$$ExternalSyntheticLambda72 implements NativeInstance.PayloadCallback {
    public final /* synthetic */ VoIPService f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ VoIPService$$ExternalSyntheticLambda72(VoIPService voIPService, int i) {
        this.f$0 = voIPService;
        this.f$1 = i;
    }

    @Override // org.telegram.messenger.voip.NativeInstance.PayloadCallback
    public final void run(int i, String str) {
        this.f$0.lambda$createGroupInstance$36(this.f$1, i, str);
    }
}