package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes3.dex */
public final /* synthetic */ class MessagesController$$ExternalSyntheticLambda441 implements RequestDelegate {
    public static final /* synthetic */ MessagesController$$ExternalSyntheticLambda441 INSTANCE = new MessagesController$$ExternalSyntheticLambda441();

    private /* synthetic */ MessagesController$$ExternalSyntheticLambda441() {
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        MessagesController.lambda$installTheme$113(tLObject, tLRPC$TL_error);
    }
}