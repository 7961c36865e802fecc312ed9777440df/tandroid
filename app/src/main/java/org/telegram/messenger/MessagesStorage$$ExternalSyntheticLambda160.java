package org.telegram.messenger;

import org.telegram.tgnet.TLRPC$ChatFull;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesStorage$$ExternalSyntheticLambda160 implements Runnable {
    public final /* synthetic */ MessagesStorage f$0;
    public final /* synthetic */ TLRPC$ChatFull f$1;
    public final /* synthetic */ boolean f$2;

    public /* synthetic */ MessagesStorage$$ExternalSyntheticLambda160(MessagesStorage messagesStorage, TLRPC$ChatFull tLRPC$ChatFull, boolean z) {
        this.f$0 = messagesStorage;
        this.f$1 = tLRPC$ChatFull;
        this.f$2 = z;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$updateChatInfo$101(this.f$1, this.f$2);
    }
}