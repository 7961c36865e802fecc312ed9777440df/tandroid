package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$$ExternalSyntheticLambda223 implements RequestDelegate {
    public final /* synthetic */ ChatActivity f$0;
    public final /* synthetic */ Runnable f$1;
    public final /* synthetic */ long[] f$2;

    public /* synthetic */ ChatActivity$$ExternalSyntheticLambda223(ChatActivity chatActivity, Runnable runnable, long[] jArr) {
        this.f$0 = chatActivity;
        this.f$1 = runnable;
        this.f$2 = jArr;
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        this.f$0.lambda$createMenu$167(this.f$1, this.f$2, tLObject, tLRPC$TL_error);
    }
}