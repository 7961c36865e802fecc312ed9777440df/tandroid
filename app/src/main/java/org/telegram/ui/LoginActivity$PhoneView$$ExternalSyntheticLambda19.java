package org.telegram.ui;

import android.os.Bundle;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.ui.LoginActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class LoginActivity$PhoneView$$ExternalSyntheticLambda19 implements RequestDelegate {
    public final /* synthetic */ LoginActivity.PhoneView f$0;
    public final /* synthetic */ Bundle f$1;
    public final /* synthetic */ String f$2;
    public final /* synthetic */ LoginActivity.PhoneInputData f$3;
    public final /* synthetic */ TLObject f$4;

    public /* synthetic */ LoginActivity$PhoneView$$ExternalSyntheticLambda19(LoginActivity.PhoneView phoneView, Bundle bundle, String str, LoginActivity.PhoneInputData phoneInputData, TLObject tLObject) {
        this.f$0 = phoneView;
        this.f$1 = bundle;
        this.f$2 = str;
        this.f$3 = phoneInputData;
        this.f$4 = tLObject;
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        this.f$0.lambda$onNextPressed$20(this.f$1, this.f$2, this.f$3, this.f$4, tLObject, tLRPC$TL_error);
    }
}