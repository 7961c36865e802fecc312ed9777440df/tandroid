package org.telegram.messenger;

import org.telegram.tgnet.TLRPC$messages_Dialogs;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesController$$ExternalSyntheticLambda195 implements Runnable {
    public final /* synthetic */ MessagesController f$0;
    public final /* synthetic */ TLRPC$messages_Dialogs f$1;
    public final /* synthetic */ boolean f$2;

    public /* synthetic */ MessagesController$$ExternalSyntheticLambda195(MessagesController messagesController, TLRPC$messages_Dialogs tLRPC$messages_Dialogs, boolean z) {
        this.f$0 = messagesController;
        this.f$1 = tLRPC$messages_Dialogs;
        this.f$2 = z;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$processDialogsUpdate$188(this.f$1, this.f$2);
    }
}