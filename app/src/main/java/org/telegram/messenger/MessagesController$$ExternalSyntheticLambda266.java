package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesController$$ExternalSyntheticLambda266 implements RequestDelegate {
    public final /* synthetic */ MessagesController f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ int f$2;
    public final /* synthetic */ Runnable f$3;

    public /* synthetic */ MessagesController$$ExternalSyntheticLambda266(MessagesController messagesController, int i, int i2, Runnable runnable) {
        this.f$0 = messagesController;
        this.f$1 = i;
        this.f$2 = i2;
        this.f$3 = runnable;
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        this.f$0.lambda$loadDialogs$162(this.f$1, this.f$2, this.f$3, tLObject, tLRPC$TL_error);
    }
}