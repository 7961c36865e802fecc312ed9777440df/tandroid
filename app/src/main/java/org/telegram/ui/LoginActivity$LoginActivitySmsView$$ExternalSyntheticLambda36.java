package org.telegram.ui;

import android.os.Bundle;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.ui.LoginActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda36 implements RequestDelegate {
    public final /* synthetic */ LoginActivity.LoginActivitySmsView f$0;
    public final /* synthetic */ Bundle f$1;

    public /* synthetic */ LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda36(LoginActivity.LoginActivitySmsView loginActivitySmsView, Bundle bundle) {
        this.f$0 = loginActivitySmsView;
        this.f$1 = bundle;
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        this.f$0.lambda$new$3(this.f$1, tLObject, tLRPC$TL_error);
    }
}