package org.telegram.ui;

import java.util.HashMap;
import org.telegram.tgnet.TLObject;
import org.telegram.ui.LoginActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class LoginActivity$PhoneView$$ExternalSyntheticLambda13 implements Runnable {
    public final /* synthetic */ LoginActivity.PhoneView f$0;
    public final /* synthetic */ TLObject f$1;
    public final /* synthetic */ HashMap f$2;

    public /* synthetic */ LoginActivity$PhoneView$$ExternalSyntheticLambda13(LoginActivity.PhoneView phoneView, TLObject tLObject, HashMap hashMap) {
        this.f$0 = phoneView;
        this.f$1 = tLObject;
        this.f$2 = hashMap;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$new$10(this.f$1, this.f$2);
    }
}