package org.telegram.messenger;
/* loaded from: classes.dex */
public final /* synthetic */ class SendMessagesHelper$$ExternalSyntheticLambda24 implements Runnable {
    public final /* synthetic */ SendMessagesHelper f$0;
    public final /* synthetic */ long f$1;
    public final /* synthetic */ int f$2;
    public final /* synthetic */ byte[] f$3;

    public /* synthetic */ SendMessagesHelper$$ExternalSyntheticLambda24(SendMessagesHelper sendMessagesHelper, long j, int i, byte[] bArr) {
        this.f$0 = sendMessagesHelper;
        this.f$1 = j;
        this.f$2 = i;
        this.f$3 = bArr;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$sendNotificationCallback$19(this.f$1, this.f$2, this.f$3);
    }
}