package org.telegram.messenger;

import org.telegram.tgnet.TLRPC$Updates;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesController$$ExternalSyntheticLambda184 implements Runnable {
    public final /* synthetic */ MessagesController f$0;
    public final /* synthetic */ TLRPC$Updates f$1;

    public /* synthetic */ MessagesController$$ExternalSyntheticLambda184(MessagesController messagesController, TLRPC$Updates tLRPC$Updates) {
        this.f$0 = messagesController;
        this.f$1 = tLRPC$Updates;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$createChat$208(this.f$1);
    }
}