package org.telegram.ui;

import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes3.dex */
public final /* synthetic */ class TwoStepVerificationActivity$$ExternalSyntheticLambda20 implements Runnable {
    public final /* synthetic */ TwoStepVerificationActivity f$0;
    public final /* synthetic */ TLRPC$TL_error f$1;
    public final /* synthetic */ TLObject f$2;

    public /* synthetic */ TwoStepVerificationActivity$$ExternalSyntheticLambda20(TwoStepVerificationActivity twoStepVerificationActivity, TLRPC$TL_error tLRPC$TL_error, TLObject tLObject) {
        this.f$0 = twoStepVerificationActivity;
        this.f$1 = tLRPC$TL_error;
        this.f$2 = tLObject;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$processDone$31(this.f$1, this.f$2);
    }
}