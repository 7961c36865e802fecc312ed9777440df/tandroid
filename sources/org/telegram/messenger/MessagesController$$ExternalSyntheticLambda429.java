package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes3.dex */
public final /* synthetic */ class MessagesController$$ExternalSyntheticLambda429 implements RequestDelegate {
    public static final /* synthetic */ MessagesController$$ExternalSyntheticLambda429 INSTANCE = new MessagesController$$ExternalSyntheticLambda429();

    private /* synthetic */ MessagesController$$ExternalSyntheticLambda429() {
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        MessagesController.lambda$completeReadTask$222(tLObject, tLRPC$TL_error);
    }
}