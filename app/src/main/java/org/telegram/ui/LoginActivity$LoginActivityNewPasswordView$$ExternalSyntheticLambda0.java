package org.telegram.ui;

import android.content.DialogInterface;
import org.telegram.tgnet.TLObject;
import org.telegram.ui.LoginActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class LoginActivity$LoginActivityNewPasswordView$$ExternalSyntheticLambda0 implements DialogInterface.OnClickListener {
    public final /* synthetic */ LoginActivity.LoginActivityNewPasswordView f$0;
    public final /* synthetic */ TLObject f$1;

    public /* synthetic */ LoginActivity$LoginActivityNewPasswordView$$ExternalSyntheticLambda0(LoginActivity.LoginActivityNewPasswordView loginActivityNewPasswordView, TLObject tLObject) {
        this.f$0 = loginActivityNewPasswordView;
        this.f$1 = tLObject;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$recoverPassword$6(this.f$1, dialogInterface, i);
    }
}