package org.telegram.messenger;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesStorage$$ExternalSyntheticLambda73 implements Runnable {
    public final /* synthetic */ MessagesStorage f$0;
    public final /* synthetic */ long f$1;
    public final /* synthetic */ int f$2;
    public final /* synthetic */ long f$3;
    public final /* synthetic */ long f$4;
    public final /* synthetic */ int f$5;

    public /* synthetic */ MessagesStorage$$ExternalSyntheticLambda73(MessagesStorage messagesStorage, long j, int i, long j2, long j3, int i2) {
        this.f$0 = messagesStorage;
        this.f$1 = j;
        this.f$2 = i;
        this.f$3 = j2;
        this.f$4 = j3;
        this.f$5 = i2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$updateChatInfo$107(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5);
    }
}