package org.telegram.messenger;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesStorage$$ExternalSyntheticLambda45 implements Runnable {
    public final /* synthetic */ MessagesStorage f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ long f$2;
    public final /* synthetic */ int f$3;
    public final /* synthetic */ int f$4;

    public /* synthetic */ MessagesStorage$$ExternalSyntheticLambda45(MessagesStorage messagesStorage, int i, long j, int i2, int i3) {
        this.f$0 = messagesStorage;
        this.f$1 = i;
        this.f$2 = j;
        this.f$3 = i2;
        this.f$4 = i3;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$getDialogPhotos$65(this.f$1, this.f$2, this.f$3, this.f$4);
    }
}