package org.telegram.messenger;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesController$$ExternalSyntheticLambda61 implements Runnable {
    public final /* synthetic */ MessagesController f$0;
    public final /* synthetic */ long f$1;

    public /* synthetic */ MessagesController$$ExternalSyntheticLambda61(MessagesController messagesController, long j) {
        this.f$0 = messagesController;
        this.f$1 = j;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$addUserToChat$241(this.f$1);
    }
}