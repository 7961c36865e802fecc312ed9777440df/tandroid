package org.telegram.messenger;

import org.telegram.tgnet.TLObject;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesController$$ExternalSyntheticLambda87 implements Runnable {
    public final /* synthetic */ MessagesController f$0;
    public final /* synthetic */ long f$1;
    public final /* synthetic */ TLObject f$2;

    public /* synthetic */ MessagesController$$ExternalSyntheticLambda87(MessagesController messagesController, long j, TLObject tLObject) {
        this.f$0 = messagesController;
        this.f$1 = j;
        this.f$2 = tLObject;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$loadPeerSettings$58(this.f$1, this.f$2);
    }
}