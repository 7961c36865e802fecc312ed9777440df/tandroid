package org.telegram.messenger.voip;
/* loaded from: classes.dex */
public final /* synthetic */ class VoIPService$$ExternalSyntheticLambda46 implements Runnable {
    public final /* synthetic */ VoIPService f$0;
    public final /* synthetic */ String f$1;
    public final /* synthetic */ int f$2;

    public /* synthetic */ VoIPService$$ExternalSyntheticLambda46(VoIPService voIPService, String str, int i) {
        this.f$0 = voIPService;
        this.f$1 = str;
        this.f$2 = i;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$createGroupInstance$44(this.f$1, this.f$2);
    }
}