package org.telegram.messenger.voip;
/* loaded from: classes.dex */
public final /* synthetic */ class VoIPService$$ExternalSyntheticLambda41 implements Runnable {
    public final /* synthetic */ VoIPService f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ int f$2;

    public /* synthetic */ VoIPService$$ExternalSyntheticLambda41(VoIPService voIPService, int i, int i2) {
        this.f$0 = voIPService;
        this.f$1 = i;
        this.f$2 = i2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$initiateActualEncryptedCall$56(this.f$1, this.f$2);
    }
}