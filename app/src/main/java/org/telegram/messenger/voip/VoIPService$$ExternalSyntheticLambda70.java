package org.telegram.messenger.voip;

import org.telegram.messenger.voip.NativeInstance;
/* loaded from: classes.dex */
public final /* synthetic */ class VoIPService$$ExternalSyntheticLambda70 implements NativeInstance.AudioLevelsCallback {
    public final /* synthetic */ VoIPService f$0;

    public /* synthetic */ VoIPService$$ExternalSyntheticLambda70(VoIPService voIPService) {
        this.f$0 = voIPService;
    }

    @Override // org.telegram.messenger.voip.NativeInstance.AudioLevelsCallback
    public final void run(int[] iArr, float[] fArr, boolean[] zArr) {
        this.f$0.lambda$initiateActualEncryptedCall$55(iArr, fArr, zArr);
    }
}