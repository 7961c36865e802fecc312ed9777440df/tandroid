package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesController$$ExternalSyntheticLambda316 implements RequestDelegate {
    public final /* synthetic */ MessagesController f$0;
    public final /* synthetic */ String f$1;

    public /* synthetic */ MessagesController$$ExternalSyntheticLambda316(MessagesController messagesController, String str) {
        this.f$0 = messagesController;
        this.f$1 = str;
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        this.f$0.lambda$registerForPush$257(this.f$1, tLObject, tLRPC$TL_error);
    }
}