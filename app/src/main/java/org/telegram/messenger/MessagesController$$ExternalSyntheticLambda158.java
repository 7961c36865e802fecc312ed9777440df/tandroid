package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLRPC$TL_channels_editAdmin;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesController$$ExternalSyntheticLambda158 implements Runnable {
    public final /* synthetic */ MessagesController f$0;
    public final /* synthetic */ TLRPC$TL_channels_editAdmin f$1;
    public final /* synthetic */ RequestDelegate f$2;

    public /* synthetic */ MessagesController$$ExternalSyntheticLambda158(MessagesController messagesController, TLRPC$TL_channels_editAdmin tLRPC$TL_channels_editAdmin, RequestDelegate requestDelegate) {
        this.f$0 = messagesController;
        this.f$1 = tLRPC$TL_channels_editAdmin;
        this.f$2 = requestDelegate;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$setUserAdminRole$82(this.f$1, this.f$2);
    }
}