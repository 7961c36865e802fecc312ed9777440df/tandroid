package org.telegram.messenger.voip;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
/* loaded from: classes4.dex */
public final /* synthetic */ class VoIPService$$ExternalSyntheticLambda95 implements RequestDelegate {
    public static final /* synthetic */ VoIPService$$ExternalSyntheticLambda95 INSTANCE = new VoIPService$$ExternalSyntheticLambda95();

    private /* synthetic */ VoIPService$$ExternalSyntheticLambda95() {
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
        VoIPService.lambda$onSignalingData$60(tLObject, tL_error);
    }
}
