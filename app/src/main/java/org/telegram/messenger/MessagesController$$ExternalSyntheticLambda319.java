package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesController$$ExternalSyntheticLambda319 implements RequestDelegate {
    public final /* synthetic */ MessagesController f$0;
    public final /* synthetic */ TLRPC$Chat f$1;
    public final /* synthetic */ long f$2;
    public final /* synthetic */ long f$3;
    public final /* synthetic */ int f$4;

    public /* synthetic */ MessagesController$$ExternalSyntheticLambda319(MessagesController messagesController, TLRPC$Chat tLRPC$Chat, long j, long j2, int i) {
        this.f$0 = messagesController;
        this.f$1 = tLRPC$Chat;
        this.f$2 = j;
        this.f$3 = j2;
        this.f$4 = i;
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        this.f$0.lambda$loadFullChat$48(this.f$1, this.f$2, this.f$3, this.f$4, tLObject, tLRPC$TL_error);
    }
}