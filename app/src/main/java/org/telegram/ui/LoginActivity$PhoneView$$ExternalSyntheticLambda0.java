package org.telegram.ui;

import android.content.DialogInterface;
import org.telegram.ui.LoginActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class LoginActivity$PhoneView$$ExternalSyntheticLambda0 implements DialogInterface.OnClickListener {
    public final /* synthetic */ LoginActivity.PhoneView f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ LoginActivity$PhoneView$$ExternalSyntheticLambda0(LoginActivity.PhoneView phoneView, int i) {
        this.f$0 = phoneView;
        this.f$1 = i;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$onNextPressed$16(this.f$1, dialogInterface, i);
    }
}