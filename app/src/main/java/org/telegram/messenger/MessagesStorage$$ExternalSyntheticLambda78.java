package org.telegram.messenger;

import org.telegram.tgnet.TLRPC$TL_chatBannedRights;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesStorage$$ExternalSyntheticLambda78 implements Runnable {
    public final /* synthetic */ MessagesStorage f$0;
    public final /* synthetic */ long f$1;
    public final /* synthetic */ int f$2;
    public final /* synthetic */ TLRPC$TL_chatBannedRights f$3;

    public /* synthetic */ MessagesStorage$$ExternalSyntheticLambda78(MessagesStorage messagesStorage, long j, int i, TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights) {
        this.f$0 = messagesStorage;
        this.f$1 = j;
        this.f$2 = i;
        this.f$3 = tLRPC$TL_chatBannedRights;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$updateChatDefaultBannedRights$143(this.f$1, this.f$2, this.f$3);
    }
}