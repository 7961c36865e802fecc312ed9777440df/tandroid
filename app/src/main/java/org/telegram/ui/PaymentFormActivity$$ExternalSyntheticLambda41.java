package org.telegram.ui;

import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes3.dex */
public final /* synthetic */ class PaymentFormActivity$$ExternalSyntheticLambda41 implements Runnable {
    public final /* synthetic */ PaymentFormActivity f$0;
    public final /* synthetic */ TLRPC$TL_error f$1;

    public /* synthetic */ PaymentFormActivity$$ExternalSyntheticLambda41(PaymentFormActivity paymentFormActivity, TLRPC$TL_error tLRPC$TL_error) {
        this.f$0 = paymentFormActivity;
        this.f$1 = tLRPC$TL_error;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$sendSavePassword$40(this.f$1);
    }
}