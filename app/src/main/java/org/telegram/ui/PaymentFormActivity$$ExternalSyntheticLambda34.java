package org.telegram.ui;

import android.content.Intent;
/* loaded from: classes3.dex */
public final /* synthetic */ class PaymentFormActivity$$ExternalSyntheticLambda34 implements Runnable {
    public final /* synthetic */ PaymentFormActivity f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ Intent f$2;

    public /* synthetic */ PaymentFormActivity$$ExternalSyntheticLambda34(PaymentFormActivity paymentFormActivity, int i, Intent intent) {
        this.f$0 = paymentFormActivity;
        this.f$1 = i;
        this.f$2 = intent;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onActivityResultFragment$39(this.f$1, this.f$2);
    }
}