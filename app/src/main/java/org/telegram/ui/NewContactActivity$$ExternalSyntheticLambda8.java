package org.telegram.ui;

import org.telegram.ui.CountrySelectActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class NewContactActivity$$ExternalSyntheticLambda8 implements CountrySelectActivity.CountrySelectActivityDelegate {
    public final /* synthetic */ NewContactActivity f$0;

    public /* synthetic */ NewContactActivity$$ExternalSyntheticLambda8(NewContactActivity newContactActivity) {
        this.f$0 = newContactActivity;
    }

    @Override // org.telegram.ui.CountrySelectActivity.CountrySelectActivityDelegate
    public final void didSelectCountry(CountrySelectActivity.Country country) {
        this.f$0.lambda$createView$3(country);
    }
}