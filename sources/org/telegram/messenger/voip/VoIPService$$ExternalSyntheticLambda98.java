package org.telegram.messenger.voip;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes3.dex */
public final /* synthetic */ class VoIPService$$ExternalSyntheticLambda98 implements RequestDelegate {
    public static final /* synthetic */ VoIPService$$ExternalSyntheticLambda98 INSTANCE = new VoIPService$$ExternalSyntheticLambda98();

    private /* synthetic */ VoIPService$$ExternalSyntheticLambda98() {
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        VoIPService.lambda$callFailed$85(tLObject, tLRPC$TL_error);
    }
}