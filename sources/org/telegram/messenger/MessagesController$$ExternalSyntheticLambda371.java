package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesController$$ExternalSyntheticLambda371 implements RequestDelegate {
    public static final /* synthetic */ MessagesController$$ExternalSyntheticLambda371 INSTANCE = new MessagesController$$ExternalSyntheticLambda371();

    private /* synthetic */ MessagesController$$ExternalSyntheticLambda371() {
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        MessagesController.lambda$logStorageDir$23(tLObject, tLRPC$TL_error);
    }
}