package org.telegram.messenger;

import org.telegram.tgnet.TLRPC$Dialog;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesController$$ExternalSyntheticLambda153 implements Runnable {
    public final /* synthetic */ MessagesController f$0;
    public final /* synthetic */ TLRPC$Dialog f$1;

    public /* synthetic */ MessagesController$$ExternalSyntheticLambda153(MessagesController messagesController, TLRPC$Dialog tLRPC$Dialog) {
        this.f$0 = messagesController;
        this.f$1 = tLRPC$Dialog;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$checkLastDialogMessage$185(this.f$1);
    }
}