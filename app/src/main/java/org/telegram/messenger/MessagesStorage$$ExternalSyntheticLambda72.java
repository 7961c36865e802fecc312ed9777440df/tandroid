package org.telegram.messenger;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesStorage$$ExternalSyntheticLambda72 implements Runnable {
    public final /* synthetic */ MessagesStorage f$0;
    public final /* synthetic */ long f$1;
    public final /* synthetic */ int f$2;
    public final /* synthetic */ long f$3;

    public /* synthetic */ MessagesStorage$$ExternalSyntheticLambda72(MessagesStorage messagesStorage, long j, int i, long j2) {
        this.f$0 = messagesStorage;
        this.f$1 = j;
        this.f$2 = i;
        this.f$3 = j2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$loadPendingTasks$12(this.f$1, this.f$2, this.f$3);
    }
}