package org.telegram.ui;

import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$$ExternalSyntheticLambda189 implements Runnable {
    public final /* synthetic */ ChatActivity f$0;
    public final /* synthetic */ TLRPC$TL_error f$1;

    public /* synthetic */ ChatActivity$$ExternalSyntheticLambda189(ChatActivity chatActivity, TLRPC$TL_error tLRPC$TL_error) {
        this.f$0 = chatActivity;
        this.f$1 = tLRPC$TL_error;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$processSelectedOption$214(this.f$1);
    }
}