package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
/* loaded from: classes4.dex */
public final /* synthetic */ class PaymentFormActivity$$ExternalSyntheticLambda56 implements RequestDelegate {
    public static final /* synthetic */ PaymentFormActivity$$ExternalSyntheticLambda56 INSTANCE = new PaymentFormActivity$$ExternalSyntheticLambda56();

    private /* synthetic */ PaymentFormActivity$$ExternalSyntheticLambda56() {
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
        PaymentFormActivity.lambda$createView$27(tLObject, tL_error);
    }
}
