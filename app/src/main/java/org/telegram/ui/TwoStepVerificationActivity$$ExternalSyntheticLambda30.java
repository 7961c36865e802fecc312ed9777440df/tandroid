package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
/* loaded from: classes4.dex */
public final /* synthetic */ class TwoStepVerificationActivity$$ExternalSyntheticLambda30 implements RequestDelegate {
    public static final /* synthetic */ TwoStepVerificationActivity$$ExternalSyntheticLambda30 INSTANCE = new TwoStepVerificationActivity$$ExternalSyntheticLambda30();

    private /* synthetic */ TwoStepVerificationActivity$$ExternalSyntheticLambda30() {
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
        TwoStepVerificationActivity.lambda$checkSecretValues$28(tLObject, tL_error);
    }
}
