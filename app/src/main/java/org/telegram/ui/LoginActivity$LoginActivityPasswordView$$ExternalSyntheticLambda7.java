package org.telegram.ui;

import org.telegram.tgnet.TLObject;
import org.telegram.ui.LoginActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class LoginActivity$LoginActivityPasswordView$$ExternalSyntheticLambda7 implements Runnable {
    public final /* synthetic */ LoginActivity.LoginActivityPasswordView f$0;
    public final /* synthetic */ TLObject f$1;

    public /* synthetic */ LoginActivity$LoginActivityPasswordView$$ExternalSyntheticLambda7(LoginActivity.LoginActivityPasswordView loginActivityPasswordView, TLObject tLObject) {
        this.f$0 = loginActivityPasswordView;
        this.f$1 = tLObject;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onNextPressed$9(this.f$1);
    }
}