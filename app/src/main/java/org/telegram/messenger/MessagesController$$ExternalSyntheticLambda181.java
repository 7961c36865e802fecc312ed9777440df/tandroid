package org.telegram.messenger;

import org.telegram.tgnet.TLRPC$TL_updatePeerBlocked;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesController$$ExternalSyntheticLambda181 implements Runnable {
    public final /* synthetic */ MessagesController f$0;
    public final /* synthetic */ TLRPC$TL_updatePeerBlocked f$1;

    public /* synthetic */ MessagesController$$ExternalSyntheticLambda181(MessagesController messagesController, TLRPC$TL_updatePeerBlocked tLRPC$TL_updatePeerBlocked) {
        this.f$0 = messagesController;
        this.f$1 = tLRPC$TL_updatePeerBlocked;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$processUpdateArray$312(this.f$1);
    }
}