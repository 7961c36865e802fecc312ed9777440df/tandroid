package org.telegram.messenger;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesStorage$$ExternalSyntheticLambda85 implements Runnable {
    public final /* synthetic */ MessagesStorage f$0;
    public final /* synthetic */ long f$1;
    public final /* synthetic */ long f$2;

    public /* synthetic */ MessagesStorage$$ExternalSyntheticLambda85(MessagesStorage messagesStorage, long j, long j2) {
        this.f$0 = messagesStorage;
        this.f$1 = j;
        this.f$2 = j2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$setDialogFlags$34(this.f$1, this.f$2);
    }
}