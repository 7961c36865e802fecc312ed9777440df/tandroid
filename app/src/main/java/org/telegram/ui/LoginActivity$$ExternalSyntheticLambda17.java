package org.telegram.ui;

import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes3.dex */
public final /* synthetic */ class LoginActivity$$ExternalSyntheticLambda17 implements Runnable {
    public final /* synthetic */ LoginActivity f$0;
    public final /* synthetic */ TLRPC$TL_error f$1;
    public final /* synthetic */ String f$2;
    public final /* synthetic */ String f$3;
    public final /* synthetic */ String f$4;

    public /* synthetic */ LoginActivity$$ExternalSyntheticLambda17(LoginActivity loginActivity, TLRPC$TL_error tLRPC$TL_error, String str, String str2, String str3) {
        this.f$0 = loginActivity;
        this.f$1 = tLRPC$TL_error;
        this.f$2 = str;
        this.f$3 = str2;
        this.f$4 = str3;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$tryResetAccount$18(this.f$1, this.f$2, this.f$3, this.f$4);
    }
}