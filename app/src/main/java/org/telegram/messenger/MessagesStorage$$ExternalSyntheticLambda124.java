package org.telegram.messenger;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesStorage$$ExternalSyntheticLambda124 implements Runnable {
    public final /* synthetic */ MessagesStorage f$0;
    public final /* synthetic */ String f$1;
    public final /* synthetic */ String f$2;

    public /* synthetic */ MessagesStorage$$ExternalSyntheticLambda124(MessagesStorage messagesStorage, String str, String str2) {
        this.f$0 = messagesStorage;
        this.f$1 = str;
        this.f$2 = str2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$applyPhoneBookUpdates$114(this.f$1, this.f$2);
    }
}