package org.telegram.messenger.voip;
/* loaded from: classes.dex */
public final /* synthetic */ class VoIPService$$ExternalSyntheticLambda42 implements Runnable {
    public final /* synthetic */ VoIPService f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ long f$2;
    public final /* synthetic */ int f$3;

    public /* synthetic */ VoIPService$$ExternalSyntheticLambda42(VoIPService voIPService, int i, long j, int i2) {
        this.f$0 = voIPService;
        this.f$1 = i;
        this.f$2 = j;
        this.f$3 = i2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$createGroupInstance$46(this.f$1, this.f$2, this.f$3);
    }
}