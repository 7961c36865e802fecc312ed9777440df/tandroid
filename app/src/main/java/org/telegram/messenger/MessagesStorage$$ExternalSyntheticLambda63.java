package org.telegram.messenger;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesStorage$$ExternalSyntheticLambda63 implements Runnable {
    public final /* synthetic */ MessagesStorage f$0;
    public final /* synthetic */ long f$1;

    public /* synthetic */ MessagesStorage$$ExternalSyntheticLambda63(MessagesStorage messagesStorage, long j) {
        this.f$0 = messagesStorage;
        this.f$1 = j;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$removePendingTask$10(this.f$1);
    }
}