package org.telegram.messenger;

import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ProductDetailsResponseListener;
import java.util.List;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes3.dex */
public final /* synthetic */ class BillingController$$ExternalSyntheticLambda8 implements ProductDetailsResponseListener {
    public final /* synthetic */ BillingController f$0;

    public /* synthetic */ BillingController$$ExternalSyntheticLambda8(BillingController billingController) {
        this.f$0 = billingController;
    }

    @Override // com.android.billingclient.api.ProductDetailsResponseListener
    public final void onProductDetailsResponse(BillingResult billingResult, List list) {
        BillingController.$r8$lambda$cek-iSqMCb909zQh6w2CpEt3xZs(this.f$0, billingResult, list);
    }
}