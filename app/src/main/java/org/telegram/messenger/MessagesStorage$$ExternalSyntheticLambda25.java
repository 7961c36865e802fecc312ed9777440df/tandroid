package org.telegram.messenger;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesStorage$$ExternalSyntheticLambda25 implements Runnable {
    public final /* synthetic */ MessagesStorage f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ MessagesStorage$$ExternalSyntheticLambda25(MessagesStorage messagesStorage, int i) {
        this.f$0 = messagesStorage;
        this.f$1 = i;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$readAllDialogs$39(this.f$1);
    }
}