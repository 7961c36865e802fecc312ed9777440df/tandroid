package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesController$$ExternalSyntheticLambda232 implements RequestDelegate {
    public final /* synthetic */ MessagesController f$0;

    public /* synthetic */ MessagesController$$ExternalSyntheticLambda232(MessagesController messagesController) {
        this.f$0 = messagesController;
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        this.f$0.lambda$sendBotStart$240(tLObject, tLRPC$TL_error);
    }
}