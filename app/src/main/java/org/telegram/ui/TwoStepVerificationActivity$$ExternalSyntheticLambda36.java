package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes3.dex */
public final /* synthetic */ class TwoStepVerificationActivity$$ExternalSyntheticLambda36 implements RequestDelegate {
    public final /* synthetic */ TwoStepVerificationActivity f$0;
    public final /* synthetic */ byte[] f$1;
    public final /* synthetic */ byte[] f$2;

    public /* synthetic */ TwoStepVerificationActivity$$ExternalSyntheticLambda36(TwoStepVerificationActivity twoStepVerificationActivity, byte[] bArr, byte[] bArr2) {
        this.f$0 = twoStepVerificationActivity;
        this.f$1 = bArr;
        this.f$2 = bArr2;
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        this.f$0.lambda$processDone$34(this.f$1, this.f$2, tLObject, tLRPC$TL_error);
    }
}