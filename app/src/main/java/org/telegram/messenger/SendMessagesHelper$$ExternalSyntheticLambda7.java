package org.telegram.messenger;
/* loaded from: classes.dex */
public final /* synthetic */ class SendMessagesHelper$$ExternalSyntheticLambda7 implements Runnable {
    public final /* synthetic */ String f$0;
    public final /* synthetic */ AccountInstance f$1;
    public final /* synthetic */ long f$2;
    public final /* synthetic */ boolean f$3;
    public final /* synthetic */ int f$4;

    public /* synthetic */ SendMessagesHelper$$ExternalSyntheticLambda7(String str, AccountInstance accountInstance, long j, boolean z, int i) {
        this.f$0 = str;
        this.f$1 = accountInstance;
        this.f$2 = j;
        this.f$3 = z;
        this.f$4 = i;
    }

    @Override // java.lang.Runnable
    public final void run() {
        SendMessagesHelper.lambda$prepareSendingText$83(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4);
    }
}