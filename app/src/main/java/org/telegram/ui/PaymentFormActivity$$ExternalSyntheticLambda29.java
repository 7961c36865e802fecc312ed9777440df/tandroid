package org.telegram.ui;

import android.view.KeyEvent;
import android.widget.TextView;
/* loaded from: classes3.dex */
public final /* synthetic */ class PaymentFormActivity$$ExternalSyntheticLambda29 implements TextView.OnEditorActionListener {
    public final /* synthetic */ PaymentFormActivity f$0;

    public /* synthetic */ PaymentFormActivity$$ExternalSyntheticLambda29(PaymentFormActivity paymentFormActivity) {
        this.f$0 = paymentFormActivity;
    }

    @Override // android.widget.TextView.OnEditorActionListener
    public final boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        boolean lambda$createView$7;
        lambda$createView$7 = this.f$0.lambda$createView$7(textView, i, keyEvent);
        return lambda$createView$7;
    }
}