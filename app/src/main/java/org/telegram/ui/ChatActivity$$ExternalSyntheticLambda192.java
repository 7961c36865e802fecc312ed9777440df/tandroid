package org.telegram.ui;

import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$User;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$$ExternalSyntheticLambda192 implements Runnable {
    public final /* synthetic */ ChatActivity f$0;
    public final /* synthetic */ TLRPC$TL_error f$1;
    public final /* synthetic */ TLRPC$User f$2;

    public /* synthetic */ ChatActivity$$ExternalSyntheticLambda192(ChatActivity chatActivity, TLRPC$TL_error tLRPC$TL_error, TLRPC$User tLRPC$User) {
        this.f$0 = chatActivity;
        this.f$1 = tLRPC$TL_error;
        this.f$2 = tLRPC$User;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onTransitionAnimationEnd$131(this.f$1, this.f$2);
    }
}