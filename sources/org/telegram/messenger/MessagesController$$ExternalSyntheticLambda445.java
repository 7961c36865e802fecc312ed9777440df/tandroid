package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes3.dex */
public final /* synthetic */ class MessagesController$$ExternalSyntheticLambda445 implements RequestDelegate {
    public static final /* synthetic */ MessagesController$$ExternalSyntheticLambda445 INSTANCE = new MessagesController$$ExternalSyntheticLambda445();

    private /* synthetic */ MessagesController$$ExternalSyntheticLambda445() {
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        MessagesController.lambda$hidePromoDialog$126(tLObject, tLRPC$TL_error);
    }
}