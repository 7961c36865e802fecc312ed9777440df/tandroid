package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_payments_sendPaymentForm;
/* loaded from: classes3.dex */
public final /* synthetic */ class PaymentFormActivity$$ExternalSyntheticLambda57 implements RequestDelegate {
    public final /* synthetic */ PaymentFormActivity f$0;
    public final /* synthetic */ TLRPC$TL_payments_sendPaymentForm f$1;

    public /* synthetic */ PaymentFormActivity$$ExternalSyntheticLambda57(PaymentFormActivity paymentFormActivity, TLRPC$TL_payments_sendPaymentForm tLRPC$TL_payments_sendPaymentForm) {
        this.f$0 = paymentFormActivity;
        this.f$1 = tLRPC$TL_payments_sendPaymentForm;
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        this.f$0.lambda$sendData$58(this.f$1, tLObject, tLRPC$TL_error);
    }
}