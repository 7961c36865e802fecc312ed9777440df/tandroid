package org.telegram.messenger;

import org.telegram.tgnet.TLRPC$Message;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesStorage$$ExternalSyntheticLambda172 implements Runnable {
    public final /* synthetic */ MessagesStorage f$0;
    public final /* synthetic */ TLRPC$Message f$1;
    public final /* synthetic */ boolean f$2;

    public /* synthetic */ MessagesStorage$$ExternalSyntheticLambda172(MessagesStorage messagesStorage, TLRPC$Message tLRPC$Message, boolean z) {
        this.f$0 = messagesStorage;
        this.f$1 = tLRPC$Message;
        this.f$2 = z;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$markMessageAsSendError$160(this.f$1, this.f$2);
    }
}