package org.telegram.ui;

import android.os.Bundle;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.ui.LoginActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class LoginActivity$PhoneView$$ExternalSyntheticLambda14 implements Runnable {
    public final /* synthetic */ LoginActivity.PhoneView f$0;
    public final /* synthetic */ TLRPC$TL_error f$1;
    public final /* synthetic */ Bundle f$2;
    public final /* synthetic */ TLObject f$3;
    public final /* synthetic */ String f$4;
    public final /* synthetic */ LoginActivity.PhoneInputData f$5;
    public final /* synthetic */ TLObject f$6;

    public /* synthetic */ LoginActivity$PhoneView$$ExternalSyntheticLambda14(LoginActivity.PhoneView phoneView, TLRPC$TL_error tLRPC$TL_error, Bundle bundle, TLObject tLObject, String str, LoginActivity.PhoneInputData phoneInputData, TLObject tLObject2) {
        this.f$0 = phoneView;
        this.f$1 = tLRPC$TL_error;
        this.f$2 = bundle;
        this.f$3 = tLObject;
        this.f$4 = str;
        this.f$5 = phoneInputData;
        this.f$6 = tLObject2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onNextPressed$19(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6);
    }
}