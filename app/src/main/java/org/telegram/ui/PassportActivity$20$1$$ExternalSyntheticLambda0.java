package org.telegram.ui;

import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_secureRequiredType;
import org.telegram.ui.PassportActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class PassportActivity$20$1$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ PassportActivity.AnonymousClass20.AnonymousClass1 f$0;
    public final /* synthetic */ TLObject f$1;
    public final /* synthetic */ String f$2;
    public final /* synthetic */ TLRPC$TL_secureRequiredType f$3;
    public final /* synthetic */ PassportActivity.PassportActivityDelegate f$4;
    public final /* synthetic */ TLRPC$TL_error f$5;
    public final /* synthetic */ PassportActivity.ErrorRunnable f$6;

    public /* synthetic */ PassportActivity$20$1$$ExternalSyntheticLambda0(PassportActivity.AnonymousClass20.AnonymousClass1 anonymousClass1, TLObject tLObject, String str, TLRPC$TL_secureRequiredType tLRPC$TL_secureRequiredType, PassportActivity.PassportActivityDelegate passportActivityDelegate, TLRPC$TL_error tLRPC$TL_error, PassportActivity.ErrorRunnable errorRunnable) {
        this.f$0 = anonymousClass1;
        this.f$1 = tLObject;
        this.f$2 = str;
        this.f$3 = tLRPC$TL_secureRequiredType;
        this.f$4 = passportActivityDelegate;
        this.f$5 = tLRPC$TL_error;
        this.f$6 = errorRunnable;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$run$1(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6);
    }
}