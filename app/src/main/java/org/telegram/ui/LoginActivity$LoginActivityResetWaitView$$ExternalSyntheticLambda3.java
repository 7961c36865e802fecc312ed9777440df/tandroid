package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.ui.LoginActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class LoginActivity$LoginActivityResetWaitView$$ExternalSyntheticLambda3 implements RequestDelegate {
    public final /* synthetic */ LoginActivity.LoginActivityResetWaitView f$0;

    public /* synthetic */ LoginActivity$LoginActivityResetWaitView$$ExternalSyntheticLambda3(LoginActivity.LoginActivityResetWaitView loginActivityResetWaitView) {
        this.f$0 = loginActivityResetWaitView;
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        this.f$0.lambda$new$1(tLObject, tLRPC$TL_error);
    }
}