package org.telegram.ui;

import org.telegram.tgnet.TLObject;
/* loaded from: classes3.dex */
public final /* synthetic */ class TwoStepVerificationActivity$$ExternalSyntheticLambda27 implements Runnable {
    public final /* synthetic */ TwoStepVerificationActivity f$0;
    public final /* synthetic */ byte[] f$1;
    public final /* synthetic */ TLObject f$2;
    public final /* synthetic */ byte[] f$3;

    public /* synthetic */ TwoStepVerificationActivity$$ExternalSyntheticLambda27(TwoStepVerificationActivity twoStepVerificationActivity, byte[] bArr, TLObject tLObject, byte[] bArr2) {
        this.f$0 = twoStepVerificationActivity;
        this.f$1 = bArr;
        this.f$2 = tLObject;
        this.f$3 = bArr2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$processDone$30(this.f$1, this.f$2, this.f$3);
    }
}