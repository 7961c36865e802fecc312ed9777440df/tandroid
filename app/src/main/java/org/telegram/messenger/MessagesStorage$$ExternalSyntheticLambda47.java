package org.telegram.messenger;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesStorage$$ExternalSyntheticLambda47 implements Runnable {
    public final /* synthetic */ MessagesStorage f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ long f$2;
    public final /* synthetic */ long f$3;

    public /* synthetic */ MessagesStorage$$ExternalSyntheticLambda47(MessagesStorage messagesStorage, int i, long j, long j2) {
        this.f$0 = messagesStorage;
        this.f$1 = i;
        this.f$2 = j;
        this.f$3 = j2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$markMentionMessageAsRead$81(this.f$1, this.f$2, this.f$3);
    }
}