package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatEditTypeActivity$$ExternalSyntheticLambda19 implements RequestDelegate {
    public final /* synthetic */ ChatEditTypeActivity f$0;
    public final /* synthetic */ boolean f$1;

    public /* synthetic */ ChatEditTypeActivity$$ExternalSyntheticLambda19(ChatEditTypeActivity chatEditTypeActivity, boolean z) {
        this.f$0 = chatEditTypeActivity;
        this.f$1 = z;
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        this.f$0.lambda$generateLink$19(this.f$1, tLObject, tLRPC$TL_error);
    }
}