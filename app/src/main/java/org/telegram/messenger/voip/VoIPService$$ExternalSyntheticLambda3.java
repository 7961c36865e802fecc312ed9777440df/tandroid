package org.telegram.messenger.voip;

import android.media.AudioManager;
/* loaded from: classes.dex */
public final /* synthetic */ class VoIPService$$ExternalSyntheticLambda3 implements Runnable {
    public final /* synthetic */ AudioManager f$0;

    public /* synthetic */ VoIPService$$ExternalSyntheticLambda3(AudioManager audioManager) {
        this.f$0 = audioManager;
    }

    @Override // java.lang.Runnable
    public final void run() {
        VoIPService.lambda$updateBluetoothHeadsetState$80(this.f$0);
    }
}