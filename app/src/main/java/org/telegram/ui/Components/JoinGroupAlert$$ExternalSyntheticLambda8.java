package org.telegram.ui.Components;

import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_messages_importChatInvite;
/* loaded from: classes3.dex */
public final /* synthetic */ class JoinGroupAlert$$ExternalSyntheticLambda8 implements Runnable {
    public final /* synthetic */ JoinGroupAlert f$0;
    public final /* synthetic */ TLRPC$TL_error f$1;
    public final /* synthetic */ TLObject f$2;
    public final /* synthetic */ TLRPC$TL_messages_importChatInvite f$3;

    public /* synthetic */ JoinGroupAlert$$ExternalSyntheticLambda8(JoinGroupAlert joinGroupAlert, TLRPC$TL_error tLRPC$TL_error, TLObject tLObject, TLRPC$TL_messages_importChatInvite tLRPC$TL_messages_importChatInvite) {
        this.f$0 = joinGroupAlert;
        this.f$1 = tLRPC$TL_error;
        this.f$2 = tLObject;
        this.f$3 = tLRPC$TL_messages_importChatInvite;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$new$9(this.f$1, this.f$2, this.f$3);
    }
}