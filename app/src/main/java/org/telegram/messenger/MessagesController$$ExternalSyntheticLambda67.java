package org.telegram.messenger;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesController$$ExternalSyntheticLambda67 implements Runnable {
    public final /* synthetic */ MessagesController f$0;
    public final /* synthetic */ long f$1;
    public final /* synthetic */ int f$2;
    public final /* synthetic */ int f$3;
    public final /* synthetic */ long f$4;
    public final /* synthetic */ Runnable f$5;

    public /* synthetic */ MessagesController$$ExternalSyntheticLambda67(MessagesController messagesController, long j, int i, int i2, long j2, Runnable runnable) {
        this.f$0 = messagesController;
        this.f$1 = j;
        this.f$2 = i;
        this.f$3 = i2;
        this.f$4 = j2;
        this.f$5 = runnable;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$deleteMessagesRange$354(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5);
    }
}