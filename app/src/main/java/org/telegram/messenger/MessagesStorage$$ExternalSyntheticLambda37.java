package org.telegram.messenger;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesStorage$$ExternalSyntheticLambda37 implements Runnable {
    public final /* synthetic */ MessagesStorage f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ int f$2;
    public final /* synthetic */ byte[] f$3;

    public /* synthetic */ MessagesStorage$$ExternalSyntheticLambda37(MessagesStorage messagesStorage, int i, int i2, byte[] bArr) {
        this.f$0 = messagesStorage;
        this.f$1 = i;
        this.f$2 = i2;
        this.f$3 = bArr;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$saveSecretParams$7(this.f$1, this.f$2, this.f$3);
    }
}