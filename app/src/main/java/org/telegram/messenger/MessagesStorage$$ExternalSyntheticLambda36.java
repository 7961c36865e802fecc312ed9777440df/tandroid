package org.telegram.messenger;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesStorage$$ExternalSyntheticLambda36 implements Runnable {
    public final /* synthetic */ MessagesStorage f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ int f$2;
    public final /* synthetic */ int f$3;
    public final /* synthetic */ long[] f$4;

    public /* synthetic */ MessagesStorage$$ExternalSyntheticLambda36(MessagesStorage messagesStorage, int i, int i2, int i3, long[] jArr) {
        this.f$0 = messagesStorage;
        this.f$1 = i;
        this.f$2 = i2;
        this.f$3 = i3;
        this.f$4 = jArr;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$getDialogs$180(this.f$1, this.f$2, this.f$3, this.f$4);
    }
}