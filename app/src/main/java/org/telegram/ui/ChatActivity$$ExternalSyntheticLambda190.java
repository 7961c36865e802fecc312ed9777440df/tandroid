package org.telegram.ui;

import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_messages_getWebPagePreview;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$$ExternalSyntheticLambda190 implements Runnable {
    public final /* synthetic */ ChatActivity f$0;
    public final /* synthetic */ TLRPC$TL_error f$1;
    public final /* synthetic */ TLObject f$2;
    public final /* synthetic */ TLRPC$TL_messages_getWebPagePreview f$3;

    public /* synthetic */ ChatActivity$$ExternalSyntheticLambda190(ChatActivity chatActivity, TLRPC$TL_error tLRPC$TL_error, TLObject tLObject, TLRPC$TL_messages_getWebPagePreview tLRPC$TL_messages_getWebPagePreview) {
        this.f$0 = chatActivity;
        this.f$1 = tLRPC$TL_error;
        this.f$2 = tLObject;
        this.f$3 = tLRPC$TL_messages_getWebPagePreview;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$searchLinks$98(this.f$1, this.f$2, this.f$3);
    }
}