package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_account_getPassword;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes3.dex */
public final /* synthetic */ class PaymentFormActivity$$ExternalSyntheticLambda54 implements RequestDelegate {
    public final /* synthetic */ PaymentFormActivity f$0;
    public final /* synthetic */ String f$1;
    public final /* synthetic */ TLRPC$TL_account_getPassword f$2;

    public /* synthetic */ PaymentFormActivity$$ExternalSyntheticLambda54(PaymentFormActivity paymentFormActivity, String str, TLRPC$TL_account_getPassword tLRPC$TL_account_getPassword) {
        this.f$0 = paymentFormActivity;
        this.f$1 = str;
        this.f$2 = tLRPC$TL_account_getPassword;
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        this.f$0.lambda$checkPassword$63(this.f$1, this.f$2, tLObject, tLRPC$TL_error);
    }
}