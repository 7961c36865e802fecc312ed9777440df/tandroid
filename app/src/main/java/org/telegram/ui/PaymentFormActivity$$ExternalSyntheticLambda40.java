package org.telegram.ui;

import org.telegram.tgnet.TLRPC$TL_account_password;
/* loaded from: classes3.dex */
public final /* synthetic */ class PaymentFormActivity$$ExternalSyntheticLambda40 implements Runnable {
    public final /* synthetic */ PaymentFormActivity f$0;
    public final /* synthetic */ TLRPC$TL_account_password f$1;
    public final /* synthetic */ byte[] f$2;

    public /* synthetic */ PaymentFormActivity$$ExternalSyntheticLambda40(PaymentFormActivity paymentFormActivity, TLRPC$TL_account_password tLRPC$TL_account_password, byte[] bArr) {
        this.f$0 = paymentFormActivity;
        this.f$1 = tLRPC$TL_account_password;
        this.f$2 = bArr;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$checkPassword$61(this.f$1, this.f$2);
    }
}