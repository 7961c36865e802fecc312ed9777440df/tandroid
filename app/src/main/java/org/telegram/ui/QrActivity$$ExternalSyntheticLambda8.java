package org.telegram.ui;

import android.util.Pair;
import org.telegram.tgnet.ResultCallback;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes3.dex */
public final /* synthetic */ class QrActivity$$ExternalSyntheticLambda8 implements ResultCallback {
    public final /* synthetic */ QrActivity f$0;
    public final /* synthetic */ boolean f$1;
    public final /* synthetic */ long f$2;

    public /* synthetic */ QrActivity$$ExternalSyntheticLambda8(QrActivity qrActivity, boolean z, long j) {
        this.f$0 = qrActivity;
        this.f$1 = z;
        this.f$2 = j;
    }

    @Override // org.telegram.tgnet.ResultCallback
    public final void onComplete(Object obj) {
        this.f$0.lambda$onItemSelected$5(this.f$1, this.f$2, (Pair) obj);
    }

    @Override // org.telegram.tgnet.ResultCallback
    public /* synthetic */ void onError(TLRPC$TL_error tLRPC$TL_error) {
        ResultCallback.CC.$default$onError(this, tLRPC$TL_error);
    }
}