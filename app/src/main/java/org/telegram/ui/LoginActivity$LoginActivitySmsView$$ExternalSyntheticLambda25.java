package org.telegram.ui;

import android.os.Bundle;
import org.telegram.tgnet.TLObject;
import org.telegram.ui.LoginActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda25 implements Runnable {
    public final /* synthetic */ LoginActivity.LoginActivitySmsView f$0;
    public final /* synthetic */ Bundle f$1;
    public final /* synthetic */ TLObject f$2;

    public /* synthetic */ LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda25(LoginActivity.LoginActivitySmsView loginActivitySmsView, Bundle bundle, TLObject tLObject) {
        this.f$0 = loginActivitySmsView;
        this.f$1 = bundle;
        this.f$2 = tLObject;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$new$1(this.f$1, this.f$2);
    }
}