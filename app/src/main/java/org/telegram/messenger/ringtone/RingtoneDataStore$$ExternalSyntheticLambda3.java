package org.telegram.messenger.ringtone;

import org.telegram.tgnet.TLObject;
/* loaded from: classes.dex */
public final /* synthetic */ class RingtoneDataStore$$ExternalSyntheticLambda3 implements Runnable {
    public final /* synthetic */ RingtoneDataStore f$0;
    public final /* synthetic */ TLObject f$1;

    public /* synthetic */ RingtoneDataStore$$ExternalSyntheticLambda3(RingtoneDataStore ringtoneDataStore, TLObject tLObject) {
        this.f$0 = ringtoneDataStore;
        this.f$1 = tLObject;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$loadUserRingtones$1(this.f$1);
    }
}