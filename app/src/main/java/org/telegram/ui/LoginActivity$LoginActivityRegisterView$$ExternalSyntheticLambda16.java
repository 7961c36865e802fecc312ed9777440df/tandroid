package org.telegram.ui;

import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.ui.LoginActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class LoginActivity$LoginActivityRegisterView$$ExternalSyntheticLambda16 implements Runnable {
    public final /* synthetic */ LoginActivity.LoginActivityRegisterView f$0;
    public final /* synthetic */ TLObject f$1;
    public final /* synthetic */ TLRPC$TL_error f$2;

    public /* synthetic */ LoginActivity$LoginActivityRegisterView$$ExternalSyntheticLambda16(LoginActivity.LoginActivityRegisterView loginActivityRegisterView, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        this.f$0 = loginActivityRegisterView;
        this.f$1 = tLObject;
        this.f$2 = tLRPC$TL_error;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onNextPressed$18(this.f$1, this.f$2);
    }
}