package org.telegram.ui;

import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.ui.PassportActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class PassportActivity$8$$ExternalSyntheticLambda8 implements Runnable {
    public final /* synthetic */ PassportActivity.AnonymousClass8 f$0;
    public final /* synthetic */ TLRPC$TL_error f$1;
    public final /* synthetic */ TLObject f$2;
    public final /* synthetic */ boolean f$3;

    public /* synthetic */ PassportActivity$8$$ExternalSyntheticLambda8(PassportActivity.AnonymousClass8 anonymousClass8, TLRPC$TL_error tLRPC$TL_error, TLObject tLObject, boolean z) {
        this.f$0 = anonymousClass8;
        this.f$1 = tLRPC$TL_error;
        this.f$2 = tLObject;
        this.f$3 = z;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$run$9(this.f$1, this.f$2, this.f$3);
    }
}