package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesController$$ExternalSyntheticLambda374 implements RequestDelegate {
    public static final /* synthetic */ MessagesController$$ExternalSyntheticLambda374 INSTANCE = new MessagesController$$ExternalSyntheticLambda374();

    private /* synthetic */ MessagesController$$ExternalSyntheticLambda374() {
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        MessagesController.lambda$markMentionsAsRead$207(tLObject, tLRPC$TL_error);
    }
}