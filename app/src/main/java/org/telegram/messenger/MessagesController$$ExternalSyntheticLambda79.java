package org.telegram.messenger;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesController$$ExternalSyntheticLambda79 implements Runnable {
    public final /* synthetic */ MessagesController f$0;
    public final /* synthetic */ long f$1;
    public final /* synthetic */ Runnable f$2;

    public /* synthetic */ MessagesController$$ExternalSyntheticLambda79(MessagesController messagesController, long j, Runnable runnable) {
        this.f$0 = messagesController;
        this.f$1 = j;
        this.f$2 = runnable;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$setUserAdminRole$78(this.f$1, this.f$2);
    }
}