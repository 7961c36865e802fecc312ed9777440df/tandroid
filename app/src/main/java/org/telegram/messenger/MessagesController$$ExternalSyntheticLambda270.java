package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
/* loaded from: classes4.dex */
public final /* synthetic */ class MessagesController$$ExternalSyntheticLambda270 implements RequestDelegate {
    public static final /* synthetic */ MessagesController$$ExternalSyntheticLambda270 INSTANCE = new MessagesController$$ExternalSyntheticLambda270();

    private /* synthetic */ MessagesController$$ExternalSyntheticLambda270() {
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
        MessagesController.lambda$completeReadTask$199(tLObject, tL_error);
    }
}
