package org.telegram.ui;

import org.telegram.ui.LoginActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda21 implements Runnable {
    public final /* synthetic */ LoginActivity.LoginActivitySmsView f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ boolean f$2;

    public /* synthetic */ LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda21(LoginActivity.LoginActivitySmsView loginActivitySmsView, int i, boolean z) {
        this.f$0 = loginActivitySmsView;
        this.f$1 = i;
        this.f$2 = z;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$tryShowProgress$11(this.f$1, this.f$2);
    }
}